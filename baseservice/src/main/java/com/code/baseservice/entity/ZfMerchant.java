package com.code.baseservice.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * (ZfMerchant)实体类
 *
 * @author makejava
 * @since 2023-03-19 23:07:57
 */
@Data
public class ZfMerchant implements Serializable {
    private static final long serialVersionUID = 557293816769063675L;
    
    private Integer merchantId;
    
    private BigDecimal balance;
    
    private Integer status;
    
    private String merchantCode;
    
    private String remark;

    private String key;

    private String merchantName;

    private BigDecimal creditAmount;

    private String merchantRate;


    private String domain;

    private Long groupId;
}

