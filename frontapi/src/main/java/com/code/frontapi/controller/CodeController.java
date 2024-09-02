package com.code.frontapi.controller;

import com.code.baseservice.base.enums.ResultEnum;
import com.code.baseservice.base.exception.BaseException;
import com.code.baseservice.dto.ResponseResult;
import com.code.baseservice.dto.frontapi.code.AddCodeDto;
import com.code.baseservice.dto.frontapi.code.EditCodeDto;
import com.code.baseservice.dto.frontapi.code.OpenCodeDto;
import com.code.baseservice.dto.frontapi.code.QueryCodeDto;
import com.code.baseservice.entity.ZfAgent;
import com.code.baseservice.entity.ZfCode;
import com.code.baseservice.service.ZfAgentService;
import com.code.baseservice.service.ZfCodeService;
import com.code.baseservice.service.impl.FileUploadService;
import com.code.baseservice.vo.ZfCodeVo;
import com.code.frontapi.util.TokenUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;

@Api(tags = "支付资料", description = "流水api")
@Slf4j
@RestController
@RequestMapping(value = "/user/code")
public class CodeController {

    @Autowired
    ZfCodeService zfCodeService;

    @Autowired
    TokenUtil tokenUtil;

    @Autowired
    ZfAgentService zfAgentService;

    @Autowired
    private FileUploadService fileUploadService;


    @PostMapping(value ={"/list"})
    @ResponseBody
    public PageInfo<ZfCodeVo> list(@RequestBody QueryCodeDto queryCodeDto, HttpServletRequest request){
        try {
            String token  =   request.getHeader("token");
            String account  = tokenUtil.parseToken(token).get("loginName");
            ZfAgent zfAgent = zfAgentService.queryByAcount(account);
            if(queryCodeDto.getCodeType() == -1){
                queryCodeDto.setCodeType(null);
            }
            PageHelper.startPage(queryCodeDto.getPageNum(), queryCodeDto.getPageSize());
            queryCodeDto.setAgentId(zfAgent.getAgentId());
            List<ZfCodeVo> vos =  zfCodeService.queryListByAgentId(queryCodeDto);
            return new PageInfo<>(vos);
        }catch (BaseException e){
            //其他非法异常，重新上传;
        }catch (Exception e){
            log.error("获取用户二维码异常 ", e);
        }
        return new PageInfo<>();
    }


    @PostMapping(value ={"/detail/{code_id}"})
    @ResponseBody
    public ResponseResult  detail(@PathVariable("code_id") Integer codeId, HttpServletRequest request){
        ResponseResult responseResult = new ResponseResult();
        try {
            ZfCode zfCode = zfCodeService.queryById(codeId);
            responseResult.setCode(ResultEnum.SUCCESS.getCode());
            responseResult.setData(zfCode);
        }catch (BaseException e){
            responseResult.setCode(e.getCode());
            responseResult.setData(e.getMessage());
            //其他非法异常，重新上传;
        }catch (Exception e){
            responseResult.setCode(ResultEnum.ERROR.getCode());
            responseResult.setData(ResultEnum.ERROR.getMsg());
            log.error("获取用户二维码异常 {}", e.getStackTrace());
        }
        return  responseResult;
    }


    @PostMapping(value ={"/edit"})
    @ResponseBody
    public ResponseResult edit(@RequestBody EditCodeDto editCodeDto, HttpServletRequest request){
        ResponseResult responseResult = new ResponseResult();
        try {
            ZfCode zfCode = new ZfCode();
            zfCode.setCodeId(editCodeDto.getCodeId());
            zfCode.setDayLimitAmount(editCodeDto.getDayLimitAmount());
            zfCode.setDayLimitTimes(editCodeDto.getDayLimitTimes());
            int r =  zfCodeService.update(zfCode);
            if(r > 0){
                responseResult.setCode(ResultEnum.SUCCESS.getCode());
                responseResult.setMsg("操作成功");
            }else {
                responseResult.setCode(ResultEnum.ERROR.getCode());
                responseResult.setMsg(ResultEnum.ERROR.getMsg());
            }
            log.info("获取用户二维码 {}", editCodeDto);
        }catch (BaseException e){
            responseResult.setCode(e.getCode());
            responseResult.setMsg(e.getMessage());
            //其他非法异常，重新上传;
        }catch (Exception e){
            responseResult.setCode(ResultEnum.ERROR.getCode());
            responseResult.setMsg(ResultEnum.ERROR.getMsg());
        }
        return responseResult;
    }


