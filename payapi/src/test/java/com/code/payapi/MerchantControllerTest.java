package com.code.payapi;

import com.alibaba.fastjson.JSONObject;
import com.code.baseservice.dto.payapi.MerchantParams;
import com.code.baseservice.entity.ZfMerchant;
import com.code.baseservice.service.ZfMerchantService;
import com.code.baseservice.util.MD5Util;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class MerchantControllerTest extends PayapiApplicationTests {

    @Autowired
    ZfMerchantService zfMerchantService;

    @Test
    public void testBalance() {
        MerchantParams merchantParams = new MerchantParams();
        ZfMerchant zfMerchant= zfMerchantService.queryById(10019);
        merchantParams.setMerchant_id(zfMerchant.getMerchantId().toString());
        String encod_str = "merchant_id=" + merchantParams.getMerchant_id()
                + "&key=" + zfMerchant.getKey();
        log.info("签名字符串 {}", encod_str);
        String sign = MD5Util.getMD5Str(encod_str).toUpperCase();
        merchantParams.setSign(sign);
        JSONObject jsonObject = zfMerchantService.query(merchantParams);
        System.out.print(jsonObject);
    }
}
