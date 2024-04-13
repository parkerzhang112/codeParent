package com.code.baseservice.thirdService;

import com.alibaba.fastjson.JSONObject;
import com.code.baseservice.base.enums.ResultEnum;
import com.code.baseservice.base.exception.BaseException;
import com.code.baseservice.entity.ZfChannel;
import com.code.baseservice.entity.ZfCode;
import com.code.baseservice.entity.ZfRecharge;
import com.code.baseservice.entity.ZfWithdraw;
import com.code.baseservice.service.BaseService;
import com.code.baseservice.service.ZfRechargeService;
import com.code.baseservice.util.HttpClientUtil;
import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import com.wechat.pay.java.core.exception.ValidationException;
import com.wechat.pay.java.core.http.DefaultHttpClientBuilder;
import com.wechat.pay.java.service.payments.jsapi.JsapiServiceExtension;
import com.wechat.pay.java.service.payments.jsapi.model.Payer;
import com.wechat.pay.java.service.payments.jsapi.model.PrepayRequest;
import com.wechat.pay.java.service.payments.jsapi.model.PrepayWithRequestPaymentResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;
import java.util.*;

import static java.net.Proxy.Type.HTTP;

@Slf4j
@Service("WxYsXCXService")
public class WxYsXCXServiceImpl implements BaseService {

    private String appid = "wx31c8156e39f646c7";

    private String appsecrect = "996b2b1eb34e86d44f432648faa1da28";

    @Value("${app.viewurl:}")
    private String viewUrl;

    @Autowired
    private ZfRechargeService zfRechargeService;

    public static JsapiServiceExtension  service;

    private String domain = "http://34.150.25.159/api/order";

    @Override
    public String notify(ZfRecharge zfRecharge,  ZfChannel zfChannel, Map<String, Object> queryParams) {
        log.info("微信开始回调。回调信息 单号 {}  {} ",zfRecharge.getOrderNo(), queryParams);
        try {
            if(queryParams.get("event_type").equals("TRANSACTION.SUCCESS")){
                zfRecharge.setOrderStatus(2);
                zfRecharge.setPaidAmount(zfRecharge.getPayAmount());
                zfRechargeService.paidOrder(zfRecharge);
                return "success";
            }
        } catch (ValidationException e) {
            log.info("订单回调失败,异常原因 {} ",e.getStackTrace());
            // 签名验证失败，返回 401 UNAUTHORIZED 状态码
        }
        return  "error";
    }

    public static String getGoodName(String price, String remark){
        log.info("渠道金额配置 ：{} 订单金额 {}", remark,price );
//        if(!StringUtil.isBlank(remark)){
//            JSONObject jsonObject = JSONObject.parseObject(remark);
//            if(StringUtils.isNotEmpty(jsonObject.getString(price))){
//                return jsonObject.getString(price);
//            }
//        }
        String key = "";
        Map<String, String> map = new HashMap<>();
        map.put("100", "19朵玫瑰花束");
        map.put("50", "11朵玫瑰花束");
        map.put("30", "8朵仿真玫瑰");
        if(map.containsKey(price)){
            return  map.get(price);
        }
        return  "补货";
//        return  null;
    }

    public static void main(String[] args) {
        String price ="49";
        String key = "";
        BigDecimal priceb = new BigDecimal(price);
        BigDecimal thirty = new BigDecimal("30");
        BigDecimal fifty = new BigDecimal("50");
        BigDecimal one = new BigDecimal("100");
        BigDecimal oneee = new BigDecimal("188");
        BigDecimal two = new BigDecimal("200");
        BigDecimal three = new BigDecimal("300");
        BigDecimal five = new BigDecimal("500");

        if(priceb.compareTo(thirty)>=0 && priceb.compareTo(fifty) < 0){
            key = "30";
        }
        if(priceb.compareTo(fifty)>=0 && priceb.compareTo(one) < 0){
            key = "50";
        }
        if(priceb.compareTo(one)>=0 && priceb.compareTo(oneee) < 0){
            key = "100";
        }
        if(priceb.compareTo(oneee)>=0 && priceb.compareTo(two) < 0){
            key = "188";
        }
        if(priceb.compareTo(two)>=0 && priceb.compareTo(three) < 0){
            key = "200";
        }
        if(priceb.compareTo(three)>=0 && priceb.compareTo(five) < 0){
            key = "300";
        }
        if(priceb.compareTo(five)>=0 ){
            key = "500";
        }


        Map<String, String> map = new HashMap<>();
        map.put("100", "19朵玫瑰花束");
        map.put("50", "11朵玫瑰花束");
        map.put("30", "8朵仿真玫瑰");
        if(map.containsKey(key)){
            System.out.printf(map.get(key));
        }

    }

