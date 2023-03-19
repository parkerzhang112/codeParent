package com.code.payapi;

import com.alibaba.fastjson.JSONObject;
import com.xiagao.baseservice.base.exception.BaseException;
import com.xiagao.baseservice.dto.api.QueryParams;
import com.xiagao.baseservice.dto.api.RechareParams;
import com.xiagao.baseservice.entity.XChannel;
import com.xiagao.baseservice.entity.XMerchant;
import com.xiagao.baseservice.entity.XRecharge;
import com.xiagao.baseservice.service.XChannelService;
import com.xiagao.baseservice.service.XMerchantService;
import com.xiagao.baseservice.service.XRechargeService;
import com.xiagao.baseservice.util.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.*;

@Slf4j
public class RechargeControllerTest extends PayapiApplicationTests {

    @Autowired
    XRechargeService xRechargeService;

    @Autowired
    XMerchantService xMerchantService;

    @Autowired
    XChannelService xChannelService;

    @Test
    public void testCreate() {
        XMerchant xMerchant = xMerchantService.selectOne(10019);
        RechareParams rechareParams = new RechareParams();
        rechareParams.setMerchant_order_no(StringUtil.createRandomStr1(20));
        rechareParams.setMerchant_id(10019);
        Random random = new Random();
        int amount = random.nextInt(100);
        rechareParams.setPay_amount(new BigDecimal("1082"));
        rechareParams.setName("张三");
        rechareParams.setCard_type("ICBC");
        rechareParams.setNotify_url("http://127.0.0.1:8081/test/notify");
        rechareParams.setTrans_type(1);
        rechareParams.setRemark(StringUtil.createRandomStr1(3));
        String encod_str = "merchant_order_no=" + rechareParams.getMerchant_order_no() + "&merchant_id=" + rechareParams.getMerchant_id()
                + "&pay_amount=" + rechareParams.getPay_amount() + "&name=" + rechareParams.getName()
                + "&notify_url=" + rechareParams.getNotify_url()
                + "&card_type=" + rechareParams.getCard_type() + "&key=" + xMerchant.getKey();
        log.info("签名字符串 {}", encod_str);
        String sign = MD5Util.getMD5Str(encod_str);
        rechareParams.setSign(sign);
        try {
            JSONObject jsonObject = xRechargeService.create(rechareParams);
            System.out.print("创建订单测试单元结果" + jsonObject);
        }catch (BaseException e){
            Telegram telegram = new Telegram();
//            telegram.sendWarrnRechargeMessage(rechareParams, e.getMessage());
            throw new RuntimeException(e);
        }

    }

    @Test
    public void testCreateHttp() {
        XMerchant xMerchant = xMerchantService.selectOne(3);
        RechareParams rechareParams = new RechareParams();
        rechareParams.setMerchant_order_no(StringUtil.createRandomStr1(20));
        rechareParams.setMerchant_id(20005);
        Random random = new Random();
        int amount = random.nextInt(10);
        rechareParams.setPay_amount(new BigDecimal(amount+2000));
        rechareParams.setName("许明方");
        rechareParams.setCard_type("ICBC");
        rechareParams.setNotify_url("http://127.0.0.1:8081/test/notify");
        rechareParams.setTrans_type(1);
        rechareParams.setRemark(StringUtil.createRandomStr1(3));
        String encod_str = "merchant_order_no=" + rechareParams.getMerchant_order_no() + "&merchant_id=" + rechareParams.getMerchant_id()
                + "&pay_amount=" + rechareParams.getPay_amount() + "&name=" + rechareParams.getName()
                + "&notify_url=" + rechareParams.getNotify_url()
                + "&card_type=" + rechareParams.getCard_type() + "&key=oN7gWt38n1uQwYeKQkl1jkNdpN2DdflC" ;
        log.info("签名字符串 {}", encod_str);
        String sign = MD5Util.getMD5Str(encod_str);
        rechareParams.setSign(sign);
        String r = HttpClientUtil.doPostJson("http://dahuilang.top/recharge/create", JSONObject.toJSONString(rechareParams));
        System.out.print("请求测试结果" + r);
    }

