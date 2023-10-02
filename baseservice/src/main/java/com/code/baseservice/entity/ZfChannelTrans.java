package com.code.baseservice.entity;

import com.code.baseservice.base.enums.TransTypeEnum;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.io.Serializable;

/**
 * (ZfChannelTrans)实体类
 *
 * @author makejava
 * @since 2023-09-03 10:45:19
 */
@Data
public class ZfChannelTrans implements Serializable {
    private static final long serialVersionUID = -20508718288408164L;
    /**
     * 渠道id
     */
    private Integer channelId;
    /**
     * 交易前余额
     */
    private BigDecimal preBalance;
    /**
     * 余额
     */
    private BigDecimal balance;
    /**
     * 手续费
     */
    private BigDecimal channelFee;
    /**
     * 系统订单号
     */
    private String orderNo;
    /**
     * 商户订单号
     */
    private String merchantOrderNo;

    /**
     * 交易金额
     */
    private BigDecimal amount;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 备注
     */
    private String remark;
    /**
     * 操作人
     */
    private String updateBy;

    //交易类型
    private Integer transType;

    public ZfChannelTrans(ZfRecharge zfRecharge, ZfChannel zfChannel, BigDecimal fee) {
        preBalance  = zfChannel.getChannelBalance();
        balance  = zfChannel.getChannelBalance().add(zfRecharge.getPayAmount()).subtract(fee);
        channelFee = fee;
        orderNo = zfRecharge.getOrderNo();
        merchantOrderNo = zfRecharge.getMerchantOrderNo();
        remark = "订单上分";
        channelId  = zfChannel.getChannelId();
        transType = TransTypeEnum.RRCHARGE.getValue();
        amount = zfRecharge.getPayAmount();
    }


    public ZfChannelTrans() {

    }

    public ZfChannelTrans(ZfWithdraw zfWithdraw, ZfChannel zfChannel, BigDecimal channlFee) {
        preBalance  = zfChannel.getChannelBalance();
        balance  = zfChannel.getChannelBalance().subtract((zfWithdraw.getPayAmount()).add(channlFee));
        channelFee = channlFee;
        orderNo = zfWithdraw.getOrderNo();
        merchantOrderNo = zfWithdraw.getMerchantOrderNo();
        remark = "代付订单减额";
        channelId  = zfChannel.getChannelId();
        transType = TransTypeEnum.TRANSFER.getValue();
        amount = zfWithdraw.getPayAmount();
    }
}

