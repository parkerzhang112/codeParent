package com.code.payapi.controller;

import com.alibaba.fastjson.JSONObject;
import com.code.baseservice.base.enums.PaytypeEnum;
import com.code.baseservice.base.enums.ResultEnum;
import com.code.baseservice.base.exception.BaseException;
import com.code.baseservice.dto.ResponseResult;
import com.code.baseservice.dto.payapi.QueryParams;
import com.code.baseservice.dto.payapi.RechareParams;
import com.code.baseservice.entity.ZfRecharge;
import com.code.baseservice.service.CommonService;
import com.code.baseservice.service.ZfRechargeService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@Slf4j
@RequestMapping("/recharge")
public class RechargeController {

    private String prefix = "pay" ;

    @Autowired
    ZfRechargeService zfRechargeService;

    @Autowired
    CommonService commonService;

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

    @ApiOperation("创建订单")
    @PostMapping("/create_a")
    @ResponseBody
    public String createA(@RequestBody RechareParams rechareParams) {
        log.info("入款参数 订单号 {} 信息 {}", rechareParams.getMerchant_order_no(), rechareParams.toString());
        ResponseResult responseResult = new ResponseResult();
        try {
            JSONObject jsonObject = zfRechargeService.createA(rechareParams);
            responseResult.setData(jsonObject);
        }catch (BaseException e){
            responseResult.setCode(e.getCode()).setMsg(e.getMessage());
        }catch (Exception e){
            log.error("系统异常", e);
            responseResult.setCode(ResultEnum.ERROR.getCode()).setMsg("系统异常");
        }
        return responseResult.toJsonString();
    }

    @GetMapping("/order/{orderno}")
    public String detail(@PathVariable("orderno") String orderno, ModelMap modelMap) {
        ZfRecharge xRecharge = zfRechargeService.queryById(orderno);
        modelMap.put("timeout", 10);
        modelMap.put("xrecharge", xRecharge);

        if(xRecharge.getPayType()== PaytypeEnum.CODE.getValue()){
            return prefix+"/index";
        }else {
            return prefix+"/trans";
        }
    }


    @ApiOperation("查询订单")
    @CrossOrigin(origins = "*", maxAge = 3600)
    @GetMapping("/order/getOrder/{orderno}")
    @ResponseBody
    public String getOrderStatus(@PathVariable("orderno") String orderno){
        ResponseResult responseResult = new ResponseResult();
        try {
           JSONObject jsonObject =  zfRechargeService.getOrderStatus(orderno);
            responseResult.setData(jsonObject);
        }catch (BaseException e){
            responseResult.setCode(e.getCode()).setMsg(e.getMessage());
        }catch (Exception e){
            responseResult.setCode(ResultEnum.ERROR.getCode()).setMsg("系统异常");
        }
        return responseResult.toJsonString();
    }

    @ApiOperation("查询订单")
    @CrossOrigin(origins = "*", maxAge = 3600)
    @PostMapping("/order/updateName")
    @ResponseBody
    public String updateName(@RequestBody Map<String, Object> map){
        ResponseResult responseResult = new ResponseResult();
        try {
              zfRechargeService.postName(map);
              responseResult.setCode(ResultEnum.SUCCESS.getCode());
        }catch (BaseException e){
            log.error("系统异常 {}", e);
            responseResult.setCode(e.getCode()).setMsg(e.getMessage());
        }catch (Exception e){
            log.error("系统异常 {}", e);
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
            log.error("系统异常 {}", e);
            responseResult.setCode(ResultEnum.ERROR.getCode()).setMsg("系统异常");
        }
        return responseResult.toJsonString();
    }

    /**
     * 下游商户进行订单通知
     * @param queryParams
     * @return
     */
    @ApiOperation("查询订单")
    @PostMapping("/notify")
    @ResponseBody
    public String notify(@RequestBody QueryParams queryParams){
        ResponseResult responseResult = new ResponseResult();
        try {
            JSONObject jsonObject = commonService.notify(queryParams);
            responseResult.setData(jsonObject);
        }catch (BaseException e){
            responseResult.setCode(e.getCode()).setMsg(e.getMessage());
        }catch (Exception e){
            log.error("系统异常 {}", e);
            responseResult.setCode(ResultEnum.ERROR.getCode()).setMsg("系统异常");
        }
        return responseResult.toJsonString();
    }

}
