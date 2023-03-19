package com.code.baseservice.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TronapiUtil {

    static String  address = "TUD4YXYdj2t1gP5th3A7t97mx1AUmrrQRt";

    public  static  String getAccountRecordd(){
        String path = "https://api.shasta.trongrid.io/v1/accounts/"+address+"/transactions/trc20";
        Map<String , Object> map = new HashMap<>();
        map.put("only_to", true);
        map.put("limit", 50);
        map.put("order_by", "block_timestamp,asc");
        map.put("max_timestamp", new Date().getTime());
        String response =  HttpClientUtil.doGet(path);
        return  response;
    }

    public static void main(String[] args) {
        String response =  getAccountRecordd();
        System.out.print(response);
    }
}
