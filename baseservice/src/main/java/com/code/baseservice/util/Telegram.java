package com.code.baseservice.util;

import com.alibaba.fastjson.JSONObject;
import com.code.baseservice.entity.ZfMerchant;
import com.code.baseservice.entity.ZfRecharge;

import java.util.HashMap;
import java.util.Map;

public class Telegram {
    public void  sendMesaage(Map<String, Object> map, String url){
        HttpClientUtil.doPostJson(url, JSONObject.toJSONString(map));
    }


    public void sendMerchantBalanceMessage(ZfMerchant xMerchant, ZfRecharge zfRecharge, String config) {
    }
}
