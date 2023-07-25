package com.code.backapi.controller;

import com.alibaba.fastjson.JSONObject;
import com.code.baseservice.base.enums.ResultEnum;
import com.code.baseservice.base.enums.TransTypeEnum;
import com.code.baseservice.base.exception.BaseException;
import com.code.baseservice.dto.ResponseResult;
import com.code.baseservice.dto.backapi.OperaBalanceParams;
import com.code.baseservice.dto.backapi.OperaOrderParams;
import com.code.baseservice.entity.ZfRecharge;
import com.code.baseservice.entity.ZfTransRecord;
import com.code.baseservice.entity.ZfWithdraw;
import com.code.baseservice.service.ZfRechargeService;
import com.code.baseservice.service.ZfTransRecordService;
import com.code.baseservice.service.ZfWithdrawService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

    @Autowired
    ZfTransRecordService zfTransRecordService;


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

    /**
     * 区别于手动取消
     * @param operaOrderParams
     * @return
     */
    @RequestMapping("/autocancel")
    @ResponseBody
    public String autocancel(@RequestBody OperaOrderParams operaOrderParams) {
        ResponseResult responseResult = new ResponseResult();
        try {
            zfRechargeService.autocancel(operaOrderParams);
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

    @RequestMapping("/notify")
    @ResponseBody
    public String notifyOrder(@RequestBody OperaOrderParams operaOrderParams) {
        ResponseResult responseResult = new ResponseResult();
        try {
            if (TransTypeEnum.TRANSFER.getValue().equals(operaOrderParams.getOrderType())) {
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

    @RequestMapping("/match")
    @ResponseBody
    public String match(@RequestBody OperaOrderParams operaOrderParams) {
        ResponseResult responseResult = new ResponseResult();
        try {
            ZfTransRecord zfTransRecord = zfTransRecordService.queryId(operaOrderParams.getTrandId());
            if( null == zfTransRecord || zfTransRecord.getStatus() != 0){
                log.info("流水状态异常 {}" , zfTransRecord);
                new BaseException(ResultEnum.TRANS_STATUS_EXCEPTION);
            }
            ZfRecharge xRecharge =  zfRechargeService.queryByMerchantOrderNo(operaOrderParams.getMerchanOrderNo());
            if(null == xRecharge){
                log.info("订单已经成功 订单号{}" ,xRecharge.getMerchantOrderNo());
                new BaseException(ResultEnum.OPRDER_REPEAT);
            }
            zfTransRecord.setStatus(2);//手工匹配
            zfTransRecord.setMerchantOrderNo(xRecharge.getMerchantOrderNo());
            //存款订单  状态为失败 则直接操作商户余额
            if(xRecharge.getOrderStatus().equals(1)) {
                xRecharge.setOrderStatus(2);
                xRecharge.setPaidAmount(zfTransRecord.getAmount());
                zfTransRecordService.update(zfTransRecord);
                zfRechargeService.paidOrder(xRecharge);
                return responseResult.toJsonString();
            }

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
