package com.code.baseservice.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.io.Serializable;

/**
 * (ZfAgentRechargeOrder)实体类
 *
 * @author makejava
 * @since 2023-08-21 23:23:46
 */
@Data
public class ZfAgentRechargeOrder implements Serializable {
    private static final long serialVersionUID = 837859443527842300L;
    /**
     * 订单号
     */
    private String orderNo;
    /**
     * 订单金额
     */
    private BigDecimal payAmount;
    /**
     * 实际金额
     */
    private BigDecimal paidAmount;
    /**
     * 0 创建  1出码 2已打款  3确认，4失败 
     */
    private Integer orderStatus;
    
    private String remark;
    /**
     * 代理id
     */
    private Integer agentId;
    /**
     * 二维码图片地址
     */
    private String image;
    /**
     * 银行卡信息
     */
    private String bankinfo;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 更新人
     */
    private String updateby;
    /**
     * 支付类型，0直充 1赊账
     */
    private Integer payType;
    /**
     * 支付凭证
     */
    private String payImage;

}

