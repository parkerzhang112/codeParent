package com.code.payapi.controller;

import com.alibaba.fastjson.JSONObject;
import com.code.baseservice.base.constant.RedisConstant;
import com.code.baseservice.base.enums.PaytypeEnum;
import com.code.baseservice.base.enums.ResultEnum;
import com.code.baseservice.base.exception.BaseException;
import com.code.baseservice.dto.ResponseResult;
import com.code.baseservice.dto.payapi.QueryParams;
import com.code.baseservice.dto.payapi.RechareParams;
import com.code.baseservice.entity.ZfChannel;
import com.code.baseservice.entity.ZfCode;
import com.code.baseservice.entity.ZfRecharge;
import com.code.baseservice.service.*;
import com.code.baseservice.thirdService.WxYsXCXServiceImpl;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Controller
@Slf4j
@RequestMapping("/recharge")
public class RechargeController {

    private String prefix = "pay";

    @Autowired
    ZfRechargeService zfRechargeService;

    @Autowired
    CommonService commonService;

    @Autowired
    ZfChannelService zfChannelService;

    @Autowired
    RedissonClient redissonClient;

    @Autowired
    WxYsXCXServiceImpl wxYsXCXService;

    @Autowired
    RedisUtilService redisUtilService;

    @Autowired
    ZfCodeService zfCodeService;

    @ApiOperation("创建订单")
    @PostMapping("/create")
    @ResponseBody
    public String createA(@RequestBody RechareParams rechareParams) {
        log.info("入款参数 订单号 {} 信息 {}", rechareParams.getMerchant_order_no(), rechareParams.toString());
        ResponseResult responseResult = new ResponseResult();
        try {
            JSONObject jsonObject = zfRechargeService.create(rechareParams);
            responseResult.setData(jsonObject);
        } catch (BaseException e) {
            responseResult.setCode(e.getCode()).setMsg(e.getMessage());
        } catch (Exception e) {
            log.error("系统异常", e);
            responseResult.setCode(ResultEnum.ERROR.getCode()).setMsg("系统异常");
        }
        log.info("订单：{}，下单返回结果:{}", rechareParams.getMerchant_order_no(), responseResult);
        return responseResult.toJsonString();
    }

    @GetMapping("/order/{orderno}")
    public String detail(@PathVariable("orderno") String orderno, ModelMap modelMap) {
        ZfRecharge xRecharge = zfRechargeService.queryById(orderno);
        modelMap.put("timeout", 10);
        modelMap.put("xrecharge", xRecharge);
        ZfChannel zfChannel = zfChannelService.queryById(xRecharge.getChannelId());
        if(zfChannel.getChannelCode().equals("XiaoChenXu"))
        {
            return prefix + "/auto";
        }
        if (xRecharge.getPayType() == PaytypeEnum.CODE.getValue()
            || xRecharge.getPayType() == 8
        ) {
            return prefix + "/index";
//        } else if(xRecharge.getPayType() == 8) {
        }else {
            return prefix + "/trans";
        }
    }


    @ApiOperation("查询订单")
    @CrossOrigin(origins = "*", maxAge = 3600)
    @GetMapping("/order/getOrder/{orderno}")
    @ResponseBody
    public String getOrderStatus(@PathVariable("orderno") String orderno) {
        ResponseResult responseResult = new ResponseResult();
        try {
            JSONObject jsonObject = zfRechargeService.getOrderStatus(orderno);
            responseResult.setData(jsonObject);
        } catch (BaseException e) {
            responseResult.setCode(e.getCode()).setMsg(e.getMessage());
        } catch (Exception e) {
            log.error("系统异常 {} ", orderno , e);
            responseResult.setCode(ResultEnum.ERROR.getCode()).setMsg("系统异常");
        }
        return responseResult.toJsonString();
    }

    @ApiOperation("查询订单")
    @CrossOrigin(origins = "*", maxAge = 3600)
    @PostMapping("/order/updateName")
    @ResponseBody
    public String updateName(@RequestBody Map<String, Object> map) {
        ResponseResult responseResult = new ResponseResult();
        try {
            zfRechargeService.postName(map);
            responseResult.setCode(ResultEnum.SUCCESS.getCode());
        } catch (BaseException e) {
            log.error("系统异常 {}", e);
            responseResult.setCode(e.getCode()).setMsg(e.getMessage());
        } catch (Exception e) {
            log.error("系统异常 {}", e);
            responseResult.setCode(ResultEnum.ERROR.getCode()).setMsg("系统异常");
        }
        return responseResult.toJsonString();
    }

    @ApiOperation("查询订单")
    @PostMapping("/view")
    @ResponseBody
    public String view(@RequestBody QueryParams queryParams) {
        ResponseResult responseResult = new ResponseResult();
        try {
            JSONObject jsonObject = zfRechargeService.query(queryParams);
            responseResult.setData(jsonObject);
        } catch (BaseException e) {
            responseResult.setCode(e.getCode()).setMsg(e.getMessage());
        } catch (Exception e) {
            log.error("系统异常 {}", e);
            responseResult.setCode(ResultEnum.ERROR.getCode()).setMsg("系统异常");
        }
        return responseResult.toJsonString();
    }

