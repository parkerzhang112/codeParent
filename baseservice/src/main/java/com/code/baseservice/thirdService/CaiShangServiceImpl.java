package com.code.baseservice.thirdService;

import com.alibaba.fastjson.JSONObject;
import com.code.baseservice.base.enums.ResultEnum;
import com.code.baseservice.base.exception.BaseException;
import com.code.baseservice.entity.ZfChannel;
import com.code.baseservice.entity.ZfRecharge;
import com.code.baseservice.entity.ZfWithdraw;
import com.code.baseservice.service.BaseService;
import com.code.baseservice.service.ZfRechargeService;
import com.code.baseservice.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

@Slf4j
@Service("CaiShang")
public class CaiShangServiceImpl implements BaseService {


    @Value("${app.viewurl:}")
    private String viewUrl;

    @Autowired
    private ZfRechargeService zfRechargeService;

    private String domain = "";


    @Override
    public String notify(ZfRecharge zfRecharge,ZfChannel zfChannel, Map<String,Object> map) {
        log.info("彩商通知 {}", map.toString());
        AesUtil aesUtil = new AesUtil();
        String decryptData  = aesUtil.decryptBy(map.get("data").toString(), zfChannel.getThirdMerchantPrivateKey());
        log.info("彩商通知 解析内容 {}", decryptData);
        JSONObject jsonObject = JSONObject.parseObject(decryptData);
        if(jsonObject.getString("orderId").equals(zfRecharge.getOrderNo()) && jsonObject.getInteger("status").equals(0)){
            zfRecharge.setOrderStatus(2);
            zfRecharge.setPaidAmount(zfRecharge.getPayAmount());
            zfRechargeService.paidOrder(zfRecharge);
            JSONObject rJ  = new JSONObject();
            rJ.put("code", 0);
            return  rJ.toString();
        }
        JSONObject rJ  = new JSONObject();
        rJ.put("code", 1);
        return  rJ.toString();

    }

    public static void main(String[] args) {
        Integer a = new Integer(1111);
        System.out.println(a.equals(1111));
    }

    @Override
    public JSONObject create(ZfChannel zfChannel, ZfRecharge zfRecharge) {
        log.info("开始创建彩商 {}", zfRecharge);
        TreeMap<String, Object> map = new TreeMap<>();
        map.put("merchantId", zfChannel.getThirdMerchantId());
        map.put("userId", String.valueOf( new Random().nextInt(10000000)));
        map.put("orderId", zfRecharge.getOrderNo());
        map.put("orderTime", DateUtil.format(new Date(), DateUtil.YYYY_MM_DD_HH_MM_SS));
        map.put("terminalType", "PC");
        map.put("account", "");
        map.put("amount", zfRecharge.getPayAmount().setScale(2).floatValue());
        map.put("payWith", 2);
        map.put("payer", StringUtil.createRandomStr1(4));
        map.put("postscript", "");
        map.put("useCounter", true);
        map.put("notifyUrl", "http://afd7895.cn/recharge/json_notify/"+ zfRecharge.getOrderNo());
        map.put("label", "");
        map.put("nonce", StringUtil.createRandomStr1(4));
        map.put("timestamp", new Date().getTime());
        AesUtil aesUtil = new AesUtil();
        String encreptData = "";
        try {
            log.info("加密参数 {} 加密密钥 {}", JSONObject.toJSONString(map), zfChannel.getThirdMerchantPrivateKey());
            encreptData =  aesUtil.encryptBy(JSONObject.toJSONString(map), zfChannel.getThirdMerchantPrivateKey());
        }catch (Exception e){
            return  null;
        }
        JSONObject requeustJson = new JSONObject();
        requeustJson.put("id", zfChannel.getThirdMerchantId());
        requeustJson.put("data", encreptData);
        try {
            log.info("单号 {} 开始请求 {}  参数 {}",zfRecharge.getMerchantOrderNo(),"https://biz.ib23u.com/api/biz/place_deposit_order", JSONObject.toJSONString(requeustJson));
            String reponse = "";
            if(zfRecharge.getChannelId().equals(26)){
                reponse = HttpClientUtil.doPostJson("https://biz.ib23u.com/api/biz/place_deposit_order", JSONObject.toJSONString(requeustJson));
            }else {
                reponse = HttpClientUtil.doPostJson("https://9ec2518d.zhaocai202403.xyz/api/biz/place_deposit_order", JSONObject.toJSONString(requeustJson));
            }
            log.info("请求返回数据 {}", reponse);
            String decrypt = aesUtil.decryptBy(reponse, zfChannel.getThirdMerchantPrivateKey());
            log.info("解密返回数据 {}", decrypt);
            JSONObject jsonObject = JSONObject.parseObject(decrypt);
            log.info("单号 {} 请求结果 {}", zfRecharge.getMerchantOrderNo(), jsonObject);

            if(jsonObject.getInteger("code" ) == 0){
                TreeMap<String, Object> map1 = new TreeMap<>();
                map1.put("merchant_order_no", zfRecharge.getMerchantOrderNo());
                map1.put("order_no", zfRecharge.getOrderNo());
                map1.put("pay_amount", zfRecharge.getPayAmount());
                map1.put("payurl",jsonObject.getString("url"));
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
