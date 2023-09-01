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
    public void testCreate() throws InterruptedException {
        Thread threadB = new Thread(new Runnable() {
            @Override
            public void run() {
                log.info("开始执行");
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
                log.info("还在执行");
                try {
                    JSONObject jsonObject = zfRechargeService.create(rechareParams);
                    Thread threadB1 = new Thread(() -> {
                        zfRechargeService.getOrderStatus(jsonObject.getString("order_no"));
                    }, "threadB1");
                    Thread threadC1 = new Thread(() -> {
                        zfRechargeService.getOrderStatus(jsonObject.getString("order_no"));
                    }, "threadC1");
                    Thread threadD1 = new Thread(() -> {
                        zfRechargeService.getOrderStatus(jsonObject.getString("order_no"));
                    }, "threadD1");
                    for (int j = 0; j < 4; j++) {
                        if(!threadB1.isAlive()) {
                            threadB1.start();
                        }
                        if(!threadC1.isAlive()) {
                            threadC1.start();

                        }
                        if(!threadD1.isAlive()) {
                            threadD1.start();
                        }
                    }
                    System.out.print("创建订单测试单元结果" + jsonObject);
                }catch (BaseException e){
                    throw new RuntimeException(e);
                }
            }
        }, "threadB");

        Thread threadC = new Thread(new Runnable() {
            @Override
            public void run() {
                log.info("开始执行");
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
                log.info("还在执行");
                try {
                    JSONObject jsonObject = zfRechargeService.create(rechareParams);
                    Thread threadB1 = new Thread(() -> {
                        zfRechargeService.getOrderStatus(jsonObject.getString("order_no"));
                    }, "threadB1");
                    Thread threadC1 = new Thread(() -> {
                        zfRechargeService.getOrderStatus(jsonObject.getString("order_no"));
                    }, "threadC1");
                    Thread threadD1 = new Thread(() -> {
                        zfRechargeService.getOrderStatus(jsonObject.getString("order_no"));
                    }, "threadD1");
                    for (int j = 0; j < 4; j++) {
                        if(!threadB1.isAlive()) {
                            threadB1.start();
                        }
                        if(!threadC1.isAlive()) {
                            threadC1.start();

                        }
                        if(!threadD1.isAlive()) {
                            threadD1.start();
                        }
                    }
                    System.out.print("创建订单测试单元结果" + jsonObject);
                }catch (BaseException e){
                    throw new RuntimeException(e);
                }
            }
        }, "threadB");

        Thread threadD = new Thread(new Runnable() {
            @Override
            public void run() {
                log.info("开始执行");
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
                log.info("还在执行");
                try {
                    JSONObject jsonObject = zfRechargeService.create(rechareParams);
                    Thread threadB1 = new Thread(() -> {
                        zfRechargeService.getOrderStatus(jsonObject.getString("order_no"));
                    }, "threadB1");
                    Thread threadC1 = new Thread(() -> {
                        zfRechargeService.getOrderStatus(jsonObject.getString("order_no"));
                    }, "threadC1");
                    Thread threadD1 = new Thread(() -> {
                        zfRechargeService.getOrderStatus(jsonObject.getString("order_no"));
                    }, "threadD1");
                    for (int j = 0; j < 4; j++) {
                        if(!threadB1.isAlive()) {
                            threadB1.start();
                        }
                        if(!threadC1.isAlive()) {
                            threadC1.start();

                        }
                        if(!threadD1.isAlive()) {
                            threadD1.start();
                        }
                    }
                    System.out.print("创建订单测试单元结果" + jsonObject);
                }catch (BaseException e){
                    throw new RuntimeException(e);
                }
            }
        }, "threadB");

        Thread threadE = new Thread(new Runnable() {
            @Override
            public void run() {
                log.info("开始执行");
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
                log.info("还在执行");
                try {
                    JSONObject jsonObject = zfRechargeService.create(rechareParams);
                    Thread threadB1 = new Thread(() -> {
                        zfRechargeService.getOrderStatus(jsonObject.getString("order_no"));
                    }, "threadB1");
                    Thread threadC1 = new Thread(() -> {
                        zfRechargeService.getOrderStatus(jsonObject.getString("order_no"));
                    }, "threadC1");
                    Thread threadD1 = new Thread(() -> {
                        zfRechargeService.getOrderStatus(jsonObject.getString("order_no"));
                    }, "threadD1");
                    for (int j = 0; j < 4; j++) {
                        if(!threadB1.isAlive()) {
                            threadB1.start();
                        }
                        if(!threadC1.isAlive()) {
                            threadC1.start();

                        }
                        if(!threadD1.isAlive()) {
                            threadD1.start();
                        }
                    }
                    System.out.print("创建订单测试单元结果" + jsonObject);
                }catch (BaseException e){
                    throw new RuntimeException(e);
                }
            }
        }, "threadB");

        Thread threadF = new Thread(new Runnable() {
            @Override
            public void run() {
                log.info("开始执行");
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
                log.info("还在执行");
                try {
                    JSONObject jsonObject = zfRechargeService.create(rechareParams);
                    Thread threadB1 = new Thread(() -> {
                        zfRechargeService.getOrderStatus(jsonObject.getString("order_no"));
                    }, "threadB1");
                    Thread threadC1 = new Thread(() -> {
                        zfRechargeService.getOrderStatus(jsonObject.getString("order_no"));
                    }, "threadC1");
                    Thread threadD1 = new Thread(() -> {
                        zfRechargeService.getOrderStatus(jsonObject.getString("order_no"));
                    }, "threadD1");
                    for (int j = 0; j < 4; j++) {
                        if(!threadB1.isAlive()) {
                            threadB1.start();
                        }
                        if(!threadC1.isAlive()) {
                            threadC1.start();

                        }
                        if(!threadD1.isAlive()) {
                            threadD1.start();
                        }
                    }
                    System.out.print("创建订单测试单元结果" + jsonObject);
                }catch (BaseException e){
                    throw new RuntimeException(e);
                }
            }
        }, "threadB");

        Thread threadG = new Thread(new Runnable() {
            @Override
            public void run() {
                log.info("开始执行");
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
                log.info("还在执行");
                try {
                    JSONObject jsonObject = zfRechargeService.create(rechareParams);
                    Thread threadB1 = new Thread(() -> {
                        zfRechargeService.getOrderStatus(jsonObject.getString("order_no"));
                    }, "threadB1");
                    Thread threadC1 = new Thread(() -> {
                        zfRechargeService.getOrderStatus(jsonObject.getString("order_no"));
                    }, "threadC1");
                    Thread threadD1 = new Thread(() -> {
                        zfRechargeService.getOrderStatus(jsonObject.getString("order_no"));
                    }, "threadD1");
                    for (int j = 0; j < 4; j++) {
                        if(!threadB1.isAlive()) {
                            threadB1.start();
                        }
                        if(!threadC1.isAlive()) {
                            threadC1.start();

                        }
                        if(!threadD1.isAlive()) {
                            threadD1.start();
                        }
                    }
                    System.out.print("创建订单测试单元结果" + jsonObject);
                }catch (BaseException e){
                    throw new RuntimeException(e);
                }
            }
        }, "threadB");

        Thread threadH = new Thread(new Runnable() {
            @Override
            public void run() {
                log.info("开始执行");
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
                log.info("还在执行");
                try {
                    JSONObject jsonObject = zfRechargeService.create(rechareParams);
                    Thread threadB1 = new Thread(() -> {
                        zfRechargeService.getOrderStatus(jsonObject.getString("order_no"));
                    }, "threadB1");
                    Thread threadC1 = new Thread(() -> {
                        zfRechargeService.getOrderStatus(jsonObject.getString("order_no"));
                    }, "threadC1");
                    Thread threadD1 = new Thread(() -> {
                        zfRechargeService.getOrderStatus(jsonObject.getString("order_no"));
                    }, "threadD1");
                    for (int j = 0; j < 4; j++) {
                        if(!threadB1.isAlive()) {
                            threadB1.start();
                        }
                        if(!threadC1.isAlive()) {
                            threadC1.start();

                        }
                        if(!threadD1.isAlive()) {
                            threadD1.start();
                        }
                    }
                    System.out.print("创建订单测试单元结果" + jsonObject);
                }catch (BaseException e){
                    throw new RuntimeException(e);
                }
            }
        }, "threadB");

        Thread threadI = new Thread(new Runnable() {
            @Override
            public void run() {
                log.info("开始执行");
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
                log.info("还在执行");
                try {
                    JSONObject jsonObject = zfRechargeService.create(rechareParams);
                    Thread threadB1 = new Thread(() -> {
                        zfRechargeService.getOrderStatus(jsonObject.getString("order_no"));
                    }, "threadB1");
                    Thread threadC1 = new Thread(() -> {
                        zfRechargeService.getOrderStatus(jsonObject.getString("order_no"));
                    }, "threadC1");
                    Thread threadD1 = new Thread(() -> {
                        zfRechargeService.getOrderStatus(jsonObject.getString("order_no"));
                    }, "threadD1");
                    for (int j = 0; j < 4; j++) {
                        if(!threadB1.isAlive()) {
                            threadB1.start();
                        }
                        if(!threadC1.isAlive()) {
                            threadC1.start();

                        }
                        if(!threadD1.isAlive()) {
                            threadD1.start();
                        }
                    }
                    System.out.print("创建订单测试单元结果" + jsonObject);
                }catch (BaseException e){
                    throw new RuntimeException(e);
                }
            }
        }, "threadB");

        for (int i = 0; i < 20; i++) {
            if(!threadB.isAlive()){
                threadB.run();
            }
            if(!threadC.isAlive()){
                threadC.run();
            }
            if(!threadD.isAlive()){
                threadD.run();
            }
            if(!threadE.isAlive()){
                threadE.run();
            }
            if(!threadF.isAlive()){
                threadF.run();
            }
            if(!threadG.isAlive()){
                threadG.run();
            }
            if(!threadH.isAlive()){
                threadI.run();
            }
        }
        Thread.sleep(10000000);
    }

    @Test
    public void testCreateHttp() {
        ZfMerchant xMerchant = zfMerchantService.queryById(10019);
        RechareParams rechareParams = new RechareParams();
        rechareParams.setMerchant_order_no(StringUtil.createRandomStr1(20));
        rechareParams.setMerchant_id(871245555);
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
        sign_str = sign_str.concat("key=skkfzzJEfsNuwHjt1l4KZELE04YTixbo");
        log.info("签名字符串: {}", sign_str);
        String sign =  MD5Util.getMD5Str(sign_str).toUpperCase();
        rechareParams.setSign(sign);
        try {
            String reponse = HttpClientUtil.doPostJson("http://qw520.top/recharge/create", JSONObject.toJSONString(rechareParams));
            JSONObject jsonObject = JSONObject.parseObject(reponse);
            HttpClientUtil.doGet("http://dfzf.top/recharge/order/getOrder/" +jsonObject.getJSONObject("data").getString("order_no") );
            System.out.print("创建订单测试单元结果" + jsonObject);
        }catch (BaseException e){
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testViewHttp(){

        TreeMap<String, Object> map = new TreeMap<>();
        map.put("merchant_id", "11168");
        map.put("merchant_order_no", "202308310947400969VQY");
        String sign_str = new CommonUtil().getSign(map);
        sign_str = sign_str.concat("key=".concat("GrSwUefNhjJZf9KBM6jxqu5CNbLVNWZg"));
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
