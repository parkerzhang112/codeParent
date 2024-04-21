package com.code.baseservice.base.constant;

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


        if(code == 2 || code == 11){
            if(paidAmount.compareTo(new BigDecimal("800") ) > -1){
                return "";
            }
        }
        return map.get(code);
    }

}
