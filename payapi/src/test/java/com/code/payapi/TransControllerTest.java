package com.code.payapi;

import com.alibaba.fastjson.JSONObject;
import com.xiagao.baseservice.base.constant.ResultEnum;
import com.xiagao.baseservice.base.exception.BaseException;
import com.xiagao.baseservice.dto.ResponseResult;
import com.xiagao.baseservice.dto.api.QueryParams;
import com.xiagao.baseservice.dto.api.TransferParams;
import com.xiagao.baseservice.entity.XMerchant;
import com.xiagao.baseservice.entity.XTransfer;
import com.xiagao.baseservice.service.XMerchantService;
import com.xiagao.baseservice.service.XTransferService;
import com.xiagao.baseservice.util.CommonUtil;
import com.xiagao.baseservice.util.HttpClientUtil;
import com.xiagao.baseservice.util.MD5Util;
import com.xiagao.baseservice.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;
import java.util.TreeMap;

@Slf4j
public class TransControllerTest extends PayapiApplicationTests {

    @Autowired
    XTransferService xTransferService;

    @Autowired
    XMerchantService xMerchantService;

    @Test
    public void testCreate() {
        TransferParams transParams = new TransferParams();
        XMerchant xMerchant = xMerchantService.selectOne(10019);
        transParams.setOrder_type(1);
        transParams.setCard_account(StringUtil.createRandomStr1(10));
        transParams.setCard_name(StringUtil.createRandomStr1(3));
        transParams.setCard_address(StringUtil.createRandomStr1(10));
        transParams.setMerchant_order_no(StringUtil.createRandomStr1(29));
        transParams.setMerchant_id(10019);
        transParams.setCard_type("ICBC");
        transParams.setNotify_url("http://127.0.0.1");
        int amount = new Random().nextInt(100) + 20000;
        transParams.setPay_amount(new BigDecimal(amount));
        transParams.setRemark(StringUtil.createRandomStr1(3));
        String encod_str = "merchant_order_no=" + transParams.getMerchant_order_no() + "&merchant_id=" + transParams.getMerchant_id()
                + "&pay_amount=" + transParams.getPay_amount() + "&card_name=" + transParams.getCard_name()
                + "&notify_url=" + transParams.getNotify_url()
                + "&card_type=" + transParams.getCard_type()
                + "&card_account=" + transParams.getCard_account()
                + "&card_address=" + transParams.getCard_address()
                + "&key=" + xMerchant.getKey();
        log.info("签名字符串 {}", encod_str);
        String sign = MD5Util.getMD5Str(encod_str);
        transParams.setSign(sign);
        ResponseResult responseResult = new ResponseResult();
        try {
            JSONObject jsonObject = xTransferService.create(transParams);

            responseResult.setData(jsonObject);
        }catch (BaseException e){
            responseResult.setCode(e.getCode()).setMsg(e.getMessage());
        }catch (Exception e){
            log.error("系统异常", e);
            responseResult.setCode(ResultEnum.ERROR.getCode()).setMsg("系统异常");
        }
        System.out.println(responseResult.toJsonString());
    }

    @Test
    public void testHttpCreate() {
        TransferParams transParams = new TransferParams();
        XMerchant xMerchant = xMerchantService.selectOne(3);
        transParams.setOrder_type(0);
        transParams.setCard_account("123456789123456089");
        transParams.setWithdrawQueryUrl("http://txdev-third.9uymfl.com/thirdwithdraw/withdraw/verify");
        transParams.setCallToken("dasdasd");
        transParams.setCard_name("何锦府");
        transParams.setCard_address(StringUtil.createRandomStr1(10));
        transParams.setMerchant_order_no(StringUtil.createRandomStr1(29));
        transParams.setMerchant_id(5988552);
        transParams.setCard_type("ABC");
        transParams.setNotify_url("http://127.0.0.1");
        int amount = new Random().nextInt(100);
        transParams.setPay_amount(new BigDecimal(2000));
//        transParams.setRemark(StringUtil.createRandomStr1(3));
        String encod_str = "merchant_order_no=" + transParams.getMerchant_order_no() + "&merchant_id=" + transParams.getMerchant_id()
                + "&pay_amount=" + transParams.getPay_amount() + "&card_name=" + transParams.getCard_name()
                + "&notify_url=" + transParams.getNotify_url()
                + "&card_type=" + transParams.getCard_type()
                + "&card_account=" + transParams.getCard_account()
                + "&card_address=" + transParams.getCard_address()
                + "&key=yPxa354qFdoFDCE26GxmebMKwSlKaKbg";


        log.info("签名字符串 {}", encod_str);
        String sign = MD5Util.getMD5Str(encod_str);
        transParams.setSign(sign);
        String r = HttpClientUtil.doPostJson("http://cs77888.top/trans/create", JSONObject.toJSONString(transParams));
//        JSONObject jsonObject = xTransferService.create(transParams);
        System.out.println(r);
    }


