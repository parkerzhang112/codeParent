package com.code.baseservice.util;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.AlgorithmParameters;
import java.security.Security;
import java.util.Base64;

public class AesUtil {
	
    private String ALGO = "AES";
    private byte[] iv;
	private String securityKey = "";
	private String method = "AES/CBC/PKCS5Padding";
    
    public AesUtil()  {
    	this.securityKey = "fdgcTcz4LdJYD6fwRmbFLDgtXQmjetEx";
    	this.iv = this.initIV("1234567890123456");
    }
    
	/**
     * run test
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
    	
    	String method = "AES/CBC/PKCS5Padding";
    	String securityKey16 = "Skesj(eE%32sLOap";
    	String securityKey24 = "Skesj(eE%32sLOapA9e2snEw";
    	String securityKey32 = "Skesj(eE%32sLOapA9e2snEwEeopsWui";
    	String iv = "1234567890123456";
    	String key = "O4IlrI342YmeHKeA";
    	String plainText = "{\"account\":\"\",\"amount\":3000,\"label\":\"\",\"merchantId\":\"68490619\",\"nonce\":\"9otp\",\"notify_url\":\"https://bjy6688.top/recharge/json_notify/DD20240115215855BKM0i\",\"orderId\":\"DD20240115215855BKM0i\",\"orderTime\":\"2024-01-15 21:58:57\",\"payer\":\"'\",\"postscript\":\"\",\"terminalType\":\"PC\",\"timestamp\":1705327137785,\"useCounter\":true,\"userId\":\"\"}";
        AesUtil aes = new AesUtil();
        System.out.println("原始数据"+ plainText);
        String cipherText = ( (aes.encryptBy(plainText,key )));//����java��AES/CBC/NoPadding����
        System.out.println("加密数据"+cipherText);
//        String outPlainText =  aes.decrypt(URLDecoder.decode(URLDecoder.decode( cipherText)));//����
//        System.out.println("解密数据"+outPlainText);
    }
    
    /**
     * ����
     * @param plainText
     * @return
     * @throws Exception
     */
    public String encrypt(String plainText) throws Exception {
        try {
            Cipher cipher = Cipher.getInstance(this.method);
            byte[] dataBytes = plainText.getBytes("UTF-8");
            SecretKeySpec keyspec = new SecretKeySpec(this.securityKey.getBytes("UTF-8"), ALGO);
            IvParameterSpec ivspec = new IvParameterSpec(iv);
            cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
            byte[] encrypted = cipher.doFinal(dataBytes);
            String EncStr = (new BASE64Encoder()).encode(encrypted);
            return EncStr;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * ����
     * @param plainText
     * @return
     * @throws Exception
     */
    public String encryptBy(String plainText, String key) throws Exception {
            try {
                if (key == null || key.length() != 16) {
                    return null;
                }
                byte[] dataByte = plainText.getBytes();
                byte[] keyByte = key.getBytes();
                byte[] ivByte = key.getBytes();
                String encryptedData;
                Security.addProvider(new BouncyCastleProvider());
//指定算法，模式，填充方式，创建一个 Cipher
                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding",
                        "BC");
//生成 Key 对象
                SecretKeySpec sKeySpec = new SecretKeySpec(keyByte, "AES");
//把向量初始化到算法参数
                AlgorithmParameters params =
                        AlgorithmParameters.getInstance("AES");
                params.init(new IvParameterSpec(ivByte));
//指定用途，密钥，参数 初始化 Cipher 对象
                cipher.init(Cipher.ENCRYPT_MODE, sKeySpec, params);
//指定加密
                byte[] result = cipher.doFinal(dataByte);
//对结果进行 Base64 编码，否则会得到一串乱码，不便于后续操作
                Base64.Encoder encoder = Base64.getEncoder();
                encryptedData = encoder.encodeToString(result);
                return encryptedData;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;

    }


    public  String decryptBy(String encryptStr, String key) {
        try {
            if (key == null || key.length() != 16) {
                return null;
            }
//解密之前先把 Base64 格式的数据转成原始格式
            Base64.Decoder decoder = Base64.getDecoder();
            byte[] dataByte = decoder.decode(encryptStr);
            byte[] keyByte = key.getBytes();
            byte[] ivByte = key.getBytes();
            String data;
            Security.addProvider(new BouncyCastleProvider());
//指定算法，模式，填充方法 创建一个 Cipher 实例
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding",
                    "BC");
//生成 Key 对象
            SecretKeySpec sKeySpec = new SecretKeySpec(keyByte, "AES");
//把向量初始化到算法参数
            AlgorithmParameters params =
                    AlgorithmParameters.getInstance("AES");
            params.init(new IvParameterSpec(ivByte));
//指定用途，密钥，参数 初始化 Cipher 对象
            cipher.init(Cipher.DECRYPT_MODE, sKeySpec, params);
//执行解密
            byte[] result = cipher.doFinal(dataByte);
//解密后转成字符串
            data = new String(result);
            return data;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * ����
     * @param cipherText
     * @return
     * @throws Exception
     */
    public String decrypt(String cipherText) throws Exception {
        try {
            byte[] encrypted1 = (new BASE64Decoder()).decodeBuffer(cipherText);
            Cipher cipher = Cipher.getInstance(this.method);
            SecretKeySpec keyspec = new SecretKeySpec(this.securityKey.getBytes("UTF-8"), ALGO);
            IvParameterSpec ivspec = new IvParameterSpec(iv);
            cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);
            byte[] original = cipher.doFinal(encrypted1);
            String originalString = new String(original, "UTF-8");
            return originalString.trim();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

	/**
	 * ��ʼ�����ķ�����ȫ��Ϊ0
	 * �����д���ʺ��������㷨��AES�㷨IVֵһ����128λ��(16�ֽ�)
	 */
	private byte[] initIV(String IVString) {
	    try {
	    	return IVString.getBytes("UTF-8");
	    } catch (Exception e) {
	        int blockSize = 16;
	        byte[] iv = new byte[blockSize];
	        for (int i = 0; i < blockSize; ++i) {
	            iv[i] = 0;
	        }
	        return iv;
	    }
	}
}