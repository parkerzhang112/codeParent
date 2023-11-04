package com.code.baseservice.service;

import com.alibaba.fastjson.JSONObject;
import com.code.baseservice.entity.ZfChannel;
import com.code.baseservice.entity.ZfRecharge;
import com.code.baseservice.entity.ZfWithdraw;

import java.util.Map;

public interface BaseService {


    String notify(Map<String, Object> map);


    JSONObject create(ZfChannel zfChannel, ZfRecharge zfRecharge);

    JSONObject create(ZfChannel zfChannel, ZfWithdraw zfWithdraw);

    JSONObject queryByWithdraw(ZfChannel zfChannel, ZfWithdraw zfWithdraw);

}
