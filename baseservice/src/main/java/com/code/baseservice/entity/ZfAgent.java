package com.code.baseservice.entity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * (ZfAgent)实体类
 *
 * @author makejava
 * @since 2023-03-19 23:11:47
 */
public class ZfAgent implements Serializable {
    private static final long serialVersionUID = 719686779229036010L;
    /**
     * 代理id
     */
    private Integer agentId;
    /**
     * 代理余额
     */
    private BigDecimal balance;
    /**
     * 代理名称
     */
    private String name;
    /**
     * 代理费率
     */
    private BigDecimal rate;
    /**
     * 上级代理id
     */
    private Integer parentId;
    /**
     * 代理登录密码
     */
    private String pwd;
    /**
     * 谷歌验证码
     */
    private String googleCode;
    /**
     * 代理登录密码
     */
    private String agentAccount;


    public Integer getAgentId() {
        return agentId;
    }

    public void setAgentId(Integer agentId) {
        this.agentId = agentId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getGoogleCode() {
        return googleCode;
    }

    public void setGoogleCode(String googleCode) {
        this.googleCode = googleCode;
    }

    public String getAgentAccount() {
        return agentAccount;
    }

    public void setAgentAccount(String agentAccount) {
        this.agentAccount = agentAccount;
    }

}

