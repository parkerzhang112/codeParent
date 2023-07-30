package com.code.baseservice.entity;

import com.code.baseservice.util.DateUtil;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * (ZfAgentRecord)实体类
 *
 * @author makejava
 * @since 2023-03-19 23:06:11
 */
@Data
public class ZfAgentRecord implements Serializable {
    private static final long serialVersionUID = 675114679783180957L;
    /**
     * 代理id
     */
    private Integer agentId;
    /**
     * 记录日期
     */
    private String recordDate;
    /**
     * 充值笔数
     */
    private Integer rechargeTimes;
    /**
     * 充值额度
     */
    private BigDecimal rechargeAmount;
    /**
     * 提款笔数
     */
    private Integer withdrawTimes;
    /**
     * 提款额度
     */
    private BigDecimal withdrawAmount;
    /**
     * 收益
     */
    private BigDecimal income;

    public ZfAgentRecord(ZfAgent zfAgent, BigDecimal fee, BigDecimal paidAmount) {
        if(paidAmount.compareTo(BigDecimal.ZERO) > 0){
            rechargeTimes =1;
            rechargeAmount = paidAmount;
            income  = fee;
        }else {
            withdrawAmount = paidAmount;
            withdrawTimes  = 1;
            income = BigDecimal.ZERO;
        }
        agentId = zfAgent.getAgentId();
        recordDate  = DateUtil.format(new Date(), DateUtil.YYYY_MM_DD);
    }

    public  ZfAgentRecord(){

    }


    public void buildAgentRecordBySunAgentSuccessAndParent(ZfAgent zfAgent, BigDecimal fee) {
        income  = fee;
        agentId = zfAgent.getAgentId();
        recordDate  = DateUtil.format(new Date(), DateUtil.YYYY_MM_DD);
    }
}

