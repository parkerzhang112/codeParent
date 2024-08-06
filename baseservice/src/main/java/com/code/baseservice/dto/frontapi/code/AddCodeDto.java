package com.code.baseservice.dto.frontapi.code;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

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

    @JsonProperty("agent_id")
    private Integer agentId;

    /**
     * 二维码地址
     */
    @JsonProperty("image_url")
    private String imageUrl;

    private MultipartFile image;

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
     * 单笔最小
     */
    @JsonProperty("min_amount")
    private BigDecimal minAmount;

    /**
     * 单笔最大
     */
    @JsonProperty("max_amount")
    private BigDecimal maxAmount;

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
