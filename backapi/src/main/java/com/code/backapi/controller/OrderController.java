package com.code.backapi.controller;

import com.alibaba.fastjson.JSONObject;
import com.code.baseservice.base.enums.ResultEnum;
import com.code.baseservice.base.exception.BaseException;
import com.code.baseservice.dto.ResponseResult;
import com.code.baseservice.dto.backapi.OperaOrderParams;
import com.code.baseservice.entity.ZfRecharge;
import com.code.baseservice.entity.ZfWithdraw;
import com.code.baseservice.service.ZfRechargeService;
import com.code.baseservice.service.ZfWithdrawService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@Api(tags = "订单操作", description = "订单操作 API")
@RequestMapping("/order")
@Slf4j
public class OrderController {

    @Autowired
    ZfRechargeService zfRechargeService;

    @Autowired
    ZfWithdrawService zfWithdrawService;


    /**
     * 确认订单
     *
     * @param operaOrderParams
     * @return
     */
    @PostMapping("/confirm")
    @ResponseBody
    public String confirmOrder(@RequestBody OperaOrderParams operaOrderParams) {
        ResponseResult responseResult = new ResponseResult();
        try {
            if (new Integer(1).equals(operaOrderParams.getOrderType())) {
                zfRechargeService.confirmOrder(operaOrderParams);
            } else {
                zfWithdrawService.confirmOrder(operaOrderParams);
            }
            return responseResult.toJsonString();
        } catch (BaseException e) {
            responseResult.setCode(e.getCode()).setMsg(e.getMessage());
        } catch (Exception e) {
            log.error("系统异常", e);
            responseResult.setCode(ResultEnum.ERROR.getCode()).setMsg("系统异常");
        }
        return responseResult.toJsonString();
    }

    @RequestMapping("/cancel")
    @ResponseBody
    public String cancelOrder(@RequestBody OperaOrderParams operaOrderParams) {
        ResponseResult responseResult = new ResponseResult();
        try {
            if (new Integer(1).equals(operaOrderParams.getOrderType())) {
                zfRechargeService.cancelOrder(operaOrderParams);
            } else {
                zfWithdrawService.cancelOrder(operaOrderParams);
            }
            return responseResult.toJsonString();
        } catch (BaseException e) {
            responseResult.setCode(e.getCode()).setMsg(e.getMessage());
        } catch (Exception e) {
            log.error("系统异常", e);
            responseResult.setCode(ResultEnum.ERROR.getCode()).setMsg("系统异常");
        }
        return responseResult.toJsonString();
    }

    @RequestMapping("notify")
    @ResponseBody
    public String notifyOrder(@RequestBody OperaOrderParams operaOrderParams) {
        ResponseResult responseResult = new ResponseResult();
        try {
            if (new Integer(1).equals(operaOrderParams.getOrderType())) {
                ZfWithdraw zfWithdraw = zfWithdrawService.queryById(operaOrderParams.getOrderNo());
                zfWithdrawService.notify(zfWithdraw);
            } else {
                ZfRecharge zfRecharge = zfRechargeService.queryById(operaOrderParams.getOrderNo());
                zfRechargeService.notify(zfRecharge);
            }
            return responseResult.toJsonString();
        } catch (BaseException e) {
            responseResult.setCode(e.getCode()).setMsg(e.getMessage());
        } catch (Exception e) {
            log.error("系统异常", e);
            responseResult.setCode(ResultEnum.ERROR.getCode()).setMsg("系统异常");
        }
        return responseResult.toJsonString();
    }

    //派单
    @RequestMapping("lock")
    @ResponseBody
    public String lock(@RequestBody OperaOrderParams operaOrderParams) {
        ResponseResult responseResult = new ResponseResult();
        try {
            zfWithdrawService.lock(operaOrderParams);

        } catch (BaseException e) {
            responseResult.setCode(e.getCode()).setMsg(e.getMessage());
        } catch (Exception e) {
            log.error("手动补分 系统异常", e);
            responseResult.setCode(ResultEnum.ERROR.getCode()).setMsg("系统异常");
        }
        return responseResult.toJsonString();
    }

}
