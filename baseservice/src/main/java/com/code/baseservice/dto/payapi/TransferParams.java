package com.code.baseservice.dto.payapi;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.math.BigDecimal;

@NoArgsConstructor
@Data
public class TransferParams {

    @JsonProperty("merchant_order_no")
    @JsonAlias(value = {"xsh_order_no", "order_number", "order", "order_no"})
    @NonNull
    private String merchant_order_no;

    /**
     * 提款金额
     */
    @JsonProperty("pay_amount")
    @NonNull
    private BigDecimal pay_amount;

    /**
     * 商户id
     */
    @JsonProperty("merchant_id")
    @NonNull
    private Integer merchant_id;

    /**
     * 签名字符串
     */
    @JsonProperty("sign")
    @NonNull
    private String sign;

    /**
     * 提款人
     */
    @JsonProperty("card_name")
    @NonNull
    private String card_name;

    /**
     * 异步通知地址
     */
    @JsonProperty("notify_url")
    @NonNull
    private  String notify_url;

    /**
     * 备注
     */
    @JsonProperty("remark")
    private String remark;

    /**
     * 银行类型
     */
    @JsonProperty("card_type")
    @NonNull
    private String card_type;


    /**
     * 银行类型
     */
    @JsonProperty("callToken")
    @NonNull
    private String token;

    /**
     * 银行卡号
     */
    @JsonProperty("card_account")
    @NonNull
    private String card_account;

    /**
     * 开户行
     */
    @JsonProperty("card_address")
    @NonNull
    private String card_address;

    /**
     * 转账类型
     */
    @JsonProperty("order_type")
    @NonNull
    private Integer order_type = 1;

}
