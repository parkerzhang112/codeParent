package com.code.baseservice.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * (ZfAgentRecord)实体类
 *
 * @author makejava
 * @since 2023-03-19 23:06:11
 */
public class ZfAgentRecord implements Serializable {
    private static final long serialVersionUID = 675114679783180957L;
    /**
     * 代理id
     */
    private Integer agentId;
    /**
     * 记录日期
     */
    private Date recordDate;
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


    public Integer getAgentId() {
        return agentId;
    }

    public void setAgentId(Integer agentId) {
        this.agentId = agentId;
    }

    public Date getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(Date recordDate) {
        this.recordDate = recordDate;
    }

    public Integer getRechargeTimes() {
        return rechargeTimes;
    }

    public void setRechargeTimes(Integer rechargeTimes) {
        this.rechargeTimes = rechargeTimes;
    }

    public BigDecimal getRechargeAmount() {
        return rechargeAmount;
    }

    public void setRechargeAmount(BigDecimal rechargeAmount) {
        this.rechargeAmount = rechargeAmount;
    }

    public Integer getWithdrawTimes() {
        return withdrawTimes;
    }

    public void setWithdrawTimes(Integer withdrawTimes) {
        this.withdrawTimes = withdrawTimes;
    }

    public BigDecimal getWithdrawAmount() {
        return withdrawAmount;
    }

    public void setWithdrawAmount(BigDecimal withdrawAmount) {
        this.withdrawAmount = withdrawAmount;
    }

    public BigDecimal getIncome() {
        return income;
    }

    public void setIncome(BigDecimal income) {
        this.income = income;
    }

}

