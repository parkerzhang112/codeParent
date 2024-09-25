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

import java.util.*;

@Slf4j
@Service("JuHeYiMa")
public class JuHeYiMaServiceImpl implements BaseService {


    @Value("${app.viewurl:}")
    private String viewUrl;

    @Autowired
    private ZfRechargeService zfRechargeService;

    private String domain = "";


    @Override
    public String notify(ZfRecharge zfRecharge,ZfChannel zfChannel, Map<String,Object> map) {
        log.info("聚合易码通知 {}", map.toString());
        JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(map));
        if(jsonObject.getString("out_trade_no").equals(zfRecharge.getOrderNo()) && jsonObject.getString("trade_status").equals("TRADE_SUCCESS")){
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
    // 方法：对 Map 的键按 ASCII 排序并拼接成查询字符串
    public static String buildSortedQueryString(Map<String, String> map) {
        // 获取 Map 的键并放入 List 中
        List<String> keys = new ArrayList<>(map.keySet());

        // 对键进行排序
        Collections.sort(keys);

        // 拼接结果字符串
        StringBuilder queryString = new StringBuilder();

        for (String key : keys) {
            if (queryString.length() > 0) {
                queryString.append("&");  // 每对 key=value 之间用 & 连接
            }
            queryString.append(key).append("=").append(map.get(key));
        }
        log.info("签名字符串 {}", queryString);
        return queryString.toString();
    }


    @Override
    public JSONObject create(ZfChannel zfChannel, ZfRecharge zfRecharge) {
        log.info("开始创建聚合易码 {}", zfRecharge);
        Map<String, String > map = new TreeMap<>();
        map.put("pid", zfChannel.getThirdMerchantId());
        map.put("type", "wxpay");
        map.put("out_trade_no", zfRecharge.getOrderNo());
        map.put("notify_url", "http://afd7895.cn/recharge/json_notify/"+ zfRecharge.getOrderNo());
        map.put("name", "张三");
        map.put("money", zfRecharge.getPayAmount().setScale(2).toString());
        map.put("clientip", "127.0.0.1");
        String sigin_srt = buildSortedQueryString(map) + zfChannel.getThirdMerchantPrivateKey();
        log.info("签名字符串 {}", sigin_srt);
        map.put("sign_type", "MD5");
        map.put("sign", MD5Util.getMD5Str(sigin_srt));
        log.info("加密参数 {} 加密密钥 {}", JSONObject.toJSONString(map), zfChannel.getThirdMerchantPrivateKey());
        try {
            log.info("单号 {} 开始请求 {}  参数 {}",zfRecharge.getMerchantOrderNo(),"https://vip.ssss00.me/mapi.php", JSONObject.toJSONString(map));
            String reponse = "";
            reponse = HttpClientUtil.doPost("https://vip.ssss00.me/mapi.php", map);
            log.info("请求返回数据 {}", reponse);
            JSONObject jsonObject = JSONObject.parseObject(reponse);
            log.info("单号 {} 请求结果 {}", zfRecharge.getMerchantOrderNo(), jsonObject);

            if(jsonObject.getInteger("code" ) == 1){
                TreeMap<String, Object> map1 = new TreeMap<>();
                map1.put("merchant_order_no", zfRecharge.getMerchantOrderNo());
                map1.put("order_no", zfRecharge.getOrderNo());
                map1.put("pay_amount", zfRecharge.getPayAmount());
                map1.put("payurl",jsonObject.getString("payurl"));
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
