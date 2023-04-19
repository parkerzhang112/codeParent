package com.code.baseservice.entity;

import com.code.baseservice.dto.payapi.RechareParams;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * (ZfRecharge)实体类
 *
 * @author makejava
 * @since 2023-03-19 23:08:26
 */
@Data
public class ZfRecharge implements Serializable {
    private static final long serialVersionUID = 423272938303655611L;
    
    private String merchantOrderNo;
    
    private BigDecimal payAmount;
    
    private BigDecimal paidAmount;
    
    private String orderNo;
    
    private String notifyUrl;
    
    private Integer codeId;
    
    private Date createTime;
    
    private Date updateTime;
    
    private Integer merchantId;
    
    private Integer channelId;
    
    private Integer agentId;
    
    private Integer orderStatus;
    
    private Integer notifyStatus;
    
    private BigDecimal merchantFee;

    private String remark;

    public  ZfRecharge(RechareParams rechareParams){
        setMerchantId(rechareParams.getMerchant_id());
        setMerchantOrderNo(rechareParams.getMerchant_order_no());
        setNotifyUrl(rechareParams.getNotify_url());
        setPayAmount(rechareParams.getPay_amount());
    }

    public ZfRecharge(){

    }

}

