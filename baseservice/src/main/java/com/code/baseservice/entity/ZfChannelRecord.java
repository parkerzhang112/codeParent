package com.code.baseservice.entity;

import com.code.baseservice.util.DateUtil;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * (ZfChannelRecord)实体类
 *
 * @author makejava
 * @since 2023-09-03 10:45:35
 */
@Data
public class ZfChannelRecord implements Serializable {
    private static final long serialVersionUID = -76338546012686387L;
    /**
     * 渠道id
     */
    private Integer channelId;
    /**
     * 充值额度
     */
    private BigDecimal rechargeAmount;
    /**
     * 充值费用
     */
    private BigDecimal channelFee;
    /**
     * 记录时间
     */
    private String recordDate;
    /**
     * 渠道名称
     */
    private String channelName;
    /**
     * 商户id
     */
    private Integer merchantId;

    private Integer rechargeTimesTotal;

    private Integer rechargeTimes;

    public ZfChannelRecord(){
    }

    public ZfChannelRecord(ZfRecharge zfRecharge, ZfChannel zfChannel, BigDecimal Fee) {
        channelId = zfRecharge.getChannelId();
        merchantId = zfRecharge.getMerchantId();
        recordDate = DateUtil.format(new Date(), DateUtil.YYYY_MM_DD);
        channelName= zfChannel.getChannelName();
        rechargeAmount = zfRecharge.getPayAmount();
        rechargeTimes = 1;
        channelFee = Fee;
    }

    public ZfChannelRecord(ZfWithdraw zfWithdraw, ZfChannel zfChannel, BigDecimal channlFee) {
        channelId = zfWithdraw.getChannelId();
        merchantId = zfWithdraw.getMerchantId();
        recordDate = DateUtil.format(new Date(), DateUtil.YYYY_MM_DD);
        channelName= zfChannel.getChannelName();
        rechargeAmount = zfWithdraw.getPayAmount();
        rechargeTimes = 1;
        channelFee = channlFee;
    }
}

