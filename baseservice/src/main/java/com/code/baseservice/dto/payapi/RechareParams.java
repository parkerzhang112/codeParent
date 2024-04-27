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
    private Integer merchant_id;

    @JsonProperty("name")
    @NonNull
    private String name;

    @JsonProperty("pay_type")
    private Integer pay_type;

    @JsonProperty("pay_amount")
    @NonNull
    private BigDecimal pay_amount;


    @JsonProperty("sign")
    @NonNull
    private String sign;

    @JsonProperty("notify_url")
    @NonNull
    private  String notify_url;

    @JsonProperty("return_url")
    @NonNull
    private  String return_url;
    @JsonProperty("remark")
    private String remark;
}
