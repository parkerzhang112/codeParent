package com.code.baseservice.thirdService;

import com.alibaba.fastjson.JSONObject;
import com.code.baseservice.base.enums.ResultEnum;
import com.code.baseservice.base.exception.BaseException;
import com.code.baseservice.dto.payapi.RechareParams;
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

import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

@Slf4j
@Service("AFeiDf")
public class AFeiDfServiceImpl implements BaseService {


    @Value("${app.viewurl:}")
    private String viewUrl;

    @Autowired
    private ZfRechargeService zfRechargeService;

    private String domain = "";


    @Override
    public String notify(ZfRecharge zfRecharge,ZfChannel zfChannel, Map<String,Object> map) {
       return  "success";
//        JSONObject jsonObject = new JSONObject();
//        try{
//            ZfRecharge zfRecharge = zfRechargeService.queryByOrderNo(queryParams.getMerchant_order_no());
//            log.info("确认订单 订单号 {}", zfRecharge.getMerchantOrderNo());
//            if (zfRecharge.getOrderStatus() ==2 ) {
//                log.info("订单已处理 订单号 {}", zfRecharge.getMerchantOrderNo());
//                throw new BaseException(ResultEnum.ERROR);
//            }
//            zfRecharge.setOrderStatus(2);
//            zfRecharge.setPaidAmount(zfRecharge.getPayAmount());
//            zfRechargeService.paidOrder(zfRecharge);
//            jsonObject.put("code", 200);
//            jsonObject.put("msg","订单成功");
//        }catch (Exception e){
//            jsonObject.put("code", 1);
//            jsonObject.put("msg","失败");
//            log.error("系统异常 {} {}", queryParams.getMerchant_order_no(), e);
//        }
//        return jsonObject;
    }


    @Override
    public JSONObject create(ZfChannel zfChannel, ZfRecharge zfRecharge) {
        log.info("开始创建阿飞代付 {}", zfRecharge);
        RechareParams rechareParams1= new RechareParams();
        rechareParams1.setMerchant_order_no(zfRecharge.getOrderNo());
        rechareParams1.setMerchant_id(zfChannel.getThirdMerchantId());
        rechareParams1.setPay_amount(zfRecharge.getPayAmount());
        rechareParams1.setNotify_url(viewUrl+"/recharge/notify");
        rechareParams1.setRemark(StringUtil.createRandomStr1(3));
        TreeMap<String, Object> map = new TreeMap<>();
        map.put("merchant_id", zfChannel.getThirdMerchantId());
        map.put("merchant_order_no", zfRecharge.getOrderNo());
        map.put("pay_amount", zfRecharge.getPayAmount());
        map.put("notify_url", viewUrl+"/recharge/notify");
        String sign_str = new CommonUtil().getSign(map);
        sign_str = sign_str.concat("key="+zfChannel.getThirdMerchantPrivateKey());
        log.info("签名字符串: {}", sign_str);
        String sign =  MD5Util.getMD5Str(sign_str).toUpperCase();
        rechareParams1.setSign(sign);
        try {
            log.info("单号 {} 开始请求 {}  参数 {}",zfRecharge.getMerchantOrderNo(), domain + "/recharge/create", JSONObject.toJSONString(rechareParams1));
            String reponse = HttpClientUtil.doPostJson(domain + "/recharge/create", JSONObject.toJSONString(rechareParams1));
            JSONObject jsonObject = JSONObject.parseObject(reponse);
            log.info("单号 {} 请求结果 {}", zfRecharge.getMerchantOrderNo(), reponse);
            if(jsonObject.getInteger("code" ) == 200){
                return jsonObject.getJSONObject("data");
            }
            throw new BaseException(ResultEnum.ERROR);
        }catch (BaseException e){
            log.error("请求异常 {} 订单号{}", e, zfRecharge.getOrderNo());
            throw new BaseException(ResultEnum.ERROR);
        }
    }

    @Override
    public JSONObject create(ZfChannel zfChannel, ZfWithdraw zfWithdraw) {
        log.info("开始创建阿飞支付订单 {}", zfWithdraw);
        try {
            Map<String, Object> map = new TreeMap<>();
            map.put("mchid", zfChannel.getThirdMerchantId());
            map.put("ordersn", zfWithdraw.getOrderNo());
            map.put("paycode", "1215");
            map.put("money", zfWithdraw.getPayAmount());
            map.put("time", (new Date()).getTime());

            String sign_str = zfChannel.getThirdMerchantId()+"%"+zfWithdraw.getOrderNo()
                    +"%1215%"+zfWithdraw.getPayAmount()+"%"+(new Date()).getTime()
                    +"%"+zfChannel.getThirdMerchantPrivateKey();
            map.put("name", zfWithdraw.getCardName());
            map.put("cardnum", zfWithdraw.getCardAccount());
            map.put("notifyurl","https://bjy6688.top/recharge/json_notify/" + zfWithdraw.getOrderNo());
            log.info("签名字符串 {}", sign_str);
            String sign = MD5Util.getMD5Str(sign_str).toUpperCase();

            map.put("sign", sign);
            log.info("单号 {} 开始请求 {}  参数 {}",zfWithdraw.getMerchantOrderNo(),"http://8.217.168.61:8059/api/order", JSONObject.toJSONString(map));
            String reponse = "";
            //            String reponse = HttpClientUtil.doPostJson("http://8.217.168.61:8059/api/order", JSONObject.toJSONString(map));
            log.info("单号 {} 请求结果 {}", zfWithdraw.getMerchantOrderNo(), reponse);
            JSONObject jsonObject = JSONObject.parseObject(reponse);
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
