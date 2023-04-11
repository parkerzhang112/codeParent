package com.code.baseservice.dto.backapi;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@ApiModel("操作订单参数说明")
public class OperaOrderParams {

    @ApiModelProperty(value = "订单号", dataType = "String", required = true, allowEmptyValue = false)
    @JsonProperty("order_no")
    private String orderNo;

    @ApiModelProperty(value = "商户订单号", dataType = "String", required = true, allowEmptyValue = false)
    @JsonProperty("merchant_order_no")
    private String merchanOrderNo;

    @ApiModelProperty(value = "备注", dataType = "String", required = true, allowEmptyValue = true)
    @JsonProperty("close_reason")
    private String closeReason;

    @ApiModelProperty(value = "支付凭证", dataType = "String", required = true, allowEmptyValue = true)
    @JsonProperty("image")
    private String image;

    @ApiModelProperty(value = "支付金额", dataType = "String", required = true, allowEmptyValue = true)
    @JsonProperty("pay_amt")
    private BigDecimal paid_amt;

    @ApiModelProperty(value = "订单类型, 0入款 1出款 ")
    @JsonProperty("order_type")
    private int orderType;

    @ApiModelProperty(value = "银行卡id, 出款的时候用", dataType = "int", required = false, allowEmptyValue = true)
    @JsonProperty("card_id")
    private Integer cardId;

    @JsonProperty("memberId")
    private int memberId;

    @JsonProperty("trand_id")
    private int trandId;

    @JsonProperty("name")
    private String name;

}
