package com.code.baseservice.thirdService;

import com.alibaba.fastjson.JSONObject;
import com.code.baseservice.base.enums.ResultEnum;
import com.code.baseservice.base.exception.BaseException;
import com.code.baseservice.dto.payapi.QueryParams;
import com.code.baseservice.dto.payapi.RechareParams;
import com.code.baseservice.entity.ZfChannel;
import com.code.baseservice.entity.ZfRecharge;
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

import java.util.TreeMap;

@Slf4j
@Service("SanShiService")
public class SanShiServiceImpl implements BaseService {


    @Value("${app.viewurl:}")
    private String viewUrl;

    @Autowired
    private ZfRechargeService zfRechargeService;

    private String domain = "http://qw520.top";


    @Override
    public JSONObject notify(QueryParams queryParams) {
        JSONObject jsonObject = new JSONObject();
        try{
            ZfRecharge zfRecharge = zfRechargeService.queryByOrderNo(queryParams.getMerchant_order_no());
            log.info("确认订单 订单号 {}", zfRecharge.getMerchantOrderNo());
            if (zfRecharge.getOrderStatus() ==2 ) {
                log.info("订单已处理 订单号 {}", zfRecharge.getMerchantOrderNo());
                throw new BaseException(ResultEnum.ERROR);
            }
            zfRecharge.setOrderStatus(2);
            zfRecharge.setPaidAmount(zfRecharge.getPayAmount());
            zfRechargeService.paidOrder(zfRecharge);
            jsonObject.put("code", 200);
            jsonObject.put("msg","订单成功");
        }catch (Exception e){
            jsonObject.put("code", 1);
            jsonObject.put("msg","失败");
            log.error("系统异常 {} {}", queryParams.getMerchant_order_no(), e);
        }
        return jsonObject;
    }

    @Override
    public JSONObject create(ZfChannel zfChannel, ZfRecharge zfRecharge) {
        log.info("开始创建三世支付订单 {}", zfRecharge);
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
}
