package com.code.baseservice.dto.payapi;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@NoArgsConstructor
@Data

public class QueryParams {
    @JsonProperty("merchant_order_no")
    @JsonAlias(value = {"xsh_order_no", "order_number", "order", "order_no"})
    @NonNull
    private String merchant_order_no;

    @JsonAlias(value = {"s_order_no"})
    @NonNull
    private String order_no;

    @JsonProperty("merchant_id")
    @NonNull
    private String merchant_Id;

    @JsonProperty("status")
    @NonNull
    private String status;

    @JsonProperty("sign")
    @NonNull
    private String  sign;
}
