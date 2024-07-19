package com.code.baseservice.dto.frontapi.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

@Data
public class QueryOrderDto {

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("start_time")
    private Date startTime;

    /**
     * 结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("end_time")
    private Date endTime;


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

}
