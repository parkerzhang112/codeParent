package com.code.baseservice.util;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Slf4j
public class AesEncryptionUtil {

    // 定义 AES 加密模式
    private static final String ALGORITHM = "AES";

    private static final String FIXED_KEY = "PJeoFztvfrrMorBi"; //

    // 加密
    public static String encrypt(String plainText)  {
        try {
            // 生成 AES 密钥
            SecretKey secretKey = getSecretKeyFromString(FIXED_KEY);
            Cipher cipher = Cipher.getInstance(ALGORITHM); // 获取 AES 加密算法
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);   // 初始化加密模式
            byte[] encryptedBytes = cipher.doFinal(plainText.getBytes()); // 执行加密
            return Base64.getEncoder().encodeToString(encryptedBytes); // 返回加密后的 Base64 编码字符串
        }catch (Exception e){
            log.info("加密异常", e);
            throw new RuntimeException(e);
        }

    }

    // 解密
    public static String decrypt(String encryptedText) throws Exception {
        try {
            // 生成 AES 密钥
            SecretKey secretKey = getSecretKeyFromString(FIXED_KEY);
            Cipher cipher = Cipher.getInstance(ALGORITHM); // 获取 AES 解密算法
            cipher.init(Cipher.DECRYPT_MODE, secretKey);   // 初始化解密模式
            byte[] decodedBytes = Base64.getDecoder().decode(encryptedText); // Base64 解码
            byte[] decryptedBytes = cipher.doFinal(decodedBytes); // 执行解密
            return new String(decryptedBytes); // 返回解密后的字符串
        }catch (Exception e){
            log.info("加密异常", e);
            throw e;
        }
    }


    public static SecretKey getSecretKeyFromString(String key) throws Exception {
        byte[] keyBytes = key.getBytes("UTF-8");
        return new SecretKeySpec(keyBytes, ALGORITHM);
    }

    // 从字节数组生成 SecretKey (用于存储和重用密钥)
    public static SecretKey getSecretKeyFromBytes(byte[] keyBytes) {
        return new SecretKeySpec(keyBytes, ALGORITHM);
    }

    public static void main(String[] args) throws Exception {
        // 原始数据
        String plainText = "Hello, this is a secret message!";

        // 生成 AES 密钥
        SecretKey secretKey = getSecretKeyFromString(FIXED_KEY);

        // 加密
        String encryptedText = encrypt(plainText);
        System.out.println("加密后的字符串: " + encryptedText);

        // 解密
        String decryptedText = decrypt(encryptedText);
        System.out.println("解密后的字符串: " + decryptedText);
    }
}
