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
import com.code.baseservice.util.MD5Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

@Slf4j
@Service("ZiJi")
public class ZiJiServiceImpl implements BaseService {


    @Value("${app.viewurl:}")
    private String viewUrl;

    @Autowired
    private ZfRechargeService zfRechargeService;

    private String domain = "";


    @Override
    public String notify(ZfRecharge zfRecharge,ZfChannel zfChannel, Map<String,Object> map) {
        log.info("自己微信通知 {}", map.toString());
        JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(map));
        if(jsonObject.getString("status").equals("1")){
            if(!jsonObject.getString("orderNo").equals(zfRecharge.getOrderNo())){
                return "fail";
            }
            zfRecharge.setOrderStatus(2);
            zfRecharge.setPaidAmount(zfRecharge.getPayAmount());
            zfRechargeService.paidOrder(zfRecharge);
            return "success";
        }
        return "fail";
    }

    public static void main(String[] args) {
        Integer a = new Integer(1111);
        System.out.println(a.equals(1111));
    }

    @Override
    public JSONObject create(ZfChannel zfChannel, ZfRecharge zfRecharge) {
        log.info("开始创建自己微信 {}", zfRecharge);
        TreeMap<String, Object> map = new TreeMap<>();
        map.put("appCode", zfChannel.getThirdMerchantId());
        map.put("channelCode", "WECHAT_CLOUD");
        map.put("orderNo", zfRecharge.getOrderNo());
        map.put("amount", zfRecharge.getPayAmount().setScale(0));
        map.put("notifyUrl", "http://afd7895.cn/recharge/json_notify/"+ zfRecharge.getOrderNo());
        String signTemp = new CommonUtil().getSign(map);
        signTemp = signTemp.substring(0, signTemp.length()-1);
        try {
            //还原秘钥
            SecretKeySpec signingKey = new SecretKeySpec(zfChannel.getThirdMerchantPrivateKey().getBytes(), "HmacSHA1");
            //实例化MAC
            Mac mac = Mac.getInstance("HmacSHA1");
            //初始化MAC
            mac.init(signingKey);
            //获得消息概要
            byte[] rawHmac = mac.doFinal(signTemp.getBytes());
            //BASE64编码
            String sign  =  java.util.Base64.getEncoder().encodeToString(rawHmac);
            map.put("sign",sign);
            log.info("单号 {} 开始请求 {}  参数 {} 签名 {}",zfRecharge.getMerchantOrderNo(),"https://api.saoziya.com/pay/order/create", JSONObject.toJSONString(map), signTemp);
            String reponse = "";
            reponse = HttpClientUtil.doPostJson("http://api.saoziya.com/pay/order/create", JSONObject.toJSONString(map));
            log.info("请求返回数据 {}", reponse);
            log.info("单号 {} 请求结果 {}", zfRecharge.getMerchantOrderNo(), reponse);
            JSONObject jsonObject = JSONObject.parseObject(reponse);
            if(jsonObject.getInteger("code" ) == 200){
                TreeMap<String, Object> map1 = new TreeMap<>();
                map1.put("merchant_order_no", zfRecharge.getMerchantOrderNo());
                map1.put("order_no", zfRecharge.getOrderNo());
                map1.put("pay_amount", zfRecharge.getPayAmount());
                map1.put("payurl",jsonObject.getJSONObject("data").getString("payUrl"));
                return new JSONObject(map1);
            }
            throw new BaseException(ResultEnum.ERROR);
        }catch (BaseException e){
            log.error("请求异常 {} 订单号{}", e, zfRecharge.getOrderNo());
            throw new BaseException(ResultEnum.ERROR);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public JSONObject create(ZfChannel zfChannel, ZfWithdraw zfWithdraw) {
        return new JSONObject();
    }

    @Override
    public JSONObject queryByWithdraw(ZfChannel zfChannel, ZfWithdraw zfWithdraw) {
        log.info("开始请求查询三方支付订单 {}", zfWithdraw);
        JSONObject jsonObject1 = new JSONObject();
        try {
            Map<String, Object> map = new TreeMap<>();
            map.put("mchid", zfChannel.getThirdMerchantId());
            map.put("ordersn","");
            map.put("Mordersn", zfWithdraw.getOrderNo());
            map.put("time", (new Date()).getTime());
            String sign_str = zfChannel.getThirdMerchantId()+"%%"+(new Date()).getTime()
                    +"%"+zfChannel.getThirdMerchantPrivateKey();
            log.info("签名字符串 {}", sign_str);
            String sign = MD5Util.getMD5Str(sign_str).toUpperCase();
            map.put("sign", sign);
            log.info("单号 {} 开始请求 {}  参数 {}",zfWithdraw.getMerchantOrderNo(), "http://8.217.168.61:8059/api/order/orderQuery", JSONObject.toJSONString(zfWithdraw));
            String reponse = HttpClientUtil.doPostJson("http://8.217.168.61:8059/api/order/orderQuery", JSONObject.toJSONString(map));
            JSONObject jsonObject = JSONObject.parseObject(reponse);
            log.info("单号 {} 请求结果 {}", zfWithdraw.getMerchantOrderNo(), reponse);
            if(jsonObject.getInteger("code").equals(1)){
                if(jsonObject.getJSONObject("data").getInteger("status" ) == 1){
                    jsonObject1.put("order_status", 2);
                    jsonObject1.put("code", 200);
                    return jsonObject1;
                }else if(jsonObject.getJSONObject("data").getInteger("status") == 2){
                    jsonObject1.put("order_status", 3);
                    jsonObject1.put("code", 200);
                    return jsonObject1;
                }
            }

            throw new BaseException(ResultEnum.ERROR);
        }catch (BaseException e){
            log.error("请求异常 {} 订单号{}", e, zfWithdraw.getMerchantOrderNo());
            throw new BaseException(ResultEnum.ERROR);
        }
    }
}
