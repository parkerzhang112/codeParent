package com.code.baseservice.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * (ZfAgentTrans)实体类
 *
 * @author makejava
 * @since 2023-03-19 23:06:24
 */
public class ZfAgentTrans implements Serializable {
    private static final long serialVersionUID = 540178769990295068L;
    
    private Integer agentId;
    
    private String preBalance;
    
    private String balance;
    
    private String amount;
    
    private Integer orderNo;
    
    private Date createTime;
    
    private Date updateTime;
    
    private String remark;


    public Integer getAgentId() {
        return agentId;
    }

    public void setAgentId(Integer agentId) {
        this.agentId = agentId;
    }

    public String getPreBalance() {
        return preBalance;
    }

    public void setPreBalance(String preBalance) {
        this.preBalance = preBalance;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public Integer getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

}

