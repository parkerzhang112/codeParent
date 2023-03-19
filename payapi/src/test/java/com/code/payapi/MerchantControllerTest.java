package com.code.payapi;

import com.alibaba.fastjson.JSONObject;
import com.xiagao.baseservice.dto.api.MerchantParams;
import com.xiagao.baseservice.entity.XMerchant;
import com.xiagao.baseservice.service.XMerchantService;
import com.xiagao.baseservice.util.MD5Util;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Slf4j
public class MerchantControllerTest extends PayapiApplicationTests {

    @Autowired
    XMerchantService xMerchantService;

    @Test
    public void testBalance() {
        MerchantParams merchantParams = new MerchantParams();
        List<XMerchant> xMerchants = xMerchantService.queryAllByLimit(0, 1);
        if (xMerchants.size() > 0) {
            merchantParams.setMerchant_id(xMerchants.get(0).getMerchantId());
            String encod_str = "merchant_id=" + merchantParams.getMerchant_id()
                    + "&key=" + xMerchants.get(0).getKey();
            log.info("签名字符串 {}", encod_str);
            String sign = MD5Util.getMD5Str(encod_str);
            merchantParams.setSign(sign);
            JSONObject jsonObject = xMerchantService.query(merchantParams);
            System.out.print(jsonObject);
        }
    }
}
