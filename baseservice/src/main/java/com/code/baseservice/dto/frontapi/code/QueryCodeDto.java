package com.code.baseservice.dto.frontapi.code;

import com.code.baseservice.dto.BaseEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class QueryCodeDto extends BaseEntity {

    /*
    * 开关状态
     */
    @JsonProperty("is_open")
    private Integer isOpen;

    /**
     * 二维码类型
     */
    @JsonProperty("code_type")
    private Integer codeType;


    /**
     * 二维码账号
     */
    @JsonProperty("account")
    private String account;

    /**
     * 二维码名称
     */
    @JsonProperty("name")
    private String name;

    private Integer agentId;
}
