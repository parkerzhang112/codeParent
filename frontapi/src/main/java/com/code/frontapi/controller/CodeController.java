package com.code.frontapi.controller;

import com.code.baseservice.base.exception.BaseException;
import com.code.baseservice.dto.FrontResponseResult;
import com.code.baseservice.dto.ResponseResult;
import com.code.baseservice.dto.frontapi.code.AddCodeDto;
import com.code.baseservice.dto.frontapi.code.QueryCodeDto;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Api(tags = "支付资料", description = "流水api")
@Slf4j
@RestController
@RequestMapping(value = "/user/code")
public class CodeController {

    @PostMapping(value ={"/list"})
    @ResponseBody
    public FrontResponseResult<QueryCodeDto> list(@RequestBody QueryCodeDto queryCodeDto, HttpServletRequest request){
        try {
            return new FrontResponseResult<>("操作成功", 200, queryCodeDto);
        }catch (BaseException e){
            return new FrontResponseResult<>("操作成功", 200, queryCodeDto);
            //其他非法异常，重新上传;
        }catch (Exception e){
            log.error("获取用户二维码异常 ", e);
            return new FrontResponseResult<>("操作成功", 200, queryCodeDto);
        }
    }


    @PostMapping(value ={"/detail/{code_id}"})
    @ResponseBody
    public FrontResponseResult<QueryCodeDto>  detail(@PathVariable("agentId") Integer agentId, HttpServletRequest request){
        ResponseResult responseResult = new ResponseResult();
        try {
            return new FrontResponseResult<>("操作成功", 200, null);

        }catch (BaseException e){
            return new FrontResponseResult<>("操作成功", 200, null);

            //其他非法异常，重新上传;
        }catch (Exception e){
            log.error("获取用户二维码异常 {}", e.getStackTrace());
            return new FrontResponseResult<>("操作成功", 200, null);

        }
    }


    @PostMapping(value ={"/edit"})
    @ResponseBody
    public String edit(@RequestBody AddCodeDto addCodeDto, HttpServletRequest request){
        ResponseResult responseResult = new ResponseResult();
        try {
            log.info("获取用户二维码 {}", addCodeDto);
        }catch (BaseException e){
            //其他非法异常，重新上传;
        }catch (Exception e){
            log.error("获取用户二维码异常 {}", e.getStackTrace());
        }
        responseResult.setMsg("操作成功");
        return responseResult.toJsonString();
    }


    @PostMapping(value ={"/add"})
    @ResponseBody
    public String add(@RequestBody AddCodeDto addCodeDto, HttpServletRequest request){
        ResponseResult responseResult = new ResponseResult();
        try {
            log.info("获取用户二维码 {}", addCodeDto);
        }catch (BaseException e){
            //其他非法异常，重新上传;
        }catch (Exception e){
            log.error("获取用户二维码异常 {}", e.getStackTrace());
        }
        responseResult.setMsg("操作成功");
        return responseResult.toJsonString();
    }
}
