package com.code.baseservice.entity;

import com.code.baseservice.dto.payapi.TransferParams;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 提款订单表(ZfWithdraw)实体类
 *
 * @author makejava
 * @since 2023-03-19 23:08:41
 */
@Data
public class ZfWithdraw implements Serializable {
    private static final long serialVersionUID = -71999264437430151L;
    /**
     * 订单号
     */
    private String orderNo;
    /**
     * 商户订单号
     */
    private String merchantOrderNo;
    /**
     * 订单金额
     */
    private BigDecimal payAmount;
    /**
     * 实际订单金额
     */
    private BigDecimal paidAmount;
    /**
     * 支付银行卡id
     */
    private Integer codeId;
    /**
     * 渠道id
     */
    private Integer channelId;
    /**
     * 商户id
     */
    private Integer merchantId;
    /**
     *  订单状态 0 创建 1处理中 2成功 3  失败
     */
    private Integer orderStatus;
    /**
     * 处理会员
     */
    private Integer agentId;
    /**
     * 通知地址
     */
    private String notifyUrl;
    /**
     * 0 未通知 1通知成功  2 通知失败
     */
    private Integer notifyStatus;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 提款姓名
     */
    private String cardAccount;
    /**
     *  开户行地址
     */
    private String cardAddress;
    /**
     * 持卡人名
     */
    private String cardName;

    /**
     * 银行类型
     */
    private String cardType;
    /**
     * 备注
     */
    private String remark;
    /**
     * 渠道手续费
     */
    private BigDecimal channelFee;
    /**
     * 订单类型 0代付订单 1下发订单
     */
    private Integer orderType;
    /**
     * 流水支付凭证
     */
    private String image;
    /**
     * 会员手续费
     */
    private BigDecimal merchantFee;
    /**
     * 父订单订单号  默认未空
     */
    private String parentOrderNo;
    /**
     * 是否拆单 0未 1是 默认未0
     */
    private Integer isSplit;

    public ZfWithdraw(TransferParams transParams) {

        setMerchantOrderNo(transParams.getMerchant_order_no());
        setCardAccount(transParams.getCard_account());
        setCardAddress(transParams.getCard_address());
        setCardName(transParams.getCard_name());
        setCardType(transParams.getCard_type());
        setOrderType(transParams.getOrder_type());
        setPayAmount(transParams.getPay_amount());
        setNotifyUrl(transParams.getNotify_url());
        setRemark(transParams.getRemark());
    }
}

