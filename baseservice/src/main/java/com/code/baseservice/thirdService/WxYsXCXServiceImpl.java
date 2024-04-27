package com.code.baseservice.thirdService;

import com.alibaba.fastjson.JSONObject;
import com.code.baseservice.base.enums.ResultEnum;
import com.code.baseservice.base.exception.BaseException;
import com.code.baseservice.dto.ZFchannelSku;
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
import com.wechat.pay.java.core.http.UrlEncoder;
import com.wechat.pay.java.service.payments.jsapi.JsapiServiceExtension;
import com.wechat.pay.java.service.payments.jsapi.model.Payer;
import com.wechat.pay.java.service.payments.jsapi.model.PrepayRequest;
import com.wechat.pay.java.service.payments.jsapi.model.PrepayWithRequestPaymentResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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

    public static String getGoodName(BigDecimal price, String goods){
        ZFchannelSku zFchannelSku  = JSONObject.parseObject(goods, ZFchannelSku.class);
        if(StringUtils.isNotBlank(zFchannelSku.getShop_one_price())){
            List<String> between = Arrays.asList(zFchannelSku.getShop_one_price().split(","));
            if(price.compareTo(new BigDecimal(between.get(0))) > -1 && price.compareTo(new BigDecimal(between.get(1)) ) < 1){
                return zFchannelSku.getShop_one_name();
            }
        }
        if(StringUtils.isNotBlank(zFchannelSku.getShop_two_price())){
            List<String> between = Arrays.asList(zFchannelSku.getShop_two_price().split(","));
            if(price.compareTo(new BigDecimal(between.get(0))) > -1 && price.compareTo(new BigDecimal(between.get(1)) ) < 1){
                return zFchannelSku.getShop_two_name();
            }
        }
        if(StringUtils.isNotBlank(zFchannelSku.getShop_three_price())){
            List<String> between = Arrays.asList(zFchannelSku.getShop_three_price().split(","));
            if(price.compareTo(new BigDecimal(between.get(0))) > -1 && price.compareTo(new BigDecimal(between.get(1)) ) < 1){
                return zFchannelSku.getShop_three_name();
            }
        }
        if(StringUtils.isNotBlank(zFchannelSku.getShop_four_price())){
            List<String> between = Arrays.asList(zFchannelSku.getShop_four_price().split(","));
            if(price.compareTo(new BigDecimal(between.get(0))) > -1 && price.compareTo(new BigDecimal(between.get(1)) ) < 1){
                return zFchannelSku.getShop_four_name();
            }
        }
        if(StringUtils.isNotBlank(zFchannelSku.getShop_five_price())){
            List<String> between = Arrays.asList(zFchannelSku.getShop_five_price().split(","));
            if(price.compareTo(new BigDecimal(between.get(0))) > -1 && price.compareTo(new BigDecimal(between.get(1)) ) < 1){
                return zFchannelSku.getShop_five_name();
            }
        }
        if(StringUtils.isNotBlank(zFchannelSku.getShop_six_price())){
            List<String> between = Arrays.asList(zFchannelSku.getShop_six_price().split(","));
            if(price.compareTo(new BigDecimal(between.get(0))) > -1 && price.compareTo(new BigDecimal(between.get(1)) ) < 1){
                return zFchannelSku.getShop_six_name();
            }
        }
        return  zFchannelSku.getShop_default_name();
    }

    public static void main(String[] args) {
        List<String> ms =   Arrays.asList("127.0.0.1:13048".split("\\:"));
        System.out.println(ms);
    }

    @Override
    public JSONObject create(ZfChannel zfChannel, ZfRecharge zfRecharge) {
        try {
            String url  = "https://api.weixin.qq.com/cgi-bin/token?appid="+zfChannel.getThirdMerchantId()+"&secret="+zfChannel.getThirdMerchantPrivateKey()+"&grant_type=client_credential";
            String respone= "";
            if (StringUtils.isNotBlank(zfChannel.getProxy())){
                List<String> ms = Arrays.asList(zfChannel.getProxy().split("\\:"));
                 respone =  HttpClientUtil.doGet(url, null, ms.get(0), ms.get(1));
            }else {
                 respone =  HttpClientUtil.doGet(url, null);
            }

            JSONObject  jsopObject = JSONObject.parseObject(respone);
            if(!jsopObject.containsKey("access_token")){
                log.info("获取access_token失败，可能原因 appid和appsercet配置错误, 渠道 {}", zfChannel.getChannelName());
                throw  new BaseException(ResultEnum.ERROR);
            }
            String accessToken = jsopObject.getString("access_token");
            JSONObject params = new JSONObject();
            params.put("is_expire", true);
            params.put("expire_type", 1);
            params.put("expire_interval", 30);
            JSONObject jumpWxa = new JSONObject();
            jumpWxa.put("path", "pages/goods/pay/pay");
            jumpWxa.put("query", "query="+zfRecharge.getOrderNo() + "&amount="+zfRecharge.getPayAmount() + "&requesturl=" + UrlEncoder.urlEncode(zfChannel.getDomain())) ;
            jumpWxa.put("env_version", "release");
            params.put("jump_wxa", jumpWxa);
            log.info("单号 {}  参数 {} token {}",zfRecharge.getMerchantOrderNo(), JSONObject.toJSONString(params), accessToken);
            String getUrlRespone = "";
            if (StringUtils.isNotBlank(zfChannel.getProxy())){
                 List<String> ms = Arrays.asList(zfChannel.getProxy().split("\\:"));
                 getUrlRespone = HttpClientUtil.doPostJsonByProxy("https://api.weixin.qq.com/wxa/generatescheme?access_token="+accessToken, params.toJSONString(), ms.get(0), ms.get(1));
            }else {
                getUrlRespone = HttpClientUtil.doPostJson("https://api.weixin.qq.com/wxa/generatescheme?access_token="+accessToken, params.toJSONString());
            }
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
                log.info("获取微信小程序加密地址异常，可能原因 微信小程序未发布支付插件或日限制次数, 渠道 {}", zfChannel.getChannelName());
                throw  new BaseException(ResultEnum.WEIXIAOCHENXU_PATH_ERROR);
            }
        }catch (Exception e){
            log.error("请求异常  订单号{}", zfRecharge.getOrderNo(), e);
        }
        return null;
    }

    public JSONObject createPrePayId(ZfChannel zfChannel, ZfRecharge zfRecharge, ZfCode zfCode){
        List<String> ms = Arrays.asList(zfChannel.getProxy().split("\\:"));
        Config config = null;
        if(ms.size() == 2) {
            // 使用自动更新平台证书的RSA配置
            // 一个商户号只能初始化一个配置，否则会因为重复的下载任务报错
            DefaultHttpClientBuilder httpClient = new DefaultHttpClientBuilder();
            SocketAddress socketAddress = new InetSocketAddress(ms.get(0), Integer.valueOf(ms.get(1)));
            httpClient.proxy(new Proxy(HTTP, socketAddress));
             config =
                    new RSAAutoCertificateConfig.Builder()
                            .merchantId(zfCode.getAccount())
                            // 使用 com.wechat.pay.java.core.util 中的函数从本地文件中加载商户私钥，商户私钥会用来生成请求的签名
                            .privateKeyFromPath(zfCode.getImage().replaceAll("profile", "data/image"))
                            .merchantSerialNumber(zfCode.getWxCertificateNo())
                            .httpClientBuilder(httpClient)
                            .apiV3Key(zfCode.getWxMerchantPublicKey())
                            .build();
        }else {
            config =
                    new RSAAutoCertificateConfig.Builder()
                            .merchantId(zfCode.getAccount())
                            // 使用 com.wechat.pay.java.core.util 中的函数从本地文件中加载商户私钥，商户私钥会用来生成请求的签名
                            .privateKeyFromPath(zfCode.getImage().replaceAll("profile", "data/image"))
                            .merchantSerialNumber(zfCode.getWxCertificateNo())
                            .apiV3Key(zfCode.getWxMerchantPublicKey())
                            .build();
        }
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
        String goodName = getGoodName(zfRecharge.getPayAmount().setScale(0), zfChannel.getGoods());
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
            String url = "https://api.weixin.qq.com/sns/jscode2session?appid="+zfChannel.getThirdMerchantId()+"&secret="+zfChannel.getThirdMerchantPrivateKey()+"&js_code="+zfRecharge.getPayName()+"&grant_type=authorization_code";
            String res= "";
            if (StringUtils.isNotBlank(zfChannel.getProxy())){
                List<String> ms = Arrays.asList(zfChannel.getProxy().split("\\:"));
                res =  HttpClientUtil.doGet(url, null, ms.get(0), ms.get(1));
            }else {
                res =  HttpClientUtil.doGet(url, null);
            }
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
