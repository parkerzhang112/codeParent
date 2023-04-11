package com.code.baseservice.util;

import com.alibaba.fastjson.JSONObject;
import com.code.baseservice.entity.ZfMerchant;
import com.code.baseservice.entity.ZfRecharge;
import com.code.baseservice.entity.ZfWithdraw;

import java.util.HashMap;
import java.util.Map;

public class Telegram {
    public void  sendMesaage(Map<String, Object> map, String url){
        HttpClientUtil.doPostJson(url, JSONObject.toJSONString(map));
    }


    public void sendMerchantBalanceMessage(ZfMerchant xMerchant, ZfRecharge zfRecharge, String config) {
    }

    public void sendWarrnSmsMessage(ZfWithdraw zfWithdraw, String bankCode, String config) {
    }
}
