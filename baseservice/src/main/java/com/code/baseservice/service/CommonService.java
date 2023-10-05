package com.code.baseservice.service;

import com.alibaba.fastjson.JSONObject;
import com.code.baseservice.dto.payapi.QueryParams;
import com.code.baseservice.entity.ZfChannel;
import com.code.baseservice.entity.ZfRecharge;
import com.code.baseservice.entity.ZfWithdraw;

import java.util.Map;

public interface CommonService {



    JSONObject notify(QueryParams queryParams);


    JSONObject create(ZfChannel zfChannel, ZfRecharge zfRecharge);

    JSONObject create(ZfChannel zfChannel, ZfWithdraw zfWithdraw);

    JSONObject queryByWithdraw(ZfChannel zfChannel, ZfWithdraw zfWithdraw);

}
