package com.code.frontapi.controller;

import com.code.baseservice.base.exception.BaseException;
import com.code.baseservice.dto.ResponseResult;
import com.code.baseservice.dto.frontapi.code.QueryCodeDto;
import com.code.baseservice.dto.frontapi.order.ConfirmOrderDto;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Api(tags = "订单操作", description = "流水api")
@Slf4j
@RestController
@RequestMapping(value = "/user/code")
public class OrderController {

    @PostMapping(value ={"/list11"})
    @ResponseBody
    public String list(@RequestBody QueryCodeDto queryCodeDto, HttpServletRequest request){
        ResponseResult responseResult = new ResponseResult();
        try {
            log.info("获取用户二维码 {}", queryCodeDto);
        }catch (BaseException e){
            //其他非法异常，重新上传;
        }catch (Exception e){
            log.error("获取用户二维码异常 ", e);
        }
        responseResult.setMsg("操作成功");
        return responseResult.toJsonString();
    }

    @PostMapping(value ={"/confirm"})
    @ResponseBody
    public String confirm(@RequestBody ConfirmOrderDto confirmOrderDto, HttpServletRequest request){
        ResponseResult responseResult = new ResponseResult();
        try {
            log.info("确认订单 {}", confirmOrderDto);
        }catch (BaseException e){
            //其他非法异常，重新上传;
        }catch (Exception e){
            log.error("确认订单 ", e);
        }
        responseResult.setMsg("操作成功");
        return responseResult.toJsonString();
    }
}
