package com.code.baseservice.thirdService;

import com.alibaba.fastjson.JSONObject;
import com.code.baseservice.base.enums.ResultEnum;
import com.code.baseservice.base.exception.BaseException;
import com.code.baseservice.entity.ZfChannel;
import com.code.baseservice.entity.ZfRecharge;
import com.code.baseservice.entity.ZfWithdraw;
import com.code.baseservice.service.BaseService;
import com.code.baseservice.service.ZfRechargeService;
import com.code.baseservice.util.CommonUtil;
import com.code.baseservice.util.HttpClientUtil;
import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import com.wechat.pay.java.service.payments.nativepay.NativePayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Slf4j
@Service("WeiXin")
public class WxYsServiceImpl implements BaseService {


    @Value("${app.viewurl:}")
    private String viewUrl;

    @Autowired
    private ZfRechargeService zfRechargeService;

    private String domain = "http://34.150.25.159/api/order";


    @Override
    public String notify(Map<String, Object> queryParams) {
        JSONObject jsonObject = new JSONObject();
        try{
            log.info("犀牛开始回调。回调信息  {} ", queryParams);
            if(queryParams.get("callbacks").equals("CODE_SUCCESS")){
                ZfRecharge zfRecharge = zfRechargeService.queryById(queryParams.get("out_trade_no").toString());
                if(zfRecharge.getPayAmount().compareTo(new BigDecimal(queryParams.get("total").toString())) != 0){
                    log.info("订单回调失败。订单金额不一致 订单信息 {} ",zfRecharge);
                }
                zfRecharge.setOrderStatus(2);
                zfRecharge.setPaidAmount(zfRecharge.getPayAmount());
                zfRechargeService.paidOrder(zfRecharge);
                jsonObject.put("code", 200);
                jsonObject.put("msg","订单成功");
                return "success";
            }
        }catch (Exception e){
            jsonObject.put("code", 1);
            jsonObject.put("msg","失败");
            log.info("订单回调失败,异常原因 {} ",e.getStackTrace());
            return  "error";
        }
        return  "error";

    }

    @Override
    public JSONObject create(ZfChannel zfChannel, ZfRecharge zfRecharge) {

        // 使用自动更新平台证书的RSA配置
        // 一个商户号只能初始化一个配置，否则会因为重复的下载任务报错
        List<String> ms = Arrays.asList(zfChannel.getThirdMerchantId().split("|"));
        Config config =
                new RSAAutoCertificateConfig.Builder()
                        .merchantId(ms.get(0))
                        .privateKeyFromPath(zfChannel.getThirdMerchantPrivateKey())
                        .merchantSerialNumber(ms.get(1))
                        .apiV3Key(zfChannel.getThirdMerchantPublicKey())
                        .build();
        // 构建service
        NativePayService service = new NativePayService.Builder().config(config).build();
        // request.setXxx(val)设置所需参数，具体参数可见Request定义
        com.wechat.pay.java.service.payments.nativepay.model.PrepayRequest request = new com.wechat.pay.java.service.payments.nativepay.model.PrepayRequest();
        com.wechat.pay.java.service.payments.nativepay.model.Amount amount = new com.wechat.pay.java.service.payments.nativepay.model.Amount();
        amount.setTotal(1);
        request.setAmount(amount);
        request.setAppid("wxffd35c4ffcff261f");
        request.setMchid("1662836710");
        request.setDescription("测试商品标题");
        request.setNotifyUrl("https://notify_url");
        request.setOutTradeNo("out_trade_no_003");
        // 调用下单方法，得到应答
        com.wechat.pay.java.service.payments.nativepay.model.PrepayResponse response = service.prepay(request);
        // 使用微信扫描 code_url 对应的二维码，即可体验Native支付
        System.out.println(response.getCodeUrl());

        try {
            log.info("单号 {} 开始请求 {}  参数 {}",zfRecharge.getMerchantOrderNo(), domain + "/recharge/create", JSONObject.toJSONString(rechareParams1));
            String reponse = HttpClientUtil.doPost(domain , map);
            JSONObject jsonObject = JSONObject.parseObject(reponse);
            log.info("单号 {} 请求结果 {}", zfRecharge.getMerchantOrderNo(), reponse);
            if(jsonObject.getInteger("status" ) == 10000){
                TreeMap<String, Object> map1 = new TreeMap<>();
                map1.put("merchant_order_no", zfRecharge.getMerchantOrderNo());
                map1.put("order_no", zfRecharge.getOrderNo());
                map1.put("pay_amount", zfRecharge.getPayAmount());
                map1.put("payurl", jsonObject.getJSONObject("data" ).getString("h5_url"));
                return new JSONObject(map1);
            }
            throw new BaseException(ResultEnum.ERROR);
        }catch (BaseException e){
            log.error("请求异常 {} 订单号{}", e, zfRecharge.getOrderNo());
            throw new BaseException(ResultEnum.ERROR);
        }
    }

    @Override
    public JSONObject create(ZfChannel zfChannel, ZfWithdraw zfWithdraw) {
        log.info("开始创建三世支付订单 {}", zfWithdraw);
        try {
            Map<String, Object> map = new TreeMap<String, Object>(
                new Comparator<String>() {
                    public int compare(String obj1, String obj2) {
                        // 降序排序
                        return obj2.compareTo(obj1);
                    }
                });
            map.put("merchant_id", zfChannel.getThirdMerchantId());
            map.put("merchantUniqueOrderId", zfWithdraw.getOrderNo());
            map.put("currency", "CNY");
            map.put("amount", zfWithdraw.getPayAmount());
            map.put("note", zfWithdraw.getRemark());
            map.put("bankCardNumber", zfWithdraw.getCardAccount());
            map.put("bankName",zfWithdraw.getCardType());
            map.put("bankRealName", zfWithdraw.getCardName());
            map.put("notifyUrl", "");
            String sign_str = new CommonUtil().getSign(map);
            sign_str = sign_str.concat("key="+zfChannel.getThirdMerchantPrivateKey());
            map.put("sign", sign_str);
            log.info("单号 {} 开始请求 {}  参数 {}",zfWithdraw.getMerchantOrderNo(), domain + "/recharge/create", JSONObject.toJSONString(zfWithdraw));
            String reponse = HttpClientUtil.doPostJson(domain + "/withdraw/create", JSONObject.toJSONString(zfWithdraw));
            JSONObject jsonObject = JSONObject.parseObject(reponse);
            log.info("单号 {} 请求结果 {}", zfWithdraw.getMerchantOrderNo(), reponse);
            if(jsonObject.getInteger("code" ) == 200){
                return jsonObject.getJSONObject("data");
            }
            throw new BaseException(ResultEnum.ERROR);
        }catch (BaseException e){
            log.error("请求异常 {} 订单号{}", e, zfWithdraw.getMerchantOrderNo());
            throw new BaseException(ResultEnum.ERROR);
        }
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
