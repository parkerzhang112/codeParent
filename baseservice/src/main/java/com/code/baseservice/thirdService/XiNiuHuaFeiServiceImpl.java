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

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

@Slf4j
@Service("XiNiuHuaFei")
public class XiNiuHuaFeiServiceImpl implements BaseService {


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
        log.info("开始创建三世支付订单 {}", zfRecharge);
        RechareParams rechareParams1= new RechareParams();
        rechareParams1.setMerchant_order_no(zfRecharge.getOrderNo());
        rechareParams1.setMerchant_id(zfChannel.getThirdMerchantId());
        rechareParams1.setPay_amount(zfRecharge.getPayAmount());
        rechareParams1.setNotify_url(viewUrl+"/recharge/notify"+zfChannel.getChannelCode());
        rechareParams1.setRemark(StringUtil.createRandomStr1(3));
        Map<String, String> map = new TreeMap<>();
        map.put("is_code","1");
        map.put("mch_id", zfChannel.getThirdMerchantId());
        map.put("out_trade_no", zfRecharge.getOrderNo());
        map.put("timestamp", String.valueOf(Math.floor(System.currentTimeMillis()/1000)));
        map.put("total", zfRecharge.getPayAmount().toBigInteger().toString());
        map.put("type", "9006");
        map.put("notify_url", viewUrl+"/recharge/notify/"+zfChannel.getChannelCode());
        String sign_str = new CommonUtil().getSignByMap(map);
        sign_str = sign_str.concat("key="+zfChannel.getThirdMerchantPrivateKey());
        log.info("签名字符串: {}", sign_str);
        String sign =  MD5Util.getMD5Str(sign_str).toUpperCase();
        rechareParams1.setSign(sign);
        map.put("sign", sign);

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