    @Test
    public void testxshCreateHttp() {
        XMerchant xMerchant = xMerchantService.selectOne(3);
        Map<String,String> map = new HashMap();
        map.put("xsh_order_no",StringUtil.createRandomStr1(20));
        map.put("merchant_id","234532");
        Random random = new Random();
        int amount = random.nextInt(10);
        map.put("xsh_amount", new BigDecimal(amount+2000).toString());
        map.put("xsh_name","许明方");
        map.put("xsh_bank_type","ICBC");
        map.put("xsh_notify_url","http://127.0.0.1:8081/test/notify");
//        map.put("xsh_trans_type","1");
        map.put("remark",StringUtil.createRandomStr1(3));
        String encod_str = "xsh_order_no=" + map.get("xsh_order_no") + "&merchant_id=" + map.get("merchant_id")
                + "&xsh_amount=" + map.get("xsh_amount") + "&xsh_name=" + map.get("xsh_name")
                + "&xsh_notify_url=" + map.get("xsh_notify_url")
                + "&xsh_bank_type=" + map.get("xsh_bank_type") + "&xsh_key=dy2NGFJS2OsHH1skNTf1FDaXHXqgR5xH" ;
        log.info("签名字符串 {}", encod_str);
        String sign = MD5Util.getMD5Str(encod_str);
        map.put("xsh_sign",sign);
        String r = HttpClientUtil.doPostJson("http://xshdd.cn/recharge/create", JSONObject.toJSONString(map));
        System.out.print("请求测试结果" + r);
    }

    @Test
    public void testfntCreateHttp() {
        XMerchant xMerchant = xMerchantService.selectOne(3);
        Map<String,String> map = new HashMap();
        map.put("order_number",StringUtil.createRandomStr1(20));
        map.put("merchant_id","10019");
        Random random = new Random();
        int amount = random.nextInt(10);
        map.put("order_amount", new BigDecimal(amount+2000).toString());
        map.put("pay_name","许明方");
        map.put("order_notify_url","http://127.0.0.1:8081/test/notify");
        String encod_str = "merchant_id=" + map.get("merchant_id")
                + "&order_number=" + map.get("order_number")
                + "&order_amount=" + map.get("order_amount")
                + "&order_notify_url=" + map.get("order_notify_url")
                + "&pay_name=" + map.get("pay_name")
                + "&pay_key=oIZOk7E9hcCRxzd2gAk52VUsIhSzlmIV" ;
        log.info("签名字符串 {}", encod_str);
        String sign = MD5Util.getMD5Str(encod_str);
        map.put("xsh_sign",sign);
        String r = HttpClientUtil.doPostJson("http://127.0.0.1:8081/recharge/create", JSONObject.toJSONString(map));
        System.out.print("请求测试结果" + r);
    }



    @Test
    public void testbjyCreateHttp() {
        XMerchant xMerchant = xMerchantService.selectOne(3);
        TreeMap<String,Object> map = new TreeMap();
        map.put("order",StringUtil.createRandomStr1(20));
        map.put("m_id","10019");
        Random random = new Random();
        int amount = random.nextInt(10);
        map.put("pay_amount", new BigDecimal(amount+2000).toString());
        map.put("pay_name","许明方");
        map.put("notify_url","http://127.0.0.1:8081/test/notify");
        String sign_str = new CommonUtil().getSign(map);
        sign_str = sign_str.concat("key=oIZOk7E9hcCRxzd2gAk52VUsIhSzlmIV");
        String sign =  MD5Util.getMD5Str(sign_str.toUpperCase());
        log.info("签名字符串 {}", sign_str);
        map.put("sign",sign);
        log.info("请求参数 {}", JSONObject.toJSONString(map));
        String r = HttpClientUtil.doPostJson("http://127.0.0.1:8081/recharge/create", JSONObject.toJSONString(map));
        System.out.print("请求测试结果" + r);
    }

    @Test
    public void testbDCreateHttp() {
        XMerchant xMerchant = xMerchantService.selectOne(3);
        TreeMap<String,Object> map = new TreeMap();
        map.put("order_no",StringUtil.createRandomStr1(20));
        map.put("mcc","5988551");
        Random random = new Random();
        int amount = random.nextInt(10);
        map.put("order_amount", new BigDecimal(amount+2000).toString());
        map.put("pay_name","许明方");
        map.put("notify_url","http://127.0.0.1:8081/test/notify");
        String sign_str = new CommonUtil().getSign(map);
        sign_str = sign_str.concat("key=nBzhobiroj1YZGZCkPn3CLBMntaTGoHC");
        String sign =  MD5Util.getMD5Str(sign_str).toUpperCase();
        log.info("签名字符串 {}", sign_str);
        map.put("sign",sign);
        log.info("请求参数 {}", JSONObject.toJSONString(map));
        String r = HttpClientUtil.doPostJson("http://api.hkbandao.cn//deposit/create", JSONObject.toJSONString(map));
        System.out.print("请求测试结果" + r);
    }

