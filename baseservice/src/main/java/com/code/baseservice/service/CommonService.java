package com.code.baseservice.service;

import com.alibaba.fastjson.JSONObject;
import com.code.baseservice.dto.payapi.RechareParams;
import com.code.baseservice.entity.ZfChannel;

import java.math.BigDecimal;

public interface CommonService {

    JSONObject request(ZfChannel zfChannel, RechareParams rechareParams);
}
