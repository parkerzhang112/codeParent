package com.code.baseservice.dto;

import lombok.Data;

@Data
public class XChannelRate {

    //手续费类型
    Integer rate_type;

    //手续费设定值
    Double rate_value;

    //渠道id
    String channel_id;
}
