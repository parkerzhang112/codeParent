package com.code.baseservice.dto.frontapi;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 【二维码】对象 zf_code
 *
 * @author ruoyi
 * @date 2023-03-18
 */
@Data
public class LoginDto {
    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @JsonProperty("user_name")
    private String userName;

    /**
     * 代理id
     */
    @JsonProperty("password")
    private String password;

    /**
     * 二维码地址
     */
    @JsonProperty("code")
    private String code;
}
