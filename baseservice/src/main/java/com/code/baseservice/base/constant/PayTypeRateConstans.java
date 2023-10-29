package com.code.baseservice.base.constant;

import java.util.HashMap;
import java.util.Map;

public class PayTypeRateConstans {
    public static String getRateString(Integer code){
        Map<Integer,String> map = new HashMap<>();
        map.put(0, "");
        map.put(1, "_trans");
        map.put(2, "_s_code");
        map.put(3, "_s_shuzi");
        return map.get(code);
    }

}
