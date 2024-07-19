package com.code.frontapi.controller;

import com.code.baseservice.base.exception.BaseException;
import com.code.baseservice.dto.ResponseResult;
import com.code.baseservice.dto.frontapi.LoginDto;
import com.code.baseservice.service.ZfCodeService;
import com.code.baseservice.service.ZfTransRecordService;
import com.code.frontapi.util.TokenUtil;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

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

    @PostMapping(value ={"/login"})
    @ResponseBody
    public String login(@RequestBody LoginDto loginDto, HttpServletRequest request){
        ResponseResult responseResult = new ResponseResult();
        try {
            String token =  tokenUtil.getToken(loginDto.getUserName());
            responseResult.setData(token);
        }catch (BaseException e){
            //其他非法异常，重新上传;
        }catch (Exception e){
            log.error("异常流水流水内容 {}", e.getStackTrace());
        }
        responseResult.setMsg("操作成功");
        return responseResult.toJsonString();
    }


}
