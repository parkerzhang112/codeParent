package com.code.baseservice.entity;

import com.code.baseservice.util.DateUtil;
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
    
    private String recordDate;
    
    private BigDecimal rechargeAmount;
    
    private Integer rechargeTimes;

    private BigDecimal withdrawAmount;

    private Integer withdrawTimes;

    private BigDecimal merchantFee;

    public ZfMerchantRecord(ZfRecharge zfRecharge) {
        merchantId = zfRecharge.getMerchantId();
        recordDate = DateUtil.format(new Date(), DateUtil.YYYY_MM_DD);
        rechargeAmount = zfRecharge.getPaidAmount();
        rechargeTimes = 1;
        merchantFee  = zfRecharge.getMerchantFee();
    }

    public ZfMerchantRecord() {

    }

    public ZfMerchantRecord(ZfWithdraw zfWithdraw) {
        merchantId = zfWithdraw.getMerchantId();
        recordDate = DateUtil.format(new Date(), DateUtil.YYYY_MM_DD);
        withdrawAmount = zfWithdraw.getPayAmount();
        withdrawTimes = 1;
        merchantFee  = zfWithdraw.getMerchantFee();
    }
}

