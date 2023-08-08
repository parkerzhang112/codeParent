package com.code.baseservice.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * (ZfAgent)实体类
 *
 * @author makejava
 * @since 2023-03-19 23:11:47
 */
@Data
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
    private String  rate;
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

    private String config;
    /**
     * 可收额度
     */
    private BigDecimal acceptAmount;

    /**
     * 押金
     */
    private BigDecimal totalAcceptAmount;

    /**
     * 提示
     */
    private Integer notice;

    /**
     * 权重
     */
    private BigDecimal weight;

}

