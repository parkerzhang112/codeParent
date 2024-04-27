package com.code.baseservice.dto.payapi;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.math.BigDecimal;

@NoArgsConstructor
@Data
public class RechareParams {

    @JsonProperty("merchant_order_no")
    @NonNull
    private String merchant_order_no;

    @JsonProperty("merchant_id")
    @NonNull
    private String merchant_id;


    @JsonProperty("pay_amount")
    @NonNull
    private BigDecimal pay_amount;


    @JsonProperty("sign")
    @NonNull
    private String sign;

    @JsonProperty("notify_url")
    @NonNull
    private  String notify_url;

    @JsonProperty("remark")
    private String remark;

    @JsonProperty("is_test")
    private Integer is_test;
}