    @Test
    public void testXshHttpCreate() {
        TransferParams transParams = new TransferParams();
        XMerchant xMerchant = xMerchantService.selectOne(3);
        transParams.setOrder_type(1);
        transParams.setCard_account("123456789123456089");
        transParams.setWithdrawQueryUrl("http://txdev-third.9uymfl.com/thirdwithdraw/withdraw/verify");
        transParams.setCallToken("dasdasd");
        transParams.setCard_name("何锦府");
        transParams.setCard_address(StringUtil.createRandomStr1(10));
        transParams.setMerchant_order_no(StringUtil.createRandomStr1(29));
        transParams.setMerchant_id(10019);
        transParams.setCard_type("ABC");
        transParams.setNotify_url("http://127.0.0.1");
        int amount = new Random().nextInt(100);
        transParams.setPay_amount(new BigDecimal(2000));
//        transParams.setRemark(StringUtil.createRandomStr1(3));
        String encod_str = "xsh_order_no=" + transParams.getMerchant_order_no() + "&merchant_id=" + transParams.getMerchant_id()
                + "&xsh_amount=" + transParams.getPay_amount() + "&xsh_card_name=" + transParams.getCard_name()
                + "&xsh_notify_url=" + transParams.getNotify_url()
                + "&xsh_card_type=" + transParams.getCard_type()
                + "&xsh_card_account=" + transParams.getCard_account()
                + "&xsh_card_address=" + transParams.getCard_address()
                + "&xsh_key=oIZOk7E9hcCRxzd2gAk52VUsIhSzlmIV";


        log.info("签名字符串 {}", encod_str);
        String sign = MD5Util.getMD5Str(encod_str);
        transParams.setSign(sign);
        String r = HttpClientUtil.doPostJson("http://127.0.0.1:8081/trans/create", JSONObject.toJSONString(transParams));
//        JSONObject jsonObject = xTransferService.create(transParams);
        System.out.println(r);
    }

    @Test
    public void testFntHttpCreate() {
        TransferParams transParams = new TransferParams();
        XMerchant xMerchant = xMerchantService.selectOne(3);
        transParams.setCard_account("123456789123456089");
        transParams.setCard_name("何锦府");
        transParams.setCard_address(StringUtil.createRandomStr1(10));
        transParams.setMerchant_order_no(StringUtil.createRandomStr1(29));
        transParams.setMerchant_id(563574);
        transParams.setCard_type("ABC");
        transParams.setNotify_url("http://127.0.0.1");
        transParams.setPay_amount(new BigDecimal(2000));
//        transParams.setRemark(StringUtil.createRandomStr1(3));
        String encod_str = "address=" + transParams.getCard_address()
                + "&card_name=" + transParams.getCard_name()
                + "&card_number=" + transParams.getCard_account()
                + "&card_type=" + transParams.getCard_type()
                + "&merchant_id=" + transParams.getMerchant_id()
                + "&order_notify_url=" + transParams.getNotify_url()
                + "&order_number=" + transParams.getMerchant_order_no()
                + "&pay_amount=" + transParams.getPay_amount()
                + "&pay_key=QZLmlnFasYhmOQpioUolegXnQLQLMQkn";
        log.info("签名字符串 {}", encod_str);
        String sign = MD5Util.getMD5Str(encod_str);
        transParams.setSign(sign);
        String r = HttpClientUtil.doPostJson("http://fnt88.cn/trans/create", JSONObject.toJSONString(transParams));
//        JSONObject jsonObject = xTransferService.create(transParams);
        System.out.println(r);
    }

    @Test
    public void testbJyHttpCreate() {
        TransferParams transParams = new TransferParams();
        XMerchant xMerchant = xMerchantService.selectOne(3);
        TreeMap<String,Object> map = new TreeMap();
        map.put("address", "广东深圳");
        map.put("card_name", "何锦府");
        map.put("card_number", "123456789123456089");
        map.put("card_type", "工商银行");
        map.put("m_id", "10019");
        map.put("notify_url", "http://127.0.0.1");
        map.put("order", StringUtil.createRandomStr1(29));
        map.put("pay_amount", new BigDecimal(2000));
        String sign_str = new CommonUtil().getSign(map);
        sign_str = sign_str.concat("key=oIZOk7E9hcCRxzd2gAk52VUsIhSzlmIV");
        String sign =  MD5Util.getMD5Str(sign_str.toUpperCase());
        map.put("sign", sign);
        log.info("签名字符串 {}", sign_str);
        transParams.setSign(sign);
        log.info("请求参数 {}", JSONObject.toJSONString(map));
        String r = HttpClientUtil.doPostJson("http://127.0.0.1:8081/trans/create", JSONObject.toJSONString(map));
//        JSONObject jsonObject = xTransferService.create(transParams);
        System.out.println(r);
    }

