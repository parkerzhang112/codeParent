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
import com.code.baseservice.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

@Slf4j
@Service("ShengYao")
public class ShengYaoServiceImpl implements BaseService {


    @Value("${app.viewurl:}")
    private String viewUrl;

    @Autowired
    private ZfRechargeService zfRechargeService;

    private String domain = "";


    @Override
    public String notify(ZfRecharge zfRecharge,ZfChannel zfChannel, Map<String,Object> map) {
        JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(map));
        log.info("生垚通知 {} 订单数据{}",jsonObject.toJSONString(), zfRecharge);
        if(!jsonObject.getString("outTradeNo").equals(zfRecharge.getOrderNo())){
            return "FAIL";
        }
        if(jsonObject.getInteger("state") ==1){
            zfRecharge.setOrderStatus(2);
            zfRecharge.setPaidAmount(zfRecharge.getPayAmount());
            zfRechargeService.paidOrder(zfRecharge);
            return "SUCCESS";
        }
        return "FAIL";
    }

    public static void main(String[] args) {
        Integer a = new Integer("895466");
        System.out.println(a == 895466);
    }

    @Override
    public JSONObject create(ZfChannel zfChannel, ZfRecharge zfRecharge) {
        log.info("开始创建圣垚 {}", zfRecharge);
        TreeMap<String, Object> map = new TreeMap<>();
        map.put("mchId", zfChannel.getThirdMerchantId());
        map.put("wayCode", zfChannel.getThirdMerchantPublicKey());
        map.put("subject", StringUtil.createRandomStr1(4));
        map.put("outTradeNo", zfRecharge.getOrderNo());
        map.put("amount", zfRecharge.getPayAmount().setScale(0).multiply(new BigDecimal("100").setScale(0)));
        map.put("clientIp", "210.73.10.148");
        map.put("notifyUrl", "http://afd7895.cn/recharge/json_notify/"+ zfRecharge.getOrderNo());
        map.put("reqTime",new Date().getTime());
        String sign_str = new CommonUtil().getSign(map)+ "key="+zfChannel.getThirdMerchantPrivateKey();
        map.put("sign", MD5Util.getMD5Str(sign_str));
        try {
            log.info("单号 {} 开始请求 {}  参数 {} 加密字符串 {}",zfRecharge.getMerchantOrderNo(),"https://yangfanpayjm944i.zzbbm.xyz/api/pay/unifiedorder", JSONObject.toJSONString(map), sign_str);
            String reponse = "";
            reponse = HttpClientUtil.doPostJson("https://yangfanpayjm944i.zzbbm.xyz/api/pay/unifiedorder", JSONObject.toJSONString(map));
            log.info("请求返回数据 {}", reponse);
            JSONObject jsonObject = JSONObject.parseObject(reponse);
            log.info("单号 {} 请求结果 {}", zfRecharge.getMerchantOrderNo(), jsonObject);
            if(jsonObject.getInteger("code" ) == 0){
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
        }
    }



    @Override
    public JSONObject create(ZfChannel zfChannel, ZfWithdraw zfWithdraw) {
        return new JSONObject();
    }

    @Override
    public JSONObject queryByWithdraw(ZfChannel zfChannel, ZfWithdraw zfWithdraw) {
        return new JSONObject();
    }
}
