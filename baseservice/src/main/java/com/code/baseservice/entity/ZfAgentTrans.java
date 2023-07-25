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


    public ZfAgentTrans(ZfRecharge zfRecharge, ZfAgent zfAgent, BigDecimal fee) {
        agentId = zfAgent.getAgentId();
        transType = TransTypeEnum.RRCHARGE.getValue();
        merchantOrderNo = zfRecharge.getMerchantOrderNo();
        amount = zfRecharge.getPayAmount();
        balance = zfAgent.getBalance();
        if (zfRecharge.getOrderStatus().equals(1)) {
            acceptAmount = zfAgent.getAcceptAmount();
            transType = TransTypeEnum.TRANSFER.getValue();
            remark = "订单充值减分";
        } else if (zfRecharge.getOrderStatus().equals(3)) {
            acceptAmount = zfAgent.getAcceptAmount();
            transType = TransTypeEnum.RRCHARGE.getValue();
            remark = "订单失败上分";
        } else if(zfRecharge.getOrderStatus() == 2 || zfRecharge.getOrderStatus()== 4) {
            //成功写入积分，前面有计算。重新扣回
            acceptAmount = zfAgent.getAcceptAmount();
            preBalance = zfAgent.getBalance();
            amount = fee;
            balance = preBalance.add(fee);
            transType = TransTypeEnum.RRCHARGE.getValue();
            remark = "订单成功返佣";
        }else if (zfRecharge.getOrderStatus() == 5){
            acceptAmount = zfAgent.getAcceptAmount();
            transType = TransTypeEnum.RRCHARGE.getValue();
            remark = "订单超时补分";
        }else {
            remark = "系统操作";
        }
    }
    public  void  buildFailOrderRollbackBySuccess(ZfRecharge zfRecharge, ZfAgent zfAgent){
        agentId = zfAgent.getAgentId();
        transType = TransTypeEnum.RRCHARGE.getValue();
        merchantOrderNo = zfRecharge.getMerchantOrderNo();
        amount = zfRecharge.getPayAmount();
        balance = zfAgent.getBalance();
        acceptAmount = zfAgent.getAcceptAmount();
        transType = TransTypeEnum.TRANSFER.getValue();
        remark = "超时订单回滚扣分";
    }
    public ZfAgentTrans(){

    }




}

