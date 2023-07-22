package com.code.baseservice.dto.backapi;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@ApiModel("代理余额押金操作")
public class OperaAgentParams {

    @ApiModelProperty(value = "余额", dataType = "String", required = true, allowEmptyValue = false)
    @JsonProperty("balance")
    private BigDecimal balance;

    @ApiModelProperty(value = "已收额度", dataType = "String", required = true, allowEmptyValue = false)
    @JsonProperty("accept_amount")
    private BigDecimal acceptAmount;

    @ApiModelProperty(value = "交易金额", dataType = "String", required = true, allowEmptyValue = false)
    @JsonProperty("amount")
    private BigDecimal amount;

    @ApiModelProperty(value = "备注", dataType = "String", required = true, allowEmptyValue = false)
    @JsonProperty("remark")
    private String remark;

    @ApiModelProperty(value = "备注", dataType = "String", required = true, allowEmptyValue = false)
    @JsonProperty("agent_id")
    private Integer agentId;

    @ApiModelProperty(value = "交易类型", dataType = "Integer", required = true, allowEmptyValue = true)
    @JsonProperty("trans_type")
    private Integer transType;


    @ApiModelProperty(value = "额度类型", dataType = "Integer", required = true, allowEmptyValue = true)
    @JsonProperty("amount_type")
    private Integer amount_type;

    private Boolean isFinsh = false;
}
