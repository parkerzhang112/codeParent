package com.code.baseservice.util;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonUtil {
    public static String getOrderNo(String merchantCode, String orderPrex) {
        String str = "MLPNKOBJIVHICGUXFYDTRSEWAQZ123456789";
        String prex = orderPrex.concat(merchantCode);
        long between = System.currentTimeMillis();
        int needLength = 32- (prex+between).length();
        String random = StringUtil.createRandomStr1(needLength);
        return prex+between+random;
    }


    public static boolean isNumeric(String str){
        for (int i = str.length();--i>=0;){
            if (!Character.isDigit(str.charAt(i))){
                return false;
            }
        }
        return true;
    }

    public static String replaceChinese(String source, String replacement){

        if(StringUtils.isEmpty(source)){
            return null;
        }
        String reg = "[\u4e00-\u9fa5]";

        Pattern pat = Pattern.compile(reg);

        Matcher mat=pat.matcher(source);

        String repickStr = mat.replaceAll(replacement);

        return repickStr;

    }


    public Map<String, Object> sortMapByKey(Map<String, Object> oriMap) {
        if (oriMap == null || oriMap.isEmpty()) {
            return null;
        }
        Map<String, Object> sortedMap = new TreeMap<String, Object>(new Comparator<String>() {
            public int compare(String key1, String key2) {
                int intKey1 = 0, intKey2 = 0;
                try {
                    intKey1 = getInt(key1);
                    intKey2 = getInt(key2);
                } catch (Exception e) {
                    intKey1 = 0;
                    intKey2 = 0;
                }
                return intKey1 - intKey2;
            }});
        sortedMap.putAll(oriMap);
        return sortedMap;
    }

    public static String getSign(Map<String,Object> map){
        ArrayList<String> list = new ArrayList<String>();
        for(Map.Entry<String,Object> entry:map.entrySet()){
            if(entry.getValue()!=""){
                list.add(entry.getKey() + "=" + entry.getValue() + "&");
            }
        }
        int size = list.size();
        String [] arrayToSort = list.toArray(new String[size]);
        Arrays.sort(arrayToSort, String.CASE_INSENSITIVE_ORDER);
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < size; i ++) {
            sb.append(arrayToSort[i]);
        }
        String result = sb.toString();
        return result;
    }

    private int getInt(String str) {
        int i = 0;
        try {
            Pattern p = Pattern.compile("^\\d+");
            Matcher m = p.matcher(str);
            if (m.find()) {
                i = Integer.valueOf(m.group());
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return i;
    }
}

