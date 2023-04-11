package com.code.baseservice.dto.payapi;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public
class MerchantParams {

    @JsonProperty("merchant_id")
    @NonNull
    private Integer merchant_id;

    @JsonProperty("sign")
    @NonNull
    private String sign;
}
