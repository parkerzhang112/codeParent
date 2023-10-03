package com.code.payapi;

import com.alibaba.fastjson.JSONObject;
import com.code.baseservice.base.enums.ResultEnum;
import com.code.baseservice.base.exception.BaseException;
import com.code.baseservice.dto.ResponseResult;
import com.code.baseservice.dto.payapi.QueryParams;
import com.code.baseservice.dto.payapi.TransferParams;
import com.code.baseservice.entity.ZfMerchant;
import com.code.baseservice.service.ZfMerchantService;
import com.code.baseservice.service.ZfWithdrawService;
import com.code.baseservice.util.CommonUtil;
import com.code.baseservice.util.HttpClientUtil;
import com.code.baseservice.util.MD5Util;
import com.code.baseservice.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Random;
import java.util.TreeMap;

@Slf4j
public class TransControllerTest extends PayapiApplicationTests {

    @Autowired
    ZfMerchantService zfMerchantService;

    @Autowired
    ZfWithdrawService zfWithdrawService;

    @Test
    public void testCreate() {
        TransferParams transParams = new TransferParams();
        ZfMerchant xMerchant = zfMerchantService.queryById(10019);
        transParams.setOrder_type(1);
        transParams.setCard_account(StringUtil.createRandomStr1(10));
        transParams.setCard_name(StringUtil.createRandomStr1(3));
        transParams.setCard_address(StringUtil.createRandomStr1(10));
        transParams.setMerchant_order_no("dsadasdsaW16798203244WwvNtvHDX");
        transParams.setMerchant_id(10019);
        transParams.setCard_type("ICBC");
        transParams.setNotify_url("http://127.0.0.1");
        int amount = new Random().nextInt(100) + 100;
        transParams.setPay_amount(new BigDecimal(amount));
        transParams.setRemark(StringUtil.createRandomStr1(3));
        TreeMap<String, Object>  map = new TreeMap<>();
        map.put("card_address", transParams.getCard_address());
        map.put("card_name", transParams.getCard_name());
        map.put("card_account", transParams.getCard_account());
        map.put("card_type", transParams.getCard_type());
        map.put("merchant_id", transParams.getMerchant_id());
        map.put("notify_url", transParams.getNotify_url());
        map.put("merchant_order_no", transParams.getMerchant_order_no());
        map.put("pay_amount", transParams.getPay_amount());
        String sign_str = new CommonUtil().getSign(map);
        sign_str = sign_str.concat("key=".concat(xMerchant.getKey()));
        String sign =  MD5Util.getMD5Str(sign_str).toUpperCase();
        log.info("签名字符串 {} 签名值 {}", sign_str, sign);
        transParams.setSign(sign);
        ResponseResult responseResult = new ResponseResult();
        try {
            JSONObject jsonObject = zfWithdrawService.create(transParams);

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

        transParams.setOrder_type(0);
        transParams.setCard_account("123456789123456089");
        transParams.setCard_name("何锦府");
        transParams.setCard_address(StringUtil.createRandomStr1(10));
        transParams.setMerchant_order_no(StringUtil.createRandomStr1(29));
        transParams.setMerchant_id(3);
        transParams.setCard_type("ABC");
        transParams.setNotify_url("http://127.0.0.1");
        int amount = new Random().nextInt(100);
        transParams.setPay_amount(new BigDecimal(amount));
//        transParams.setRemark(StringUtil.createRandomStr1(3));
        String encod_str = "card_account=" + transParams.getCard_account()
                + "&card_address=" + transParams.getCard_address()
                + "&card_name=" + transParams.getCard_name()
                + "&card_type=" + transParams.getCard_type()
                + "&merchant_id=" + transParams.getMerchant_id()
                + "&merchant_order_no=" + transParams.getMerchant_order_no()
                + "&notify_url=" + transParams.getNotify_url()
                + "&pay_amount=" + transParams.getPay_amount()
                + "&key=zKvy&fRTFPQUg4Ce2uRaa966XLd8tTGy";


        log.info("签名字符串 {}", encod_str);
        String sign = MD5Util.getMD5Str(encod_str).toUpperCase();
        transParams.setSign(sign);
        String r = HttpClientUtil.doPostJson("http://qw520.top/withdraw/create", JSONObject.toJSONString(transParams));
//        JSONObject jsonObject = xTransferService.create(transParams);
        System.out.println(r);
    }


    @Test
    public void testView() {
        QueryParams queryParams = new QueryParams();
        ZfMerchant xMerchant = zfMerchantService.queryById(10019);
        queryParams.setMerchant_Id(xMerchant.getMerchantId());
        queryParams.setMerchant_order_no("22221232");
        TreeMap<String, Object> map = new TreeMap<>();
        map.put("merchant_id", queryParams.getMerchant_Id());
        map.put("merchant_order_no", queryParams.getMerchant_order_no());
        String sign_str = new CommonUtil().getSign(map);
        sign_str = sign_str.concat("key=".concat(xMerchant.getKey()));
        String sign =  MD5Util.getMD5Str(sign_str).toUpperCase();
        log.info("订单号 {} 当前我方签名 {}   对方签名 {}", queryParams.getMerchant_order_no(), sign, sign_str);
        queryParams.setSign(sign);
        ResponseResult responseResult = new ResponseResult();
        try {
            JSONObject jsonObject = zfWithdrawService.query(queryParams);
            responseResult.setData(jsonObject);
        }catch (BaseException e){
            responseResult.setCode(e.getCode()).setMsg(e.getMessage());
        }catch (Exception e){
            log.error("系统异常", e);
            responseResult.setCode(ResultEnum.ERROR.getCode()).setMsg("系统异常");
        }
        System.out.println(responseResult.toJsonString());
    }


}
