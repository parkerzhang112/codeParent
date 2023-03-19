package com.code.baseservice.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * (ZfMerchantRecord)实体类
 *
 * @author makejava
 * @since 2023-03-19 23:08:08
 */
@Data
public class ZfMerchantRecord implements Serializable {
    private static final long serialVersionUID = -60824821435662218L;
    
    private Integer merchantId;
    
    private Date recordDate;
    
    private BigDecimal rechargeAmount;
    
    private Integer rechargeTimes;
    
    private BigDecimal merchantFee;

}