    @Override
    public JSONObject create(ZfChannel zfChannel, ZfRecharge zfRecharge) {
        try {
            String respone =  HttpClientUtil.doGet("https://api.weixin.qq.com/cgi-bin/token?appid="+zfChannel.getThirdMerchantId()+"&secret="+zfChannel.getThirdMerchantPrivateKey()+"&grant_type=client_credential");
            JSONObject  jsopObject = JSONObject.parseObject(respone);
            String accessToken = jsopObject.getString("access_token");
            JSONObject params = new JSONObject();
            params.put("is_expire", true);
            params.put("expire_type", 1);
            params.put("expire_interval", 30);
            JSONObject jumpWxa = new JSONObject();
            jumpWxa.put("path", "pages/goods/pay/pay");
            jumpWxa.put("query", "query="+zfRecharge.getOrderNo() + "&amount="+zfRecharge.getPayAmount());
            jumpWxa.put("env_version", "release");
            params.put("jump_wxa", jumpWxa);
            log.info("单号 {}  参数 {} token {}",zfRecharge.getMerchantOrderNo(), JSONObject.toJSONString(params), accessToken);
            String getUrlRespone = HttpClientUtil.doPostJson("https://api.weixin.qq.com/wxa/generatescheme?access_token="+accessToken, params.toJSONString());
            JSONObject getUrlResponeJson = JSONObject.parseObject(getUrlRespone);
            if(getUrlResponeJson.getInteger("errcode") == 0){
                log.info("单号 {} 请求结果 {}", zfRecharge.getMerchantOrderNo(), getUrlRespone);
                TreeMap<String, Object> map1 = new TreeMap<>();
                map1.put("merchant_order_no", zfRecharge.getMerchantOrderNo());
                map1.put("order_no", zfRecharge.getOrderNo());
                map1.put("pay_amount", zfRecharge.getPayAmount());
                map1.put("payurl", getUrlResponeJson.getString("openlink"));
                return new JSONObject(map1);
            }else {
                log.error("请求异常  订单号{}", zfRecharge.getOrderNo(), getUrlRespone);
            }
        }catch (Exception e){
            log.error("请求异常  订单号{}", zfRecharge.getOrderNo(), e);
        }
        return null;
    }

