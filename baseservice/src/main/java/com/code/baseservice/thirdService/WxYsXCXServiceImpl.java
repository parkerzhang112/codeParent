package com.code.baseservice.thirdService;

import com.alibaba.fastjson.JSONObject;
import com.code.baseservice.base.enums.ResultEnum;
import com.code.baseservice.base.exception.BaseException;
import com.code.baseservice.entity.ZfChannel;
import com.code.baseservice.entity.ZfRecharge;
import com.code.baseservice.entity.ZfWithdraw;
import com.code.baseservice.service.BaseService;
import com.code.baseservice.service.ZfRechargeService;
import com.code.baseservice.util.HttpClientUtil;
import com.code.baseservice.util.StringUtils;
import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import com.wechat.pay.java.core.exception.ValidationException;
import com.wechat.pay.java.service.payments.jsapi.JsapiService;
import com.wechat.pay.java.service.payments.jsapi.model.PrepayRequest;
import com.wechat.pay.java.service.payments.jsapi.model.PrepayResponse;
import jodd.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Slf4j
@Service("WxYsXCXService")
public class WxYsXCXServiceImpl implements BaseService {


    @Value("${app.viewurl:}")
    private String viewUrl;

    @Autowired
    private ZfRechargeService zfRechargeService;

    public static JsapiService service;

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
        if(!StringUtil.isBlank(remark)){
            JSONObject jsonObject = JSONObject.parseObject(remark);
            if(StringUtils.isNotEmpty(jsonObject.getString(price))){
                return jsonObject.getString(price);
            }
        }
        Map<String, String> map = new HashMap<>();
        map.put("500", "【巨量千川解析】");
        map.put("300", "【主播集训课】全方位解析定位及技巧");
        map.put("200", "【运营集训课】直播间分工及职责");
        map.put("188" ,"【运营集训课】直播间爆款打造");
        map.put("100", "【投手集训课】初级数据分析");
        map.put("50", "【产品集训课】选品排品逻辑");
        map.put("30", "【服装品类】参考直播话术");
        if(map.containsKey(price)){
            return  map.get(price);
        }
        return  null;
    }

    public static void main(String[] args) {
        String b  = "{\n" +
                "    \"500\": \"【巨量千川解析】\",\n" +
                "    \"300\": \"【主播集训课】全方位解析定位及技巧\",\n" +
                "    \"200\": \"【运营集训课】直播间分工及职责\",\n" +
                "    \"188\": \"【运营集训课】直播间爆款打造\",\n" +
                "    \"100\": \"【投手集训课】初级数据分析\",\n" +
                "    \"50\": \"【产品集训课】选品排品逻辑\",\n" +
                "    \"30\": \"【服装品类】参考直播话术\"\n" +
                "}";
        JSONObject jsonObject = JSONObject.parseObject(b);
        BigDecimal a = new BigDecimal("200.00");
        System.out.print(a.toString());
        System.out.print(jsonObject.getString(a.setScale(0).toString()));

    }

    @Override
    public JSONObject create(ZfChannel zfChannel, ZfRecharge zfRecharge) {

        // 使用自动更新平台证书的RSA配置
        // 一个商户号只能初始化一个配置，否则会因为重复的下载任务报错
        List<String> ms = Arrays.asList(zfChannel.getThirdMerchantId().split("\\|"));
        Config config =
                new RSAAutoCertificateConfig.Builder()
                        .merchantId(ms.get(0))
                        // 使用 com.wechat.pay.java.core.util 中的函数从本地文件中加载商户私钥，商户私钥会用来生成请求的签名
                        .privateKeyFromPath(zfChannel.getThirdMerchantPrivateKey())
                        .merchantSerialNumber(ms.get(1))
                        .apiV3Key(zfChannel.getThirdMerchantPublicKey())
                        .build();

        service = new JsapiService.Builder().config(config).build();
        PrepayRequest request = new PrepayRequest();
        com.wechat.pay.java.service.payments.jsapi.model.Amount amount = new com.wechat.pay.java.service.payments.jsapi.model.Amount();
        amount.setTotal(zfRecharge.getPayAmount().intValue() * 100);
        request.setAmount(amount);
        request.setAppid(ms.get(2));
        request.setMchid(ms.get(0));
        String goodName = getGoodName(zfRecharge.getPayAmount().setScale(0).toString(), zfChannel.getRemark());
        if(goodName == null){
            return null;
        }
        request.setDescription(goodName);
        request.setNotifyUrl( "https://bjy6688.top/recharge/json_notify/"+ zfRecharge.getOrderNo());
        request.setOutTradeNo(zfRecharge.getOrderNo());
        // 调用request.setXxx(val)设置所需参数，具体参数可见Request定义
        // 调用接口
        try {
            log.info("单号 {} 开始请求 {}  参数 {}",zfRecharge.getMerchantOrderNo(), domain + "/recharge/create", JSONObject.toJSONString(request));
            PrepayResponse response = service.prepay(request);
            if(response.getPrepayId() != null){
                log.info("单号 {} 请求结果 {}", zfRecharge.getMerchantOrderNo(), response.toString());
                TreeMap<String, Object> map1 = new TreeMap<>();
                map1.put("merchant_order_no", zfRecharge.getMerchantOrderNo());
                map1.put("order_no", zfRecharge.getOrderNo());
                map1.put("pay_amount", zfRecharge.getPayAmount());
                map1.put("payurl", response.getPrepayId());
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