    @Test
    public void testcJCreateHttp() {
        XMerchant xMerchant = xMerchantService.selectOne(3);
        TreeMap<String,Object> map = new TreeMap();
        map.put("order_no",StringUtil.createRandomStr1(20));
        map.put("mcc_id","90001");
        Random random = new Random();
        int amount = random.nextInt(10);
        map.put("order_pay_amount", new BigDecimal(amount+2000).toString());
        map.put("pay_name","许明方");
        map.put("notify_url","http://127.0.0.1:8081/test/notify");
        String sign_str = new CommonUtil().getSign(map);
        sign_str = sign_str.concat("key=KfknMMtQPU2EYzaKtIbxPfX1B7ejwUVQ");
        String sign =  MD5Util.getMD5Str(sign_str).toUpperCase();
        log.info("签名字符串 {}", sign_str);
        map.put("sign",sign);
        log.info("请求参数 {}", JSONObject.toJSONString(map));
        String r = HttpClientUtil.doPostJson("http://inter.cj8678a.cn/recharge/create", JSONObject.toJSONString(map));
        System.out.print("请求测试结果" + r);
    }

    @Test
    public void testCreateHttp2() {
        XMerchant xMerchant = xMerchantService.selectOne(3);
        JSONObject rechareParams = new JSONObject();
        rechareParams.put("m_order_no",StringUtil.createRandomStr1(20));
        rechareParams.put("m_id",10019);
        Random random = new Random();
        int amount = random.nextInt(10);
        rechareParams.put("amount",new BigDecimal(amount+100));
        rechareParams.put("name","许明方");
        rechareParams.put("bank_type","ICBC");
        rechareParams.put("notify_url","http://127.0.0.1:8081/test/notify");
        rechareParams.put("t_type",1);
        rechareParams.put("name",StringUtil.createRandomStr1(3));
        String encod_str = "merchant_order_no=" + rechareParams.get("m_order_no") + "&merchant_id=" + rechareParams.get("m_id")
                + "&pay_amount=" + rechareParams.get("amount") + "&name=" + rechareParams.get("name")
                + "&notify_url=" + rechareParams.get("notify_url")
                + "&card_type=" + rechareParams.get("bank_type") + "&key=oIZOk7E9hcCRxzd2gAk52VUsIhSzlmIV" ;
        log.info("签名字符串 {}", encod_str);
        String sign = MD5Util.getMD5Str(encod_str);
        rechareParams.put("sign",sign);
        String r = HttpClientUtil.doPostJson("http://127.0.0.1:8081/recharge/create", JSONObject.toJSONString(rechareParams));
        System.out.print("请求测试结果" + r);
    }


    @Test
    public void testuCreate() {
        XMerchant xMerchant = xMerchantService.selectOne(3);
        RechareParams rechareParams = new RechareParams();
        rechareParams.setMerchant_order_no(StringUtil.createRandomStr1(20));
        rechareParams.setMerchant_id(3);
        Random random = new Random();
        int amount = random.nextInt(100);
        rechareParams.setPay_amount(new BigDecimal(amount + 999));
        rechareParams.setName("张三");
        rechareParams.setCard_type("ICBC");
        rechareParams.setNotify_url("http://127.0.0.1:8081/test/notify");
        rechareParams.setTrans_type(2);
        rechareParams.setRemark(StringUtil.createRandomStr1(3));
        String encod_str = "merchant_order_no=" + rechareParams.getMerchant_order_no() + "&merchant_id=" + rechareParams.getMerchant_id()
                + "&pay_amount=" + rechareParams.getPay_amount() + "&name=" + rechareParams.getName()
                + "&notify_url=" + rechareParams.getNotify_url()
                + "&card_type=" + rechareParams.getCard_type() + "&key=" + xMerchant.getKey();
        log.info("签名字符串 {}", encod_str);
        String sign = MD5Util.getMD5Str(encod_str);
        rechareParams.setSign(sign);
        JSONObject jsonObject = xRechargeService.create(rechareParams);
        System.out.print("创建订单测试单元结果" + jsonObject);
    }