    @PostMapping("/getPrePayId")
    @ResponseBody
    public String getPrePayId(@RequestBody QueryParams queryParams) {
        log.info("请求参数 {}", queryParams.toString());
        ResponseResult responseResult = new ResponseResult();
        try {
            ZfRecharge zfRecharge = zfRechargeService.queryById(queryParams.getOrder_no());
            ZfChannel zfChannel = zfChannelService.queryById(zfRecharge.getChannelId());
            //填充openid进支付名称
            zfRecharge.setPayName(queryParams.getOpenId());
            JSONObject openId = wxYsXCXService.getOpenId(zfChannel, zfRecharge);
            zfRecharge.setPayName(openId.getString("openid"));
            ZfCode zfCode = zfCodeService.queryById(zfRecharge.getCodeId());
            JSONObject jsonObject =   wxYsXCXService.createPrePayId(zfChannel, zfRecharge, zfCode);
            redisUtilService.set(RedisConstant.LIMIT + zfRecharge.getCodeId(), 1, zfCode.getLimitSends());
            log.info("支付参数加密返回 {}", jsonObject.toJSONString());
            responseResult.setData(jsonObject);
        } catch (BaseException e) {
            responseResult.setCode(e.getCode()).setMsg(e.getMessage());
        } catch (Exception e) {
            log.error("系统异常 {}", e);
            responseResult.setCode(ResultEnum.ERROR.getCode()).setMsg("系统异常");
        }
        return responseResult.toJsonString();
    }

//    /**
//     * 下游商户进行订单通知
//     *
//     * @param queryParams
//     * @return
//     */
//    @ApiOperation("查询订单")
//    @PostMapping("/notify")
//    @ResponseBody
//    public String notify(@RequestBody QueryParams queryParams) {
//        ResponseResult responseResult = new ResponseResult();
//        try {
//            JSONObject jsonObject = commonService.notify(queryParams);
//            responseResult.setData(jsonObject);
//        } catch (BaseException e) {
//            responseResult.setCode(e.getCode()).setMsg(e.getMessage());
//        } catch (Exception e) {
//            log.error("系统异常 {}", e);
//            responseResult.setCode(ResultEnum.ERROR.getCode()).setMsg("系统异常");
//        }
//        return responseResult.toJsonString();
//    }

    /**
     * 下游商户进行订单通知
     *
     * @param queryParams
     * @return
     */
    @ApiOperation("查询订单")
    @PostMapping("/notify/{orderNo}")
    @ResponseBody
    public String notify(@PathVariable("orderNo") String orderNo, @RequestParam  Map<String, Object> map) {
        ResponseResult responseResult = new ResponseResult();

        try {
            ZfRecharge zfRecharge = zfRechargeService.queryById(orderNo);
            if(zfRecharge != null){
                ZfChannel zfChannel = zfChannelService.queryById(zfRecharge.getChannelId());
                String jsonObject = commonService.notify(zfRecharge,zfChannel, map);
                return jsonObject;
            }

        } catch (BaseException e) {
            responseResult.setCode(e.getCode()).setMsg(e.getMessage());
        } catch (Exception e) {
            log.error("系统异常 {}", e);
            responseResult.setCode(ResultEnum.ERROR.getCode()).setMsg("系统异常");
        }
        return responseResult.toJsonString();
    }

    /**
     * 下游商户进行订单通知
     *
     * @param queryParams
     * @return
     */
    @ApiOperation("查询订单")
    @PostMapping("/json_notify/{orderNo}")
    @ResponseBody
    public String jsonNotify(@PathVariable("orderNo") String orderNo, @RequestBody  Map<String, Object> map) {
        log.info("收到通知 {}", map);
        ResponseResult responseResult = new ResponseResult();
        RLock rLock = redissonClient.getLock("recharge:order" + orderNo);
        try {
           if( rLock.tryLock(2, TimeUnit.SECONDS)){
               ZfRecharge zfRecharge = zfRechargeService.queryById(orderNo);
               if(zfRecharge != null){
                   if (zfRecharge.getOrderStatus() != 1 && zfRecharge.getOrderStatus() != 5) {
                       log.info("订单已处理 订单号 {}", zfRecharge.getMerchantOrderNo());
                       throw new BaseException(ResultEnum.ERROR);
                   }
                   ZfChannel zfChannel = zfChannelService.queryById(zfRecharge.getChannelId());
                   String jsonObject = commonService.notify(zfRecharge,zfChannel, map);
                   return jsonObject;
               }
           }else {
               log.error("单号 {} 获取锁失败", orderNo);
           }

        } catch (BaseException e) {
            responseResult.setCode(e.getCode()).setMsg(e.getMessage());
        } catch (Exception e) {
            log.error("系统异常 {}", e);
            responseResult.setCode(ResultEnum.ERROR.getCode()).setMsg("系统异常");
        }finally {
            if(rLock.isLocked() && rLock.isHeldByCurrentThread()){
                rLock.unlock();
            }
        }
        return responseResult.toJsonString();
    }
}
