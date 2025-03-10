package com.code.baseservice.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * (ZfChannel)实体类
 *
 * @author makejava
 * @since 2023-03-19 23:06:51
 */
@Data
public class ZfChannel implements Serializable {
    private static final long serialVersionUID = -89468178571180403L;
    /**
     * 渠道id
     */
    private Integer channelId;
    /**
     * 渠道名称
     */
    private String  channelName;
    /**
     * 渠道类型 0 出 1入
     */
    private Integer channelType;
    /**
     * 渠道费率
     */
    private String channelRate;
    /**
     * 渠道状态 0 停  1启
     */
    private Integer channelStatus;
    /**
     * 最小金额
     */
    private BigDecimal minAmount;
    /**
     * 最大金额
     */
    private BigDecimal maxAmount;

    /**支付类型*/
    private Integer payType;

    //渠道余额
    private BigDecimal channelBalance;

    //商户代码
    private String channelCode;

    //三方商户id
    private String thirdMerchantId;

    //三方商户私钥
    private String thirdMerchantPrivateKey;

    //三方商户公钥
    private String thirdMerchantPublicKey;

    private BigDecimal thirdMerchantChannelRate;

    private String remark;

    private String domain;

    private String proxy;

    //商品价格表
    private String goods;

}