    @PostMapping(value ={"/open"})
    @ResponseBody
    public ResponseResult open(@RequestBody OpenCodeDto openCodeDto, HttpServletRequest request){
        ResponseResult responseResult = new ResponseResult();
        try {
            log.info("开启二维码 {}", openCodeDto);
            ZfCode zfCode = new ZfCode();
            zfCode.setCodeId(openCodeDto.getCodeId());
            zfCode.setIsOpen(openCodeDto.getIsOpen());
            int r =  zfCodeService.update(zfCode);
            if(r > 0){
                responseResult.setCode(ResultEnum.SUCCESS.getCode());
                responseResult.setMsg("操作成功");
            }else {
                responseResult.setCode(ResultEnum.ERROR.getCode());
                responseResult.setMsg(ResultEnum.ERROR.getMsg());
            }
        }catch (BaseException e){
            responseResult.setCode(e.getCode());
            responseResult.setMsg(e.getMessage());
            //其他非法异常，重新上传;
        }catch (Exception e){
            responseResult.setCode(ResultEnum.ERROR.getCode());
            responseResult.setMsg(ResultEnum.ERROR.getMsg());
        }
        return responseResult;
    }

    @PostMapping(value ={"/add"})
    @ResponseBody
    public ResponseResult add(AddCodeDto addCodeDto, HttpServletRequest request){
        ResponseResult responseResult = new ResponseResult();
        try {
            log.info("添加用户二维码 {}", addCodeDto);
            String token  =   request.getHeader("token");
            String account  = tokenUtil.parseToken(token).get("loginName");
            ZfAgent zfAgent = zfAgentService.queryByAcount(account);
            addCodeDto.setMinAmount(new BigDecimal("100"));
            addCodeDto.setMaxAmount(new BigDecimal("2000"));
            zfCodeService.addCode(addCodeDto, zfAgent);

            responseResult.setCode(ResultEnum.SUCCESS.getCode());
            responseResult.setMsg(ResultEnum.SUCCESS.getMsg());
        }catch (BaseException e){
            responseResult.setCode(e.getCode());
            responseResult.setMsg(e.getMessage());
            //其他非法异常，重新上传;
        }catch (Exception e){
            responseResult.setCode(ResultEnum.ERROR.getCode());
            responseResult.setMsg("操作异常");
            log.error("操作异常 ", e);
        }
        return responseResult;
    }

    @PostMapping(value ={"/upload"})
    @ResponseBody
    public ResponseResult upload(AddCodeDto addCodeDto, HttpServletRequest request){
        ResponseResult responseResult = new ResponseResult();
        try {
            log.info("用户上传二维码 {}", addCodeDto);
            String path =  fileUploadService.uploadFile(addCodeDto.getImage());
            responseResult.setCode(ResultEnum.SUCCESS.getCode());
            responseResult.setData(path);
            responseResult.setMsg(ResultEnum.SUCCESS.getMsg());
        }catch (BaseException e){
            responseResult.setCode(e.getCode());
            responseResult.setMsg(e.getMessage());
            //其他非法异常，重新上传;
        }catch (Exception e){
            responseResult.setCode(ResultEnum.ERROR.getCode());
            responseResult.setMsg("操作异常");
            log.error("操作异常 ", e);
        }
        return responseResult;
    }
}
