package com.code.payapi.controller;

import com.alibaba.fastjson.JSONObject;
import com.code.baseservice.base.enums.PaytypeEnum;
import com.code.baseservice.base.enums.ResultEnum;
import com.code.baseservice.base.exception.BaseException;
import com.code.baseservice.dto.ResponseResult;
import com.code.baseservice.dto.payapi.QueryParams;
import com.code.baseservice.dto.payapi.RechareParams;
import com.code.baseservice.entity.ZfCode;
import com.code.baseservice.entity.ZfRecharge;
import com.code.baseservice.service.ZfCodeService;
import com.code.baseservice.service.ZfRechargeService;
import com.code.baseservice.util.DeviceUtils;
import com.code.baseservice.util.GeneratorVnQrUtil;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
@RequestMapping("/recharge")
public class RechargeController {

    private String prefix = "pay" ;

    @Autowired
    ZfRechargeService zfRechargeService;

    @Autowired
    ZfCodeService zfCodeService;

    @ApiOperation("创建订单")
    @PostMapping("/create")
    @ResponseBody
    public String create(@RequestBody RechareParams rechareParams) {
        log.info("入款参数 订单号 {} 信息 {}", rechareParams.getMerchant_order_no(), rechareParams.toString());
        ResponseResult responseResult = new ResponseResult();
        try {
            if("test".equals(rechareParams.getName())){
                rechareParams.setName("");
            }
            JSONObject jsonObject = zfRechargeService.create(rechareParams);
            responseResult.setData(jsonObject);
        }catch (BaseException e){
            responseResult.setCode(e.getCode()).setMsg(e.getMessage());
        }catch (Exception e){
            log.error("系统异常", e);
            responseResult.setCode(ResultEnum.ERROR.getCode()).setMsg("系统异常");
        }
        log.info("订单号  {} 接口返回 {}", rechareParams.getMerchant_order_no(),responseResult.toJsonString());
        return responseResult.toJsonString();
    }

    @ApiOperation("创建订单")
    @PostMapping("/createCard")
    @ResponseBody
    public String createCard(@RequestBody RechareParams rechareParams) {
        log.info("入款参数 订单号 {} 信息 {}", rechareParams.getMerchant_order_no(), rechareParams.toString());
        ResponseResult responseResult = new ResponseResult();
        try {
            JSONObject jsonObject = zfRechargeService.createCard(rechareParams);
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
    public String detail(@PathVariable("orderno") String orderno, ModelMap modelMap, HttpServletRequest request) {
        ZfRecharge xRecharge = zfRechargeService.queryById(orderno);
        String page = PaytypeEnum.getPayView(xRecharge.getPayType());
        if(xRecharge.getPayType().equals(PaytypeEnum.CARD.getValue())){
            ZfCode zfCode = zfCodeService.queryById(xRecharge.getCodeId());
            List<String> infos = Arrays.asList(zfCode.getAccount().split("\\|"));
            modelMap.put("bank_card_name", zfCode.getName());
            modelMap.put("bank_card_num", infos.get(0));
            modelMap.put("bank_card_type", infos.get(1));
            modelMap.put("bank_address", infos.get(2));
            long now = new Date().getTime();
            long createTime = xRecharge.getCreateTime().getTime();
            long second =  15*60 -(now - createTime)/1000l ;
            modelMap.put("second", second);
            String code = GeneratorVnQrUtil.vietQrGenerate(infos.get(2),infos.get(0),"QRIBFTTA","12", xRecharge.getPayAmount().setScale(0).toString(), xRecharge.getRemark() );
            modelMap.put("code", code);
            if(infos.get(1).toUpperCase().contains("CAKE")){
                if(DeviceUtils.isMobileDevice(request)){
                    page = "vn_cake_card";
                }else {
                    page = "vn_cake_card_pc";
                }
            }
        }else {
            if(xRecharge.getCodeId() != 0){
                ZfCode zfCode = zfCodeService.queryById(xRecharge.getCodeId());
                modelMap.put("code", zfCode);
            }
        }
        modelMap.put("timeout", 10);
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        String formattedNumber = decimalFormat.format(xRecharge.getPayAmount().setScale(0));
        modelMap.put("xrecharge", xRecharge);
        modelMap.put("amount", formattedNumber);
        return  prefix + "/" + page;
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

}