    @Test
    public void testChannelFee() {
        XChannel xChannel = xChannelService.queryById(3);
        BigDecimal fee = xChannelService.sumChannelFee(new BigDecimal("10000"), xChannel);
        System.out.print(fee);
    }

    @Test
    public void testView() {
        QueryParams queryParams = new QueryParams();
        List<XRecharge> xRecharges = xRechargeService.queryAllByLimit(10, 1);
        if (xRecharges.size() > 0) {
            XMerchant xMerchant = xMerchantService.selectOne(xRecharges.get(0).getMerchantId());
            queryParams.setMerchant_order_no(xRecharges.get(0).getMerchantOrderNo());
            queryParams.setMerchant_id(xRecharges.get(0).getMerchantId());
            String encod_str = "merchant_order_no=" + queryParams.getMerchant_order_no() + "&merchant_id=" + queryParams.getMerchant_id()
                    + "&key=" + xMerchant.getKey();
            log.info("签名字符串 {}", encod_str);
            String sign = MD5Util.getMD5Str(encod_str);
            queryParams.setSign(sign);
            JSONObject jsonObject = xRechargeService.query(queryParams);
            System.out.print("查询订单测试单元" + jsonObject);
        }

    }

    @Test
    public void testHttpViewCreate() {
        QueryParams queryParams = new QueryParams();
        HashMap<String,Object> map = new HashMap();
        List<XRecharge> xRecharges = xRechargeService.queryAllByLimit(0, 1);

        map.put("merchant_order_no", "202303111329202297KDX");
        map.put("merchant_id", "563574");
        String encod_str = "merchant_order_no=" + map.get("merchant_order_no") + "&merchant_id=" + map.get("merchant_id")
                + "&key=QZLmlnFasYhmOQpioUolegXnQLQLMQkn";
        String sign =  MD5Util.getMD5Str(encod_str);
        map.put("sign", sign);
        log.info("签名字符串 {}", encod_str);
        queryParams.setSign(sign);
        log.info("请求参数 {}", JSONObject.toJSONString(map));
        String r = HttpClientUtil.doPostJson("http://fnt88.cn/pay/view", JSONObject.toJSONString(map));
        System.out.println(r);
    }

    @Test
    public void testbDHttpViewCreate() {
        QueryParams queryParams = new QueryParams();
        XMerchant xMerchant = xMerchantService.selectOne(3);
        TreeMap<String,Object> map = new TreeMap();
        List<XRecharge> xRecharges = xRechargeService.queryAllByLimit(0, 1);

        map.put("mcc", "5988551");
        map.put("order_no", "20230317095323127KWE3");
        String sign_str = new CommonUtil().getSign(map);
        sign_str = sign_str.concat("key=nBzhobiroj1YZGZCkPn3CLBMntaTGoHC");
        String sign =  MD5Util.getMD5Str(sign_str).toUpperCase();
        map.put("sign", sign);
        log.info("签名字符串 {}", sign_str);
        queryParams.setSign(sign);
        log.info("请求参数 {}", JSONObject.toJSONString(map));
        String r = HttpClientUtil.doPostJson("http://api.hkbandao.cn/recharge/view", JSONObject.toJSONString(map));
//        JSONObject jsonObject = xTransferService.create(transParams);
        System.out.println(r);
    }

    @Test
    public void testcJHttpViewCreate() {
        QueryParams queryParams = new QueryParams();
        TreeMap<String,Object> map = new TreeMap();
        List<XRecharge> xRecharges = xRechargeService.queryAllByLimit(0, 1);

        map.put("mcc_id", "90001");
        map.put("order_no", "20230315140626585ND6R");
        String sign_str = new CommonUtil().getSign(map);
        sign_str = sign_str.concat("key=KfknMMtQPU2EYzaKtIbxPfX1B7ejwUVQ");
        String sign =  MD5Util.getMD5Str(sign_str).toUpperCase();
        map.put("sign", sign);
        log.info("签名字符串 {}", sign_str);
        queryParams.setSign(sign);
        log.info("请求参数 {}", JSONObject.toJSONString(map));
        String r = HttpClientUtil.doPostJson("http://inter.cj8678a.cn/cj/view", JSONObject.toJSONString(map));
//        JSONObject jsonObject = xTransferService.create(transParams);
        System.out.println(r);
    }


}
