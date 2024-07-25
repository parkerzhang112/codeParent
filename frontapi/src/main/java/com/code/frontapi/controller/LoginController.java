package com.code.frontapi.controller;

import com.code.baseservice.base.enums.ResultEnum;
import com.code.baseservice.base.exception.BaseException;
import com.code.baseservice.dto.ResponseResult;
import com.code.baseservice.dto.frontapi.LoginDto;
import com.code.baseservice.dto.frontapi.RegisterDto;
import com.code.baseservice.service.ZfAgentService;
import com.code.baseservice.service.ZfCodeService;
import com.code.baseservice.service.ZfTransRecordService;
import com.code.baseservice.service.impl.CaptchaService;
import com.code.frontapi.util.TokenUtil;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Api(tags = "流水", description = "流水api")
@Slf4j
@RestController
@RequestMapping(value = "/index")
public class LoginController {

    @Autowired
    ZfTransRecordService zfTransRecordService;

    @Autowired
    ZfCodeService zfCodeService;

    @Autowired
    TokenUtil tokenUtil;
    @Autowired
    ZfAgentService zfAgentService;

    @Autowired
    CaptchaService captchaService;

    @PostMapping(value ={"/login"})
    @ResponseBody
    public String login(@RequestBody LoginDto loginDto, HttpServletRequest request){
        ResponseResult responseResult = new ResponseResult();
        try {
            zfAgentService.login(loginDto);
            String token =  tokenUtil.getToken(loginDto.getUserName());
            responseResult.setCode(ResultEnum.SUCCESS.getCode());
            responseResult.setData(token);
            responseResult.setMsg("操作成功");
        }catch (BaseException e){
            responseResult.setCode(e.getCode());
            responseResult.setMsg(e.getMessage());
            //其他非法异常，重新上传;
        }catch (Exception e){
            log.error("异常流水流水内容", e);
            responseResult.setCode(ResultEnum.ERROR.getCode());
            responseResult.setMsg(ResultEnum.ERROR.getMsg());
        }
        return responseResult.toJsonString();
    }

    @CrossOrigin
    @GetMapping("/generate")
    public String generateCaptcha(HttpSession session) throws Exception {
        String captchaText = captchaService.generateCaptchaText();
        String captchaImage = captchaService.generateCaptchaImage(captchaText);
        log.info("session id {}", session.getId());
        session.setAttribute("captcha", captchaText);
        ResponseResult responseResult = new ResponseResult();
        responseResult.setCode(ResultEnum.SUCCESS.getCode());
        responseResult.setData(captchaImage);
        return  responseResult.toJsonString();
    }

    @PostMapping(value ={"/register"})
    @ResponseBody
    public String register(@RequestBody RegisterDto registerDto, HttpSession session){
        ResponseResult responseResult = new ResponseResult();
        try {
//            String captchaText = (String) session.getAttribute("captcha");
//            if(!captchaText.equals(registerDto.getCode())){
//                new BaseException(ResultEnum.VAILD_CODE_ERROR);
//            }
            String Account = zfAgentService.regsiter(registerDto);

            String token =  tokenUtil.getToken(Account);
            responseResult.setData(token);
        }catch (BaseException e){
            log.error("注册业务异常 ", e);
            responseResult.setMsg(e.getMessage());
            responseResult.setCode(e.getCode());
            //其他非法异常，重新上传;
        }catch (Exception e){
            log.error("注册程序异常 ", e);
            responseResult.setMsg(ResultEnum.ERROR.getMsg());
            responseResult.setCode(ResultEnum.ERROR.getCode());
        }
        return responseResult.toJsonString();
    }
}
