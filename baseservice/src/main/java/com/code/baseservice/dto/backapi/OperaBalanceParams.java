package com.code.baseservice.dto.backapi;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@ApiModel("商户上分操作")
public class OperaBalanceParams {

    @ApiModelProperty(value = "订单号", dataType = "String", required = true, allowEmptyValue = false)
    @JsonProperty("order_no")
    private String orderNo;

    @ApiModelProperty(value = "交易类型", dataType = "Integer", required = true, allowEmptyValue = true)
    @JsonProperty("trans_type")
    private Integer transType;

    @ApiModelProperty(value = "merchant_id", dataType = "Integer", required = true, allowEmptyValue = true)
    @JsonProperty("merchant_id")
    private Integer merchantId;

    @ApiModelProperty(value = "订单类型, 0入款 1出款 ")
    @JsonProperty("order_type")
    private int orderType;

    @ApiModelProperty(value = "上分金额", dataType = "String", required = true, allowEmptyValue = true)
    @JsonProperty("amount")
    private BigDecimal amount;

    @ApiModelProperty(value = "上分备注")
    @JsonProperty("remark")
    private String remark;
}
