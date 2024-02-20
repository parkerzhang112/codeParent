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
import java.util.TreeMap;

@Slf4j
@Service("ZhaoCai")
public class ZhaoCaiServiceImpl implements BaseService {


    @Value("${app.viewurl:}")
    private String viewUrl;

    @Autowired
    private ZfRechargeService zfRechargeService;

    private String domain = "";


    @Override
    public String notify(ZfRecharge zfRecharge,ZfChannel zfChannel, Map<String,Object> map) {
        log.info("招财通知 {}", map.toString());
        JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(map));
        if(jsonObject.getString("callbacks").equals("CODE_SUCCESS")){
            if(!jsonObject.getString("out_trade_no").equals(zfRecharge.getOrderNo())){
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
        log.info("开始创建招财 {}", zfRecharge);
        TreeMap<String, Object> map = new TreeMap<>();
        map.put("appid", zfChannel.getThirdMerchantId());
        map.put("pay_type", "alipay_tra");
        map.put("out_trade_no", zfRecharge.getOrderNo());
        map.put("amount", zfRecharge.getPayAmount().setScale(2));
        map.put("out_uid", StringUtil.createRandomStr1(4));
        map.put("version", "v2.0");
        map.put("error_url", "http://afd7895.cn/recharge/notify/"+ zfRecharge.getOrderNo());
        map.put("callback_url", "http://afd7895.cn/recharge/notify/"+ zfRecharge.getOrderNo());
        map.put("success_url", "http://afd7895.cn/recharge/notify/"+ zfRecharge.getOrderNo());
        String signTemp = new CommonUtil().getSign(map) + "key=" + zfChannel.getThirdMerchantPrivateKey();
        map.put("sign",MD5Util.getMD5Str(signTemp).toUpperCase());
        try {
            log.info("单号 {} 开始请求 {}  参数 {} 签名 {}",zfRecharge.getMerchantOrderNo(),"https://9ec2518d.zhaocai202403.xyz/index/unifiedorder", JSONObject.toJSONString(map), signTemp);
            String reponse = "";
            reponse = HttpClientUtil.doPostJson("https://9ec2518d.zhaocai202403.xyz/index/unifiedorder", JSONObject.toJSONString(map));
            log.info("请求返回数据 {}", reponse);
            log.info("单号 {} 请求结果 {}", zfRecharge.getMerchantOrderNo(), reponse);
            JSONObject jsonObject = JSONObject.parseObject(reponse);
            if(jsonObject.getInteger("code" ) == 200){
                TreeMap<String, Object> map1 = new TreeMap<>();
                map1.put("merchant_order_no", zfRecharge.getMerchantOrderNo());
                map1.put("order_no", zfRecharge.getOrderNo());
                map1.put("pay_amount", zfRecharge.getPayAmount());
                map1.put("payurl",jsonObject.getJSONObject("data").getString("url"));
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
