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
        preBalance = zfAgent.getBalance();
        balance = preBalance.add(fee);
        transType = TransTypeEnum.RRCHARGE.getValue();
        merchantOrderNo = zfRecharge.getMerchantOrderNo();
        amount = fee;
        acceptAmount = zfRecharge.getPayAmount();
        if(BigDecimal.ZERO.compareTo(fee) == 0){
            remark = "押金额度操作";
        }else {
            remark = "跑分手续费";
        }

    }

    public ZfAgentTrans(){

    }




}