    public JSONObject createPrePayId(ZfChannel zfChannel, ZfRecharge zfRecharge, ZfCode zfCode){
        List<String> ms = Arrays.asList(zfChannel.getProyx().split("\\:"));
        DefaultHttpClientBuilder httpClient = new DefaultHttpClientBuilder();
        SocketAddress socketAddress = new InetSocketAddress(ms.get(0), Integer.valueOf(ms.get(1)));
        httpClient.proxy(new Proxy(HTTP, socketAddress)).build();
        // 使用自动更新平台证书的RSA配置
        // 一个商户号只能初始化一个配置，否则会因为重复的下载任务报错
        Config config =
                new RSAAutoCertificateConfig.Builder()
                        .merchantId(zfCode.getAccount())
                        // 使用 com.wechat.pay.java.core.util 中的函数从本地文件中加载商户私钥，商户私钥会用来生成请求的签名
                        .privateKeyFromPath(zfCode.getImage())
                        .merchantSerialNumber(zfCode.getWxCertificateNo())
                        .httpClientBuilder(httpClient)
                        .apiV3Key(zfCode.getWxMerchantPublicKey())
                        .build();
        service = new JsapiServiceExtension.Builder().config(config).build();
        PrepayRequest request = new PrepayRequest();
        com.wechat.pay.java.service.payments.jsapi.model.Amount amount = new com.wechat.pay.java.service.payments.jsapi.model.Amount();
        amount.setTotal(zfRecharge.getPayAmount().intValue() * 100);
        request.setAmount(amount);
        request.setAppid(zfChannel.getThirdMerchantId());
        Payer payer =  new Payer();
        payer.setOpenid(zfRecharge.getPayName());
        request.setPayer(payer);
        request.setMchid(zfCode.getAccount());
        String goodName = getGoodName(zfRecharge.getPayAmount().setScale(0).toString(), zfChannel.getRemark());
        if(goodName == null){
            return null;
        }
        request.setDescription(goodName);
        request.setNotifyUrl( "https://"+zfChannel.getDomain()+"/outpay/recharge/json_notify/"+ zfRecharge.getOrderNo());
        request.setOutTradeNo(zfRecharge.getOrderNo());
        // 调用request.setXxx(val)设置所需参数，具体参数可见Request定义
        // 调用接口
        try {

            log.info("单号 {} 开始请求 {}  参数 {}",zfRecharge.getMerchantOrderNo(), domain + "/recharge/create", JSONObject.toJSONString(request));
            PrepayWithRequestPaymentResponse response = service.prepayWithRequestPayment(request);
            if(response.getAppId() != null){
                log.info("单号 {} 请求结果 {}", zfRecharge.getMerchantOrderNo(), response.toString());
                TreeMap<String, Object> map1 = new TreeMap<>();
                map1.put("appId", response.getAppId());
                map1.put("timeStamp", response.getTimeStamp());
                map1.put("nonceStr", response.getNonceStr());
                map1.put("package", response.getPackageVal());
                map1.put("signType", response.getSignType());
                map1.put("paySign", response.getPaySign());
                return new JSONObject(map1);
            }else {
                log.info("单号 {} 请求结果 {}", zfRecharge.getMerchantOrderNo(), response.toString());
                return null;
            }
        }catch (BaseException e){
            log.error("请求异常 {} 订单号{}", e, zfRecharge.getOrderNo());
            return  null;
        }
    }

    public JSONObject getOpenId(ZfChannel zfChannel, ZfRecharge zfRecharge){
        try{
            String url = "https://api.weixin.qq.com/sns/jscode2session?appid="+this.appid+"&secret="+this.appsecrect+"&js_code="+zfRecharge.getPayName()+"&grant_type=authorization_code";
            String res = HttpClientUtil.doGet(url);
            log.info("获取open id {}", res);
            JSONObject jsonObject = JSONObject.parseObject(res);
            return jsonObject;
        }catch (Exception e){
            log.error("获取openid  异常 ", e);
            return new JSONObject();
        }
    }

    @Override
    public JSONObject create(ZfChannel zfChannel, ZfWithdraw zfWithdraw) {
        log.info("开始创建三世支付订单 {}", zfWithdraw);
        return  new JSONObject();
    }

    @Override
    public JSONObject queryByWithdraw(ZfChannel zfChannel, ZfWithdraw zfWithdraw) {
        log.info("开始请求查询三方支付订单 {}", zfWithdraw);
        JSONObject jsonObject1 = new JSONObject();
        try {
            log.info("单号 {} 开始请求 {}  参数 {}",zfWithdraw.getMerchantOrderNo(), domain + "/recharge/create", JSONObject.toJSONString(zfWithdraw));
            String reponse = HttpClientUtil.doPostJson(domain + "/withdraw/view", JSONObject.toJSONString(zfWithdraw));
            JSONObject jsonObject = JSONObject.parseObject(reponse);
            log.info("单号 {} 请求结果 {}", zfWithdraw.getMerchantOrderNo(), reponse);
            if(jsonObject.getInteger("code" ) == 200){
                jsonObject1.put("code", 200);
                return jsonObject;
            }
            throw new BaseException(ResultEnum.ERROR);
        }catch (BaseException e){
            log.error("请求异常 {} 订单号{}", e, zfWithdraw.getMerchantOrderNo());
            throw new BaseException(ResultEnum.ERROR);
        }
    }
}
