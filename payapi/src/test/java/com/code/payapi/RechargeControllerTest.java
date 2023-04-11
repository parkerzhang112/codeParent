package com.code.payapi;

import com.alibaba.fastjson.JSONObject;
import com.code.baseservice.base.exception.BaseException;
import com.code.baseservice.dto.payapi.RechareParams;
import com.code.baseservice.entity.ZfMerchant;
import com.code.baseservice.service.ZfMerchantService;
import com.code.baseservice.service.ZfRechargeService;
import com.code.baseservice.util.CommonUtil;
import com.code.baseservice.util.MD5Util;
import com.code.baseservice.util.StringUtil;
import com.code.baseservice.util.Telegram;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.*;

@Slf4j
public class RechargeControllerTest extends PayapiApplicationTests {

    @Autowired
    ZfRechargeService zfRechargeService;

    @Autowired
    ZfMerchantService zfMerchantService;

    @Test
    public void testCreate() {
        ZfMerchant xMerchant = zfMerchantService.queryById(10019);
        RechareParams rechareParams = new RechareParams();
        rechareParams.setMerchant_order_no(StringUtil.createRandomStr1(20));
        rechareParams.setMerchant_id(10019);
        Random random = new Random();
        int amount = random.nextInt(106);
        rechareParams.setPay_amount(new BigDecimal("123"));
        rechareParams.setNotify_url("http://127.0.0.1:8081/test/notify");
        rechareParams.setRemark(StringUtil.createRandomStr1(3));
        TreeMap<String, Object> map = new TreeMap<>();
        map.put("merchant_id", rechareParams.getMerchant_id());
        map.put("merchant_order_no", rechareParams.getMerchant_order_no());
        map.put("pay_amount", rechareParams.getPay_amount());
        map.put("notify_url", rechareParams.getNotify_url());
        String sign_str = new CommonUtil().getSign(map);
        sign_str = sign_str.concat("key=".concat(xMerchant.getKey()));
        String sign =  MD5Util.getMD5Str(sign_str).toUpperCase();
        rechareParams.setSign(sign);
        try {
            JSONObject jsonObject = zfRechargeService.create(rechareParams);
            System.out.print("创建订单测试单元结果" + jsonObject);
        }catch (BaseException e){
            throw new RuntimeException(e);
        }

    }

}
