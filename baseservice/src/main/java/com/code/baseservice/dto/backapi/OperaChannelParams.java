package com.code.baseservice.dto.backapi;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@ApiModel("渠道余额押金操作")
public class OperaChannelParams {

    @ApiModelProperty(value = "交易金额", dataType = "String", required = true, allowEmptyValue = false)
    @JsonProperty("amount")
    private BigDecimal amount;

    @ApiModelProperty(value = "备注", dataType = "String", required = true, allowEmptyValue = false)
    @JsonProperty("remark")
    private String remark;

    @ApiModelProperty(value = "渠道id", dataType = "String", required = true, allowEmptyValue = false)
    @JsonProperty("channel_id")
    private Integer channelId;

    @ApiModelProperty(value = "交易类型", dataType = "Integer", required = true, allowEmptyValue = true)
    @JsonProperty("trans_type")
    private Integer transType;
}
