package com.code.baseservice.base.constant;

import com.code.baseservice.base.enums.PaytypeEnum;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class PayTypeRateConstans {
    public static String getRateString(Integer code, BigDecimal paidAmount){
        Map<Integer,String> map = new HashMap<>();
        map.put(0, "");
        map.put(1, "_trans");
        map.put(2, "_s_code");
        map.put(3, "_s_shuzi");
        map.put(4, "_s_yunshanfu");
        map.put(5, "_yunshanfu");
        map.put(6, "_s_weixin");
        map.put(7, "_weixin");
        map.put(9, "_card");
        map.put(11, "_b_code");
        map.put(12, "_g_code");
        map.put(13, "_d_code");
        if(isAliPay(code)){
            if(paidAmount.compareTo(new BigDecimal("500")) < 0){
                return map.get(PaytypeEnum.SMALL_CODE.getValue());
            }
            if(paidAmount.compareTo(new BigDecimal("500")) > -1
                && paidAmount.compareTo(new BigDecimal("800")) == -1
            ){
                return  map.get(PaytypeEnum.ZHONGCODE.getValue());
            }
            if(paidAmount.compareTo(new BigDecimal("800")) > -1
                && paidAmount.compareTo(new BigDecimal("2000")) == -1
            ){
                return map.get(PaytypeEnum.CODE.getValue());
            }
            if(paidAmount.compareTo(new BigDecimal("2000")) > -1
            ){
                return map.get(PaytypeEnum.CHAOCODE.getValue());
            }
        }
        return map.get(code);
    }

    public static String getMerchantRateString(Integer code, BigDecimal paidAmount){
        Map<Integer,String> map = new HashMap<>();
        map.put(0, "");
        map.put(1, "_trans");
        map.put(2, "_s_code");
        map.put(3, "_s_shuzi");
        map.put(4, "_s_yunshanfu");
        map.put(5, "_yunshanfu");
        map.put(6, "_s_weixin");
        map.put(7, "_weixin");
        map.put(9, "_card");
        map.put(11, "_b_code");
        map.put(12, "_g_code");
        map.put(13, "_d_code");
        return map.get(code);
    }


    public static boolean isAliPay(int pay_type){
        if(PaytypeEnum.CODE.getValue() == pay_type
            || PaytypeEnum.TRANS.getValue() == pay_type
                || PaytypeEnum.SMALL_CODE.getValue() == pay_type
                || PaytypeEnum.CHAOCODE.getValue() == pay_type
                || PaytypeEnum.ZHONGCODE.getValue() == pay_type
        ){
            return true;
        }
        return false;
    }
}
