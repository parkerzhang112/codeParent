package com.code.baseservice.base.constant;

import java.util.*;

public class BankTypeConstans {

    public static String getBankTypeName(String code){
        Map<String,String> map = new HashMap<>();
        map.put("ICBC", "中国工商银行");
        map.put("ABC", "农业银行");
        map.put("GDB", "广发银行");
        map.put("SPDB", "浦发银行");
        map.put("ECITIC", "中信银行");
        map.put("SPABANK", "平安银行");
        map.put("PSBC", "中国邮政");
        map.put("CEBB", "光大银行");
        map.put("CMBC", "民生银行");
        map.put("CMB", "招商银行");
        map.put("BOC", "中国银行");
        map.put("HNRCC", "湖南农村信用社");
        map.put("CSCB", "长沙银行");
        map.put("CCB", "建设银行");
        map.put("GDRCC", "广东农信");
        map.put("GDRC", "广东农信");
        map.put("LZYH", "辽宁农村信用社");
        map.put("RCB", "佛山农商");
        map.put("SFDB", "南海农商银行");
        map.put("CHANGANBANK", "长安银行");
        map.put("HNNCYH", "河南农村信用社");
        map.put("BJNSYH", "北京农村商业银行");
        map.put("HBNCYH", "湖北农村信用社");
        map.put("AHNCYH", "安徽农村信用社");
        map.put("YNNCYH", "云南农村信用社");
        map.put("HNNX", "海南农村信用社");
        map.put("CIB", "兴业银行");
        map.put("HEBYH", "哈尔滨银行");
        map.put("HEBNX", "哈尔滨农村信用社");
        map.put("JSNX","江苏省农村商业银行");
        map.put("CSNS","常熟农商银行");
        map.put("ZJGNX", "张家港农村商业银行");
        map.put("DHCZ", "张家港农村商业银行");
        map.put("DHCZ", "张家港农村商业银行");
        map.put("HLJNX", "黑龙江信用社");
        map.put("SRBANK", "上饶银行");
        map.put("JJBANK", "九江银行");
        map.put("JXNX", "江西农村信用社");
        map.put("NJB", "南京银行");
        map.put("SSZBANK", "石嘴山银行");
        map.put("NXBANK", "宁夏银行");
        map.put("HXB", "华夏银行");
        map.put("BCOM", "交通银行");
        map.put("JSBANK", "江苏银行");
        return map.get(code);
    }

    public static boolean isUnquine(String bankcode){
        String bankcodes = "JSBANK";
        List<String> list = Arrays.asList( bankcodes.split(","));
        return  list.contains(bankcode);
    }


    public static List<String> getPriorityBank(){
        List<String> list = new ArrayList<>();
        list.add("HBNX");
        return list;
    }
}
