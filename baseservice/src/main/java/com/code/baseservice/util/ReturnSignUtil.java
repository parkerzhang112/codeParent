package com.code.baseservice.util;

import com.alibaba.fastjson.JSONObject;
import com.code.baseservice.base.constant.BankTypeConstans;
import com.xiagao.baseservice.dto.api.QueryParams;
import com.xiagao.baseservice.dto.api.RechareParams;
import com.xiagao.baseservice.dto.api.TransferParams;
import com.xiagao.baseservice.entity.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.util.TreeMap;

@Slf4j
public class ReturnSignUtil {

    @Value("${app.viewurl:}")
    private static String viewUrl;


    public static JSONObject defalutRechargeSign(XCard xCard, XRecharge xRecharge, XMerchant xMerchant){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("card_account", xCard.getCardNum());
        jsonObject.put("card_type", xCard.getCardType());
        jsonObject.put("card_type_name", BankTypeConstans.getBankTypeName(xCard.getCardType()));
        jsonObject.put("name", xCard.getCardName());
        jsonObject.put("card_address", xCard.getCardAddress());
        jsonObject.put("order_no", xRecharge.getOrderNo());
        jsonObject.put("merchant_order_no", xRecharge.getMerchantOrderNo());
        jsonObject.put("pay_amount", xRecharge.getPayAmount());
        String sign_str = "card_account=" + xCard.getCardNum() + "&name=" + xCard.getCardName()
                + "&card_address=" + xCard.getCardAddress() + "&card_type=" + xCard.getCardType()
                + "&order_no=" + xRecharge.getOrderNo() + "&merchant_order_no=" + xRecharge.getMerchantOrderNo()
                + "&pay_amount=" + xRecharge.getPayAmount()
                + "&key=" + xMerchant.getKey();
        jsonObject.put("payurl", viewUrl+"/recharge/order/"+xRecharge.getOrderNo());
        String sign = MD5Util.getMD5Str(sign_str);
        log.info("订单号 {}  签名字符串 {} ", xRecharge.getOrderNo(), sign_str);
        jsonObject.put("sign", sign);
        jsonObject.put("remark", xRecharge.getRemark());
        return jsonObject;
    }

    public static JSONObject bDRechargeSign(XCard xCard,XRecharge xRecharge, XMerchant xMerchant){
        TreeMap<String, Object>  map = new TreeMap<>();
        map.put("card_number", xCard.getCardNum());
        map.put("bank", BankTypeConstans.getBankTypeName(xCard.getCardType()));
        map.put("name", xCard.getCardName());
        map.put("bank_of_deposit", xCard.getCardAddress());
        map.put("order_no", xRecharge.getMerchantOrderNo());
        map.put("pay_amount", xRecharge.getPayAmount());
        String sign_str = new CommonUtil().getSign(map);
        sign_str = sign_str.concat("key=".concat(xMerchant.getKey()));
        String sign =  MD5Util.getMD5Str(sign_str).toUpperCase();
        map.put("payurl", viewUrl+"/recharge/order/"+xRecharge.getOrderNo());
        log.info("订单号 {}  签名字符串 {} ", xRecharge.getOrderNo(), sign_str);
        map.put("sign", sign);
        map.put("remark", xRecharge.getRemark());
        return new JSONObject(map);
    }

    public static JSONObject bDTransSign(TransferParams transParams,  XMerchant xMerchant){
        TreeMap<String, Object>  map = new TreeMap<>();
        map.put("order_no", transParams.getMerchant_order_no());
        map.put("pay_amount",transParams.getPay_amount());
        String sign_str = new CommonUtil().getSign(map);
        sign_str = sign_str.concat("key=".concat(xMerchant.getKey()));
        String sign =  MD5Util.getMD5Str(sign_str).toUpperCase();
        log.info("订单号 {}  签名字符串 {} ", transParams.getMerchant_order_no(), sign_str);
        map.put("sign", sign);
        map.put("remark", transParams.getRemark());
        return new JSONObject(map);
    }

