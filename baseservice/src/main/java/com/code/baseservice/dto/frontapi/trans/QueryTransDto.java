package com.code.baseservice.dto.frontapi.trans;

import com.code.baseservice.dto.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.models.auth.In;
import lombok.Data;

import java.util.Date;

@Data
public class QueryTransDto extends BaseEntity {

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
     * 商户订单号
     */
    @JsonProperty("remark")
    private String remark;

    private Integer agentId;


}
