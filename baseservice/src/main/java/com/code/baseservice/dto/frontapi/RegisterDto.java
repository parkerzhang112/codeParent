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
public class RegisterDto {
    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @JsonProperty("user_name")
    private String userName;

    /**
     * 密码
     */
    @JsonProperty("password")
    private String password;

    /**
     * 二次密码
     */
    @JsonProperty("confirm_password")
    private String confirm_password;

    /**
     * 邀请码
     */
        @JsonProperty("invite_code")
    private String InvitationCode;

    /**
     * 验证码
     */
    @JsonProperty("code")
    private String code;
}
