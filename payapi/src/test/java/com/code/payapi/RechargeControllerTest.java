package com.code.payapi;

import com.alibaba.fastjson.JSONObject;
import com.code.baseservice.base.exception.BaseException;
import com.code.baseservice.dto.payapi.QueryParams;
import com.code.baseservice.dto.payapi.RechareParams;
import com.code.baseservice.entity.ZfMerchant;
import com.code.baseservice.entity.ZfRecharge;
import com.code.baseservice.service.ZfMerchantService;
import com.code.baseservice.service.ZfRechargeService;
import com.code.baseservice.util.*;
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
        int amount = random.nextInt(107);
        rechareParams.setPay_amount(new BigDecimal("146"));
        rechareParams.setNotify_url("http://127.0.0.1:8081/test/notify");
        rechareParams.setRemark(StringUtil.createRandomStr1(3));
        TreeMap<String, Object> map = new TreeMap<>();
        map.put("merchant_id", rechareParams.getMerchant_id());
        map.put("merchant_order_no", rechareParams.getMerchant_order_no());
        map.put("pay_amount", rechareParams.getPay_amount());
        map.put("notify_url", rechareParams.getNotify_url());
        String sign_str = new CommonUtil().getSign(map);
        sign_str = sign_str.concat("key=".concat(xMerchant.getKey()));
        log.info("签名字符串: {}", sign_str);
        String sign =  MD5Util.getMD5Str(sign_str).toUpperCase();
        rechareParams.setSign(sign);
        try {
            JSONObject jsonObject = zfRechargeService.create(rechareParams);
            zfRechargeService.getOrderStatus(jsonObject.getString("order_no"));
            System.out.print("创建订单测试单元结果" + jsonObject);
        }catch (BaseException e){
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testCreateHttp() {
        ZfMerchant xMerchant = zfMerchantService.queryById(10019);
        RechareParams rechareParams = new RechareParams();
        rechareParams.setMerchant_order_no(StringUtil.createRandomStr1(20));
        rechareParams.setMerchant_id(768834);
        Random random = new Random();
        int amount = random.nextInt(107);
        rechareParams.setPay_amount(new BigDecimal("146"));
        rechareParams.setNotify_url("http://127.0.0.1:8081/test/notify");
        rechareParams.setRemark(StringUtil.createRandomStr1(3));
        TreeMap<String, Object> map = new TreeMap<>();
        map.put("merchant_id", rechareParams.getMerchant_id());
        map.put("merchant_order_no", rechareParams.getMerchant_order_no());
        map.put("pay_amount", rechareParams.getPay_amount());
        map.put("notify_url", rechareParams.getNotify_url());
        String sign_str = new CommonUtil().getSign(map);
        sign_str = sign_str.concat("key=LEYVGR4LCsKVDTcURpprTS3zm5mjkpYK");
        log.info("签名字符串: {}", sign_str);
        String sign =  MD5Util.getMD5Str(sign_str).toUpperCase();
        rechareParams.setSign(sign);
        try {
            String reponse = HttpClientUtil.doPostJson("http://qw520.top/recharge/create", JSONObject.toJSONString(rechareParams));
            JSONObject jsonObject = JSONObject.parseObject(reponse);
            HttpClientUtil.doGet("http://127.0.0.1:8081/recharge/order/getOrder/" +jsonObject.getJSONObject("data").getString("order_no") );
            System.out.print("创建订单测试单元结果" + jsonObject);
        }catch (BaseException e){
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testViewHttp(){

        TreeMap<String, Object> map = new TreeMap<>();
        map.put("merchant_id", "768834");
        map.put("merchant_order_no", "202304191318096284XJO");
        String sign_str = new CommonUtil().getSign(map);
        sign_str = sign_str.concat("key=".concat("LEYVGR4LCsKVDTcURpprTS3zm5mjkpYK"));
        String sign =  MD5Util.getMD5Str(sign_str).toUpperCase();
        map.put("sign",sign);
        log.info("请求参数 {}", JSONObject.toJSON(map));
        String reponse = HttpClientUtil.doPostJson("http://qw520.top/recharge/view", JSONObject.toJSONString(map));
        System.out.print("查询订单测试单元" + reponse);
    }

    @Test
    public void testView(){
        QueryParams queryParams = new QueryParams();
        ZfRecharge xRecharges = zfRechargeService.queryById("DDD1681388713213MWllk8DexsBDrpsA");
        ZfMerchant xMerchant = zfMerchantService.queryById(xRecharges.getMerchantId());
        queryParams.setMerchant_order_no(xRecharges.getMerchantOrderNo());
        queryParams.setMerchant_Id(xRecharges.getMerchantId());

        TreeMap<String, Object> map = new TreeMap<>();
        map.put("merchant_id", queryParams.getMerchant_Id());
        map.put("merchant_order_no", queryParams.getMerchant_order_no());
        String sign_str = new CommonUtil().getSign(map);
        sign_str = sign_str.concat("key=".concat(xMerchant.getKey()));
        String sign =  MD5Util.getMD5Str(sign_str).toUpperCase();
        queryParams.setSign(sign);
        log.info("请求参数 {}", JSONObject.toJSON(queryParams));
        JSONObject jsonObject = zfRechargeService.query(queryParams);
        System.out.print("查询订单测试单元" + jsonObject);
    }

}
