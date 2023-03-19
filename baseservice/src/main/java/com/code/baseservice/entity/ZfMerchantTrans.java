package com.code.baseservice.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * (ZfMerchantTrans)实体类
 *
 * @author makejava
 * @since 2023-03-19 23:08:17
 */
@Data
public class ZfMerchantTrans implements Serializable {
    private static final long serialVersionUID = 170153784606053442L;
    
    private Integer merchantId;
    /**
     * 交易前余额
     */
    private BigDecimal preBalance;
    /**
     * 余额
     */
    private BigDecimal balance;
    /**
     * 交易金额
     */
    private BigDecimal amount;
    /**
     * 商户手续费
     */
    private BigDecimal merchantFee;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 备注
     */
    private String remark;
    /**
     * 交易订单号
     */
    private String merchantOrderNo;

}