    public static JSONObject bDViewSign(XMerchant xMerchant, XRecharge xRecharge, XTransfer xTransfer){
        TreeMap<String, Object>  map = new TreeMap<>();
        if(xRecharge != null){
            Integer OrderStatus = xRecharge.getOrderStatus().equals(4) ? 2:xRecharge.getOrderStatus() ;

            map.put("mcc", xRecharge.getMerchantId());
            map.put("order_no", xRecharge.getMerchantOrderNo());
            map.put("order_status", OrderStatus);
            map.put("pay_amount", xRecharge.getPayAmount());
            map.put("paid_amount", xRecharge.getPaidAmount());
        }else {
            map.put("mcc", xTransfer.getMerchantId());
            map.put("order_no", xTransfer.getMerchantOrderNo());
            map.put("order_status", xTransfer.getOrderStatus());
            map.put("pay_amount", xTransfer.getPayAmount());
            map.put("paid_amount", xTransfer.getPaidAmount());
        }
        String sign_str = new CommonUtil().getSign(map);
        sign_str = sign_str.concat("key=".concat(xMerchant.getKey()));
        String sign =  MD5Util.getMD5Str(sign_str).toUpperCase();
        log.info("订单号 {}  签名字符串 {} ", map.get("order_no"), sign_str);
        map.put("sign", sign);
        return new JSONObject(map);
    }

///CJ支付的
    public static JSONObject cJRechargeSign(XCard xCard,XRecharge xRecharge, XMerchant xMerchant){
        TreeMap<String, Object>  map = new TreeMap<>();
        map.put("bank_card_number", xCard.getCardNum());
        map.put("bank_name", BankTypeConstans.getBankTypeName(xCard.getCardType()));
        map.put("account_of_pople", xCard.getCardName());
        map.put("bank_of_deposit", xCard.getCardAddress());
        map.put("order_no", xRecharge.getMerchantOrderNo());
        map.put("order_pay_amount", xRecharge.getPayAmount());
        String sign_str = new CommonUtil().getSign(map);
        sign_str = sign_str.concat("key=".concat(xMerchant.getKey()));
        String sign =  MD5Util.getMD5Str(sign_str).toUpperCase();
        map.put("payurl", viewUrl+"/recharge/order/"+xRecharge.getOrderNo());
        log.info("订单号 {}  签名字符串 {} ", xRecharge.getOrderNo(), sign_str);
        map.put("sign", sign);
        map.put("remark", xRecharge.getRemark());
        return new JSONObject(map);
    }

    public static JSONObject cJTransSign(TransferParams transParams,  XMerchant xMerchant){
        TreeMap<String, Object>  map = new TreeMap<>();
        map.put("order_no", transParams.getMerchant_order_no());
        map.put("order_pay_amount",transParams.getPay_amount());
        String sign_str = new CommonUtil().getSign(map);
        sign_str = sign_str.concat("key=".concat(xMerchant.getKey()));
        String sign =  MD5Util.getMD5Str(sign_str).toUpperCase();
        log.info("订单号 {}  签名字符串 {} ", transParams.getMerchant_order_no(), sign_str);
        map.put("sign", sign);
        map.put("remark", transParams.getRemark());
        return new JSONObject(map);
    }

    public static JSONObject cJViewSign(XMerchant xMerchant, XRecharge xRecharge, XTransfer xTransfer){
        TreeMap<String, Object>  map = new TreeMap<>();
        if(xRecharge != null){
            Integer OrderStatus = xRecharge.getOrderStatus().equals(4) ? 2:xRecharge.getOrderStatus() ;
            map.put("mcc_id", xRecharge.getMerchantId());
            map.put("order_no", xRecharge.getMerchantOrderNo());
            map.put("order_status", OrderStatus);
            map.put("order_pay_amount", xRecharge.getPayAmount());
            map.put("order_paid_amount", xRecharge.getPaidAmount());
        }else {
            map.put("mcc_id", xTransfer.getMerchantId());
            map.put("order_no", xTransfer.getMerchantOrderNo());
            map.put("order_status", xTransfer.getOrderStatus());
            map.put("order_pay_amount", xTransfer.getPayAmount());
            map.put("order_paid_amount", xTransfer.getPaidAmount());
        }
        String sign_str = new CommonUtil().getSign(map);
        sign_str = sign_str.concat("key=".concat(xMerchant.getKey()));
        String sign =  MD5Util.getMD5Str(sign_str).toUpperCase();
        log.info("订单号 {}  签名字符串 {} ", map.get("order_no"), sign_str);
        map.put("sign", sign);
        return new JSONObject(map);
    }

}