    @Test
    public void testbDHttpCreate() {
        TransferParams transParams = new TransferParams();
        XMerchant xMerchant = xMerchantService.selectOne(3);
        TreeMap<String,Object> map = new TreeMap();
        map.put("bank_of_deposit", "广东深圳");
        map.put("account_hodler", "何锦府");
        map.put("card_number", "123456789123456089");
        map.put("bank", "工商银行");
        map.put("mcc", "10019");
        map.put("notify_url", "http://127.0.0.1");
        map.put("order_no", StringUtil.createRandomStr1(29));
        map.put("order_amount", new BigDecimal(2000));
        String sign_str = new CommonUtil().getSign(map);
        sign_str = sign_str.concat("key=oIZOk7E9hcCRxzd2gAk52VUsIhSzlmIV");
        String sign =  MD5Util.getMD5Str(sign_str).toUpperCase();
        map.put("sign", sign);
        log.info("签名字符串 {}", sign_str);
        transParams.setSign(sign);
        log.info("请求参数 {}", JSONObject.toJSONString(map));
        String r = HttpClientUtil.doPostJson("http://127.0.0.1:8081/trans/create", JSONObject.toJSONString(map));
//        JSONObject jsonObject = xTransferService.create(transParams);
        System.out.println(r);
    }

    @Test
    public void testcJHttpCreate() {
        TransferParams transParams = new TransferParams();
        XMerchant xMerchant = xMerchantService.selectOne(3);
        TreeMap<String,Object> map = new TreeMap();
        map.put("bank_of_deposit", "广东深圳");
        map.put("account_of_pople", "何锦府");
        map.put("bank_card_number", "123456789123456089");
        map.put("bank_name", "工商银行");
        map.put("mcc_id", "90001");
        map.put("notify_url", "http://127.0.0.1");
        map.put("order_no", "ORDER_REPEAT");
        map.put("order_pay_amount", new BigDecimal(2000));
        String sign_str = new CommonUtil().getSign(map);
        sign_str = sign_str.concat("key=KfknMMtQPU2EYzaKtIbxPfX1B7ejwUVQ");
        String sign =  MD5Util.getMD5Str(sign_str).toUpperCase();
        map.put("sign", sign);
        log.info("签名字符串 {}", sign_str);
        transParams.setSign(sign);
        log.info("请求参数 {}", JSONObject.toJSONString(map));
        String r = HttpClientUtil.doPostJson("http://inter.cj8678a.cn/trans/create", JSONObject.toJSONString(map));
//        JSONObject jsonObject = xTransferService.create(transParams);
        System.out.println(r);
    }

    @Test
    public void testbDHttpViewCreate() {
        QueryParams queryParams = new QueryParams();
        XMerchant xMerchant = xMerchantService.selectOne(3);
        TreeMap<String,Object> map = new TreeMap();
        map.put("mcc", "10019");
        map.put("order_no", "W4rMH5cQHgnOSGeQRSIxICEbqTCQt");
        String sign_str = new CommonUtil().getSign(map);
        sign_str = sign_str.concat("key=oIZOk7E9hcCRxzd2gAk52VUsIhSzlmIV");
        String sign =  MD5Util.getMD5Str(sign_str).toUpperCase();
        map.put("sign", sign);
        log.info("签名字符串 {}", sign_str);
        queryParams.setSign(sign);
        log.info("请求参数 {}", JSONObject.toJSONString(map));
        String r = HttpClientUtil.doPostJson("http://127.0.0.1:8081/trans/view", JSONObject.toJSONString(map));
//        JSONObject jsonObject = xTransferService.create(transParams);
        System.out.println(r);
    }

    @Test
    public void testcJHttpViewCreate() {
        QueryParams queryParams = new QueryParams();
        XMerchant xMerchant = xMerchantService.selectOne(3);
        TreeMap<String,Object> map = new TreeMap();
        map.put("mcc_id", "90001");
        map.put("order_no", "2021234567890a");
        String sign_str = new CommonUtil().getSign(map);
        sign_str = sign_str.concat("key=KfknMMtQPU2EYzaKtIbxPfX1B7ejwUVQ");
        String sign =  MD5Util.getMD5Str(sign_str).toUpperCase();
        map.put("sign", sign);
        log.info("签名字符串 {}", sign_str);
        queryParams.setSign(sign);
        log.info("请求参数 {}", JSONObject.toJSONString(map));
        String r = HttpClientUtil.doPostJson("http://inter.cj8678a.cn/trans/view", JSONObject.toJSONString(map));
//        JSONObject jsonObject = xTransferService.create(transParams);
        System.out.println(r);
    }

    @Test
    public void testView() {
        QueryParams queryParams = new QueryParams();
        List<XTransfer> xTransfers = xTransferService.queryAllByLimit(0, 1);
        if (xTransfers.size() > 0) {
            XMerchant xMerchant = xMerchantService.selectOne(3);
            queryParams.setMerchant_id(3);
            queryParams.setMerchant_order_no(xTransfers.get(0).getMerchantOrderNo());
            String encod_str = "merchant_order_no=" + queryParams.getMerchant_order_no() + "&merchant_id=" + queryParams.getMerchant_id()
                    + "&key=" + xMerchant.getKey();
            String sign = MD5Util.getMD5Str(encod_str);
            queryParams.setSign(sign);
            JSONObject jsonObject = xTransferService.query(queryParams);
            System.out.print(jsonObject);
        }
    }


}
