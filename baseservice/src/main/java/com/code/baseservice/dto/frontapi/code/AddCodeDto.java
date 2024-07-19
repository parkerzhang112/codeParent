package com.code.baseservice.dto.frontapi.code;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 【二维码】对象 zf_code
 *
 * @author ruoyi
 * @date 2023-03-18
 */
@Data
public class AddCodeDto  {
    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @JsonProperty("code_id")
    private Integer codeId;

    /**
     * 代理id
     */
    @JsonProperty("agent_id")
    private Integer agentId;

    /**
     * 二维码地址
     */
    @JsonProperty("image_url")
    private String imageUrl;

    /**
     * 开关状态
     */
    @JsonProperty("is_open")
    private Integer isOpen;

    /**
     * 日限额
     */
    @JsonProperty("day_limit_amount")
    private BigDecimal dayLimitAmount;

    /**
     * 日限笔数
     */
    @JsonProperty("day_limit_times")
    private Integer dayLimitTimes;

    /**
     * 名称
     */
    @JsonProperty("name")
    private String name;


    /**
     * 支付宝账号
     */
    @JsonProperty("account")
    private String account;


    /**
     * 二维码类型
     */
    @JsonProperty("code_type")
    private Integer codeType;

}
