package com.code.payapi.controller;

import com.alibaba.fastjson.JSONObject;
import com.code.baseservice.base.enums.ResultEnum;
import com.code.baseservice.base.exception.BaseException;
import com.code.baseservice.dto.ResponseResult;
import com.code.baseservice.dto.payapi.MerchantParams;
import com.code.baseservice.service.ZfMerchantService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = "商户", description = "商户api")
@RestController
@Slf4j
@RequestMapping("/merchant")
public class MerchantContoller {

    @Autowired
    ZfMerchantService zfMerchantService;

    @PostMapping("/balacne")
    @ResponseBody
    public String view(@RequestBody MerchantParams merchantParams){
        ResponseResult responseResult = new ResponseResult();
        try {
            JSONObject jsonObject = zfMerchantService.query(merchantParams);
            responseResult.setData(jsonObject);
        }catch (BaseException e){
            responseResult.setCode(e.getCode()).setMsg(e.getMessage());
        }catch (Exception e){
            responseResult.setCode(ResultEnum.ERROR.getCode()).setMsg("系统异常");
        }
        return responseResult.toJsonString();
    }
}
