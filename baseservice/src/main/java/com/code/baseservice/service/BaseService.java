package com.code.baseservice.service;

import com.alibaba.fastjson.JSONObject;
import com.code.baseservice.dto.payapi.QueryParams;
import com.code.baseservice.entity.ZfChannel;
import com.code.baseservice.entity.ZfRecharge;

public interface BaseService {


    JSONObject notify(QueryParams queryParams);


    JSONObject create(ZfChannel zfChannel, ZfRecharge zfRecharge);
}
