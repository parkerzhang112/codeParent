package com.code.baseservice.dto.frontapi.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ConfirmOrderDto {


    /**
     * 商户订单号
     */
    @JsonProperty("merchant_order_no")
    private String merchantOrderNo;


    /**
     * 订单状态
     */
    @JsonProperty("order_status")
    private Integer orderStatus;

}
