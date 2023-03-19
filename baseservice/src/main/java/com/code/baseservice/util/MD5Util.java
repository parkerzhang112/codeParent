package com.code.baseservice.util;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {
    
    public static String getMD5Str(String str) {
        byte[] digest = null;
        try {
            MessageDigest md5 = MessageDigest.getInstance("md5");
            digest  = md5.digest(str.getBytes("utf-8"));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //16是表示转换为16进制数
        String md5Str = new BigInteger(1, digest).toString(16);
        while (true){
            if (md5Str.length()<32){
                md5Str = "0" + md5Str;
            }else {
                break;
            }
        }
        return md5Str;
    }

    public static void main(String[] args) {
        String mdst =  getMD5Str("merchant_order_no=TX210425142524507bf3000&merchant_id=10000&key=%VZJJ2H78NY6HKIQ7MRPFKR^06!*T7JC");
        System.out.print(mdst);
    }

}