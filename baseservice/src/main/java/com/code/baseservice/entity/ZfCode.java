package com.code.baseservice.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * (ZfCode)实体类
 *
 * @author makejava
 * @since 2023-03-19 23:07:15
 */
@Data
public class ZfCode implements Serializable {
    private static final long serialVersionUID = 449487199437871700L;
    /**
     * 主键id
     */
    private Integer codeId;
    /**
     * 代理id
     */
    private Integer agentId;
    /**
     * 最小收金额
     */
    private BigDecimal minAmount;
    /**
     * 最大收金额
     */
    private BigDecimal maxAmount;
    /**
     * 二维码地址
     */
    private String image;
    /**
     * 二维码状态
     */
    private Integer status;
    /**
     * 开关状态
     */
    private Integer isOpen;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 日限额
     */
    private BigDecimal dayLimitAmount;
    /**
     * 日限笔数
     */
    private Integer dayLimitTimes;
    
    private String name;

    private Integer  transStatus;

    private BigDecimal balance;

    private String account;

    private Integer transferStatus;

    private Integer channelId;

    private String ip;


}

