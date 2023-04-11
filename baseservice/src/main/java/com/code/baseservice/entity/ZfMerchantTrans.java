package com.code.baseservice.entity;

import com.code.baseservice.base.enums.TransTypeEnum;
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

    /**
     * 交易类型  0 出 1入
     */
    private Integer transType;

    public ZfMerchantTrans(){

    }

    public ZfMerchantTrans(ZfRecharge zfRecharge, ZfMerchant zfMerchant) {
        merchantId = zfRecharge.getMerchantId();
        preBalance = zfMerchant.getBalance();
        balance = zfMerchant.getBalance().add(zfRecharge.getPaidAmount().subtract(zfRecharge.getMerchantFee()));
        amount = zfRecharge.getPaidAmount();
        merchantFee = zfRecharge.getMerchantFee();
        merchantOrderNo = zfRecharge.getMerchantOrderNo();
        transType = TransTypeEnum.RRCHARGE.getValue();

    }

    public ZfMerchantTrans(ZfWithdraw zfWithdraw, ZfMerchant zfMerchant, int type) {
        merchantId = zfWithdraw.getMerchantId();
        preBalance = zfMerchant.getBalance();
        amount = zfWithdraw.getPayAmount();
        merchantOrderNo = zfWithdraw.getMerchantOrderNo();
        merchantFee = zfWithdraw.getChannelFee();
        if(type == TransTypeEnum.TRANSFER.getValue()){
            balance = zfMerchant.getBalance().subtract(zfWithdraw.getPayAmount().add(merchantFee));
            transType = TransTypeEnum.TRANSFER.getValue();
            setRemark(zfWithdraw.getOrderType() == 1 ? "下发":"代付");
        }else {
            setTransType(1);
            if(zfWithdraw.getPaidAmount().compareTo(BigDecimal.ZERO) > 0){
                amount = amount.subtract(zfWithdraw.getPaidAmount());
            }
            setAmount(amount);
            setBalance(zfMerchant.getBalance().add(amount).add(zfWithdraw.getChannelFee()));
            setRemark("冲正");
        }
    }
}

