package com.code.baseservice.dto.frontapi.code;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 【二维码】对象 zf_code
 *
 * @author ruoyi
 * @date 2023-03-18
 */
@Data
public class OpenCodeDto {
    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @JsonProperty("code_id")
    private Integer codeId;

    /**
     * 主键id
     */
    @JsonProperty("is_open")
    private Integer isOpen;

}
