package com.code.baseservice.entity;

import com.code.baseservice.base.enums.TransTypeEnum;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * (ZfAgentTrans)实体类
 *
 * @author makejava
 * @since 2023-03-19 23:06:24
 */
@Data
public class ZfAgentTrans implements Serializable {
    private static final long serialVersionUID = 540178769990295068L;
    
    private Integer agentId;
    
    private BigDecimal preBalance;
    
    private BigDecimal balance;
    
    private BigDecimal amount;
    
    private String orderNo;
    
    private Date createTime;
    
    private Date updateTime;
    
    private String remark;

    private String merchantOrderNo;

    private BigDecimal acceptAmount;

    private Integer transType;

    private BigDecimal preAcceptAmount;

    private Integer operType;



    public ZfAgentTrans(ZfRecharge zfRecharge, ZfAgent zfAgent, BigDecimal fee) {
        agentId = zfAgent.getAgentId();
        transType = TransTypeEnum.RRCHARGE.getValue();
        merchantOrderNo = zfRecharge.getMerchantOrderNo();
        amount = zfRecharge.getPayAmount();
        balance = zfAgent.getBalance();
        preBalance = zfAgent.getBalance();
        if (zfRecharge.getOrderStatus().equals(1)) {
            acceptAmount = zfAgent.getAcceptAmount();
            preAcceptAmount = acceptAmount.add(amount);
            transType = TransTypeEnum.TRANSFER.getValue();
            remark = "订单充值减分";
        } else if (zfRecharge.getOrderStatus().equals(3)) {
            acceptAmount = zfAgent.getAcceptAmount();
            preAcceptAmount = acceptAmount.subtract(amount);
            transType = TransTypeEnum.RRCHARGE.getValue();
            remark = "订单失败上分";
        } else if(zfRecharge.getOrderStatus() == 2 || zfRecharge.getOrderStatus()== 4) {
            //成功写入积分，前面有计算。重新扣回
            acceptAmount = zfAgent.getAcceptAmount();
            preAcceptAmount = zfAgent.getAcceptAmount();

            preBalance = zfAgent.getBalance().subtract(fee);
            amount = fee;
            transType = TransTypeEnum.RRCHARGE.getValue();
            remark = "订单成功返佣";
        }else if (zfRecharge.getOrderStatus() == 5){
            acceptAmount = zfAgent.getAcceptAmount();
            preAcceptAmount = zfAgent.getAcceptAmount().subtract(amount);
            transType = TransTypeEnum.RRCHARGE.getValue();
            remark = "订单超时补分";
        }else {
            remark = "系统操作";
        }
    }

    public ZfAgentTrans(ZfWithdraw zfWithdraw, ZfAgent zfAgent, BigDecimal fee) {
        agentId = zfAgent.getAgentId();
        transType = TransTypeEnum.RRCHARGE.getValue();
        merchantOrderNo = zfWithdraw.getMerchantOrderNo();
        if(!zfWithdraw.getAgentId().equals(zfAgent.getAgentId())){
            amount = fee;
            remark = "返佣:"+fee ;
        }else {
            amount = zfWithdraw.getPayAmount().add(fee);
            remark = String.format("订单代付:%.2f-返佣:%.2f", zfWithdraw.getPayAmount(), fee);
        }
        balance = zfAgent.getBalance();
        preBalance = zfAgent.getBalance();
        acceptAmount = zfAgent.getAcceptAmount();
        preAcceptAmount = acceptAmount.subtract(amount);
        transType = TransTypeEnum.RRCHARGE.getValue();
    }


    public ZfAgentTrans(ZfAgentRechargeOrder zfAgentRechargeOrder, ZfAgent zfAgent) {
        agentId = zfAgentRechargeOrder.getAgentId();
        transType = TransTypeEnum.RRCHARGE.getValue();
        merchantOrderNo = zfAgentRechargeOrder.getOrderNo();
        amount = zfAgentRechargeOrder.getPaidAmount();
        balance = zfAgent.getBalance();
        preBalance = zfAgent.getBalance();
        acceptAmount = zfAgent.getAcceptAmount();
        preAcceptAmount = acceptAmount.subtract(amount);
        remark = "代理充值上分";
    }


    public  void  buildFailOrderRollbackBySuccess(ZfRecharge zfRecharge, ZfAgent zfAgent){
        agentId = zfAgent.getAgentId();
        transType = TransTypeEnum.RRCHARGE.getValue();
        merchantOrderNo = zfRecharge.getMerchantOrderNo();
        amount = zfRecharge.getPayAmount();
        balance = zfAgent.getBalance();
        acceptAmount = zfAgent.getAcceptAmount();
        preAcceptAmount  = zfAgent.getAcceptAmount().add(amount);
        transType = TransTypeEnum.TRANSFER.getValue();
        remark = "超时订单回滚扣分";
    }
    public ZfAgentTrans(){

    }




}

