package com.code.baseservice.dto.frontapi.order;

import com.code.baseservice.dto.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class QueryOrderDto extends BaseEntity {

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("start_time")
    private String startTime;

    /**
     * 结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("end_time")
    private String endTime;


    /**
     * 商户订单号
     */
    @JsonProperty("order_no")
    private String orderNo;


    /**
     * 订单状态
     */
    @JsonProperty("order_status")
    private Integer orderStatus;

    /**
     * 代理id
     */
    @JsonProperty("agent_id")
    private Integer agentId;

    /**
     * 二维码id
     */
    @JsonProperty("code_id")
    private Integer codeId;

}
