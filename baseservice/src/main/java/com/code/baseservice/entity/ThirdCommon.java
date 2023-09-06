package com.code.baseservice.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class ThirdCommon  implements Serializable {
    private String thirdMerchantOderNo;

    private BigDecimal payAmount;

    private String reason;

    private Boolean isSuccess;
}
