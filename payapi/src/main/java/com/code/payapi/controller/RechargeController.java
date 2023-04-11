package com.code.payapi.controller;

import com.alibaba.fastjson.JSONObject;

import com.code.baseservice.base.enums.ResultEnum;
import com.code.baseservice.base.exception.BaseException;
import com.code.baseservice.dto.payapi.QueryParams;
import com.code.baseservice.dto.payapi.RechareParams;
import com.code.baseservice.dto.ResponseResult;
import com.code.baseservice.service.ZfRechargeService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@Slf4j
@RequestMapping("/recharge")
public class RechargeController {

    private String prefix = "pay" ;

    @Autowired
    ZfRechargeService zfRechargeService;

    @ApiOperation("创建订单")
    @PostMapping("/create")
    @ResponseBody
    public String create(@RequestBody RechareParams rechareParams) {
        log.info("入款参数 订单号 {} 信息 {}", rechareParams.getMerchant_order_no(), rechareParams.toString());
        ResponseResult responseResult = new ResponseResult();
        try {
            JSONObject jsonObject = zfRechargeService.create(rechareParams);
            responseResult.setData(jsonObject);
        }catch (BaseException e){
            responseResult.setCode(e.getCode()).setMsg(e.getMessage());
        }catch (Exception e){
            log.error("系统异常", e);
            responseResult.setCode(ResultEnum.ERROR.getCode()).setMsg("系统异常");
        }
        return responseResult.toJsonString();
    }

    @ApiOperation("查询订单")
    @PostMapping("/view")
    @ResponseBody
    public String view(@RequestBody QueryParams queryParams){
        ResponseResult responseResult = new ResponseResult();
        try {
            JSONObject jsonObject = zfRechargeService.query(queryParams);
            responseResult.setData(jsonObject);
        }catch (BaseException e){
            responseResult.setCode(e.getCode()).setMsg(e.getMessage());
        }catch (Exception e){
            responseResult.setCode(ResultEnum.ERROR.getCode()).setMsg("系统异常");
        }
        return responseResult.toJsonString();
    }

}
