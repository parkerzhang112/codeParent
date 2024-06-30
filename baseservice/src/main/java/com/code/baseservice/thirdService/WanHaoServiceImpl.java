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
import com.code.baseservice.util.MD5Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

@Slf4j
@Service("WanHao")
public class WanHaoServiceImpl implements BaseService {


    @Value("${app.viewurl:}")
    private String viewUrl;

    @Autowired
    private ZfRechargeService zfRechargeService;

    private String domain = "";


    @Override
    public String notify(ZfRecharge zfRecharge,ZfChannel zfChannel, Map<String,Object> map) {
        log.info("万豪通知 {} 订单数据{}",map, zfRecharge);
        JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(map));
        if(!jsonObject.getString("source_order_id").equals(zfRecharge.getOrderNo())){
            return "fail";
        }
        if(jsonObject.getInteger("status") ==1){
            zfRecharge.setOrderStatus(2);
            zfRecharge.setPaidAmount(zfRecharge.getPayAmount());
            zfRechargeService.paidOrder(zfRecharge);
            return "success";
        }
        return "fail";
    }

    public static void main(String[] args) {
        Integer a = new Integer("895466");
        System.out.println(a == 895466);
    }

    @Override
    public JSONObject create(ZfChannel zfChannel, ZfRecharge zfRecharge) {
        log.info("开始创建万豪 {}", zfRecharge);
        Map<String, String> map = new HashMap<>();
        map.put("merchant_id", zfChannel.getThirdMerchantId());
        map.put("payment_way", "zfb");
        map.put("client_ip", "127.0.0.1");
        map.put("goods_name", "zhangsan");
        map.put("realname", zfRecharge.getPayName() == null ? "张三":zfRecharge.getPayName());
        map.put("order_amount", String.valueOf(zfRecharge.getPayAmount().setScale(0)));
        map.put("source_order_id", zfRecharge.getOrderNo());
        map.put("notify_url", "http://afd7895.cn/recharge/notify/"+ zfRecharge.getOrderNo());
        map.put("return_url", "http://afd7895.cn/recharge/notify/"+ zfRecharge.getOrderNo());
        map.put("format", "json");
        String sign_str  = "client_ip="+map.get("client_ip")+

                "&format=json&goods_name=" + map.get("goods_name")+
                "&merchant_id="+ map.get("merchant_id")+
                "&notify_url="+ map.get("notify_url")+
                "&order_amount="+ map.get("order_amount")+
                "&payment_way="+ map.get("payment_way")+
                "&realname="+ map.get("realname")+
                "&return_url="+ map.get("return_url")+
                "&source_order_id="+ map.get("source_order_id")+
                "&token="+  zfChannel.getThirdMerchantPrivateKey();
        String signStr =  MD5Util.getMD5Str(sign_str);
        map.put("sign", signStr);
        try {
            log.info("单号 {} 开始请求 {}  参数 {} 加密字符串 {} 结果{}",zfRecharge.getMerchantOrderNo(),"https://xahnob7yqke3.hipay.one/pay/pay/createpay", JSONObject.toJSONString(map), sign_str, signStr);
            String reponse = "";
            reponse = HttpClientUtil.doPost("https://fe49b48e.bopay8.com/Pay", map);
            log.info("请求返回数据 {}", JSONObject.parseObject(reponse));
            JSONObject jsonObject = JSONObject.parseObject(reponse);
            log.info("单号 {} 请求结果 {}", zfRecharge.getMerchantOrderNo(), jsonObject);
            if(jsonObject.getInteger("code" ) == 200){
                TreeMap<String, Object> map1 = new TreeMap<>();
                map1.put("merchant_order_no", zfRecharge.getMerchantOrderNo());
                map1.put("order_no", zfRecharge.getOrderNo());
                map1.put("pay_amount", zfRecharge.getPayAmount());
                map1.put("payurl",jsonObject.getJSONObject("payment").getString("pay_url"));
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
