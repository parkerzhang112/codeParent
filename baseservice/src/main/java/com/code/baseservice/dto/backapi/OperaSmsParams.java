package com.code.baseservice.dto.backapi;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@ApiModel("操作订单参数说明")
public class OperaSmsParams {

    @ApiModelProperty(value = "短信id", dataType ="int", allowEmptyValue = false, required = true)
    @JsonProperty("sms_id")
    private Integer smsId;

    @ApiModelProperty(value = "操作人", dataType ="string", allowEmptyValue = false, required = true)
    @JsonProperty("opeara")
    private String opeara;
}
