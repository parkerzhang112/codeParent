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
import com.code.baseservice.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.TreeMap;

@Slf4j
@Service("HuiHuang")
public class HuiHuangServiceImpl implements BaseService {


    @Value("${app.viewurl:}")
    private String viewUrl;

    @Autowired
    private ZfRechargeService zfRechargeService;

    private String domain = "";


    @Override
    public String notify(ZfRecharge zfRecharge,ZfChannel zfChannel, Map<String,Object> map) {
        log.info("辉煌通知 {}", map.toString());
        JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(map));
        JSONObject data  = jsonObject.getJSONObject("data");
        if(jsonObject.getInteger("code") == 1){
            if(!data.getString("orderid").equals(zfRecharge.getOrderNo())){
                return "fail";
            }
            if(data.getInteger("state") ==5){
                zfRecharge.setOrderStatus(2);
                zfRecharge.setPaidAmount(zfRecharge.getPayAmount());
                zfRechargeService.paidOrder(zfRecharge);
                return "success";
            }
        }
        return "fail";
    }

    public static void main(String[] args) {
        Integer a = new Integer("895466");
        System.out.println(a == 895466);
    }

    @Override
    public JSONObject create(ZfChannel zfChannel, ZfRecharge zfRecharge) {
        log.info("开始创建辉煌 {}", zfRecharge);
        TreeMap<String, Object> map = new TreeMap<>();
        map.put("recvid", zfChannel.getThirdMerchantId());
        map.put("orderid", zfRecharge.getOrderNo());
        map.put("amount", zfRecharge.getPayAmount().setScale(2));
        map.put("paychannelid", zfChannel.getThirdMerchantPublicKey().isEmpty() ? "601":zfChannel.getThirdMerchantPublicKey());
        map.put("notifyurl", "http://afd7895.cn/recharge/json_notify/"+ zfRecharge.getOrderNo());
        map.put("returnurl", "http://afd7895.cn/recharge/json_notify/"+ zfRecharge.getOrderNo());
        map.put("note", "");
        map.put("memuid", MD5Util.getMD5Str(StringUtil.createRandomStr1(4)));
        String sign_str  = "pay"+map.get("recvid") + map.get("orderid")+ map.get("amount") + zfChannel.getThirdMerchantPrivateKey();
        map.put("sign", MD5Util.getMD5Str(sign_str));
        try {
            log.info("单号 {} 开始请求 {}  参数 {} 加密字符串 {}",zfRecharge.getMerchantOrderNo(),"https://xahnob7yqke3.hipay.one/pay/pay/createpay", JSONObject.toJSONString(map), sign_str);
            String reponse = "";
            reponse = HttpClientUtil.doPostJson("https://xahnob7yqke3.hipay.one/pay/pay/createpay", JSONObject.toJSONString(map));
            log.info("请求返回数据 {}", reponse);
            JSONObject jsonObject = JSONObject.parseObject(reponse);
            log.info("单号 {} 请求结果 {}", zfRecharge.getMerchantOrderNo(), jsonObject);

            if(jsonObject.getInteger("code" ) == 1){
                TreeMap<String, Object> map1 = new TreeMap<>();
                map1.put("merchant_order_no", zfRecharge.getMerchantOrderNo());
                map1.put("order_no", zfRecharge.getOrderNo());
                map1.put("pay_amount", zfRecharge.getPayAmount());
                map1.put("payurl",jsonObject.getJSONObject("data").getString("payurl"));
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
