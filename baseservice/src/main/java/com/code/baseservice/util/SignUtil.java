package com.code.baseservice.util;

import com.code.baseservice.base.constant.ResultEnum;
import com.code.baseservice.base.exception.BaseException;
import com.xiagao.baseservice.dto.api.QueryParams;
import com.xiagao.baseservice.dto.api.RechareParams;
import com.xiagao.baseservice.dto.api.TransferParams;
import lombok.extern.slf4j.Slf4j;

import java.util.TreeMap;

@Slf4j
public class SignUtil {

    public static void defalutSign(RechareParams rechareParams, XMerchant xMerchant){
        String encod_str = "merchant_order_no=" + rechareParams.getMerchant_order_no() + "&merchant_id=" + rechareParams.getMerchant_id()
                + "&pay_amount=" + rechareParams.getPay_amount() + "&name=" + rechareParams.getName()
                + "&notify_url=" + rechareParams.getNotify_url()
                + "&card_type=" + rechareParams.getCard_type() + "&key=" + xMerchant.getKey();
        String sign =  MD5Util.getMD5Str(encod_str);
        log.info("开始验签 签名字符串 {}  加密值 {} 订单号 {}", encod_str, sign, rechareParams.getMerchant_order_no());
        if(!sign.equals(rechareParams.getSign())){
            log.info("当前我方签名 {} 签名字符串  对方签名 {}", sign, encod_str, rechareParams.getSign());
            throw new BaseException(ResultEnum.SIGN_ERROR);
        }
    }

    public static  void xShPay(RechareParams rechareParams, XMerchant xMerchant){
        String encod_str = "xsh_order_no=" + rechareParams.getMerchant_order_no() + "&merchant_id=" + rechareParams.getMerchant_id()
                + "&xsh_amount=" + rechareParams.getPay_amount() + "&xsh_name=" + rechareParams.getName()
                + "&xsh_notify_url=" + rechareParams.getNotify_url()
                + "&xsh_bank_type=" + rechareParams.getCard_type() + "&xsh_key=" + xMerchant.getKey();
        String sign =  MD5Util.getMD5Str(encod_str);
        log.info("开始验签 签名字符串 {}  加密值 {} 订单号 {}", encod_str, sign, rechareParams.getMerchant_order_no());
        if(!sign.equals(rechareParams.getSign())){
            log.info("当前我方签名 {} 签名字符串  对方签名 {}", sign, encod_str, rechareParams.getSign());
            throw new BaseException(ResultEnum.SIGN_ERROR);
        }
    }

    public static void fNtPay(RechareParams rechareParams, XMerchant xMerchant) {

        String encod_str = "merchant_id=" + rechareParams.getMerchant_id()
                + "&order_number=" + rechareParams.getMerchant_order_no()
                + "&order_amount=" + rechareParams.getPay_amount()
                + "&order_notify_url=" + rechareParams.getNotify_url()
                +  "&pay_name=" + rechareParams.getName()
                + "&pay_key=" + xMerchant.getKey();
        String sign =  MD5Util.getMD5Str(encod_str);
        log.info("开始验签 签名字符串 {}  加密值 {} 订单号 {}", encod_str, sign, rechareParams.getMerchant_order_no());
        if(!sign.equals(rechareParams.getSign())){
            log.info("当前我方签名 {} 签名字符串  对方签名 {}", sign, encod_str, rechareParams.getSign());
            throw new BaseException(ResultEnum.SIGN_ERROR);
        }
    }


    public static void bJYPay(RechareParams rechareParams, XMerchant xMerchant) {
        TreeMap<String, Object> map = new TreeMap<>();
        map.put("m_id", rechareParams.getMerchant_id());
        map.put("order", rechareParams.getMerchant_order_no());
        map.put("pay_amount", rechareParams.getPay_amount());
        map.put("notify_url", rechareParams.getNotify_url());
        map.put("pay_name", rechareParams.getName());
        String sign_str = new CommonUtil().getSign(map);
        sign_str = sign_str.concat("key=".concat(xMerchant.getKey()));
        String sign =  MD5Util.getMD5Str(sign_str.toUpperCase());
        log.info("开始验签 签名字符串 {}  加密值 {} 订单号 {}", sign_str, sign, rechareParams.getMerchant_order_no());
        if(!sign.equals(rechareParams.getSign())){
            log.info("当前我方签名 {} 签名字符串  对方签名 {}", sign, sign_str, rechareParams.getSign());
            throw new BaseException(ResultEnum.SIGN_ERROR);
        }
    }

    public static void defalutTransSign(TransferParams transParams, XMerchant xMerchant) {
        String encod_str = "merchant_order_no=" + transParams.getMerchant_order_no() + "&merchant_id=" + transParams.getMerchant_id()
                + "&pay_amount=" + transParams.getPay_amount() + "&card_name=" + transParams.getCard_name()
                + "&notify_url=" + transParams.getNotify_url()
                + "&card_type=" + transParams.getCard_type()
                + "&card_account=" + transParams.getCard_account()
                + "&card_address=" + transParams.getCard_address()
                + "&key=" + xMerchant.getKey();
        String sign = MD5Util.getMD5Str(encod_str);
        if (!sign.equals(transParams.getSign())) {
            log.info("订单号 {} 当前我方签名 {} 签名字符串  对方签名 {}", transParams.getMerchant_order_no(), sign, encod_str, transParams.getSign());
            throw new BaseException(ResultEnum.SIGN_ERROR);
        }

    }

    /**
     * 代付除开
     * @param transParams
     * @param xMerchant
     */
    public static void xShTransPay(TransferParams transParams, XMerchant xMerchant) {
        String encod_str = "xsh_order_no=" + transParams.getMerchant_order_no() + "&merchant_id=" + transParams.getMerchant_id()
                + "&xsh_amount=" + transParams.getPay_amount() + "&xsh_card_name=" + transParams.getCard_name()
                + "&xsh_notify_url=" + transParams.getNotify_url()
                + "&xsh_card_type=" + transParams.getCard_type()
                + "&xsh_card_account=" + transParams.getCard_account()
                + "&xsh_card_address=" + transParams.getCard_address()
                + "&xsh_key=" + xMerchant.getKey();
        String sign = MD5Util.getMD5Str(encod_str);
        if (!sign.equals(transParams.getSign())) {
            log.info("订单号 {} 当前我方签名 {} 签名字符串  对方签名 {}", transParams.getMerchant_order_no(), sign, encod_str, transParams.getSign());
            throw new BaseException(ResultEnum.SIGN_ERROR);
        }
    }

    /**
     * 代付除开
     * @param transParams
     * @param xMerchant
     */
    public static void fNtPay(TransferParams transParams, XMerchant xMerchant) {
        String encod_str =
                 "address=" + transParams.getCard_address()
                 + "&card_name=" + transParams.getCard_name()
                 + "&card_number=" + transParams.getCard_account()
                + "&card_type=" + transParams.getCard_type()
                + "&merchant_id=" + transParams.getMerchant_id()
                + "&order_notify_url=" + transParams.getNotify_url()
                + "&order_number=" + transParams.getMerchant_order_no()
                + "&pay_amount=" + transParams.getPay_amount()
                + "&pay_key=" + xMerchant.getKey();
        String sign = MD5Util.getMD5Str(encod_str);
        if (!sign.equals(transParams.getSign())) {
            log.info("订单号 {} 当前我方签名 {} 签名字符串  对方签名 {}", transParams.getMerchant_order_no(), sign, encod_str, transParams.getSign());
            throw new BaseException(ResultEnum.SIGN_ERROR);
        }
    }

    /**
     * 代付除开
     * @param transParams
     * @param xMerchant
     */
    public static void bJYPay(TransferParams transParams, XMerchant xMerchant) {
        TreeMap<String, Object>  map = new TreeMap<>();
        map.put("address", transParams.getCard_address());
        map.put("card_name", transParams.getCard_name());
        map.put("card_number", transParams.getCard_account());
        map.put("card_type", transParams.getCard_type());
        map.put("m_id", transParams.getMerchant_id());
        map.put("notify_url", transParams.getNotify_url());
        map.put("order", transParams.getMerchant_order_no());
        map.put("pay_amount", transParams.getPay_amount());
        String sign_str = new CommonUtil().getSign(map);
        sign_str = sign_str.concat("key=".concat(xMerchant.getKey()));
        String sign =  MD5Util.getMD5Str(sign_str.toUpperCase());
        if (!sign.equals(transParams.getSign())) {
            log.info("订单号 {} 当前我方签名 {} 签名字符串  对方签名 {}", transParams.getMerchant_order_no(), sign, sign_str, transParams.getSign());
            throw new BaseException(ResultEnum.SIGN_ERROR);
        }
    }


    public static void bJYPay(QueryParams queryParams, XMerchant xMerchant) {
        TreeMap<String, Object>  map = new TreeMap<>();
        map.put("m_id", queryParams.getMerchant_id());
        map.put("order", queryParams.getMerchant_order_no());
        String sign_str = new CommonUtil().getSign(map);
        sign_str = sign_str.concat("key=".concat(xMerchant.getKey()));
        String sign =  MD5Util.getMD5Str(sign_str.toUpperCase());
        if (!sign.equals(queryParams.getSign())) {
            log.info("订单号 {} 当前我方签名 {} 签名字符串  对方签名 {}", queryParams.getMerchant_order_no(), sign, sign_str, queryParams.getSign());
            throw new BaseException(ResultEnum.SIGN_ERROR);
        }
    }

    /**
     * 代付除开
     * @param transParams
     * @param xMerchant
     */
    public static void bDPay(TransferParams transParams, XMerchant xMerchant) {
        TreeMap<String, Object>  map = new TreeMap<>();
        map.put("bank_of_deposit", transParams.getCard_address());
        map.put("account_hodler", transParams.getCard_name());
        map.put("card_number", transParams.getCard_account());
        map.put("bank", transParams.getCard_type());
        map.put("mcc", transParams.getMerchant_id());
        map.put("notify_url", transParams.getNotify_url());
        map.put("order_no", transParams.getMerchant_order_no());
        map.put("order_amount", transParams.getPay_amount());
        String sign_str = new CommonUtil().getSign(map);
        sign_str = sign_str.concat("key=".concat(xMerchant.getKey()));
        String sign =  MD5Util.getMD5Str(sign_str).toUpperCase();
        if (!sign.equals(transParams.getSign())) {
            log.info("订单号 {} 当前我方签名 {} 签名字符串  对方签名 {}", transParams.getMerchant_order_no(), sign, sign_str, transParams.getSign());
            throw new BaseException(ResultEnum.SIGN_ERROR);
        }
    }

    public static void bDPay(RechareParams rechareParams, XMerchant xMerchant) {
        TreeMap<String, Object> map = new TreeMap<>();
        map.put("mcc", rechareParams.getMerchant_id());
        map.put("order_no", rechareParams.getMerchant_order_no());
        map.put("order_amount", rechareParams.getPay_amount());
        map.put("notify_url", rechareParams.getNotify_url());
        map.put("pay_name", rechareParams.getName());
        String sign_str = new CommonUtil().getSign(map);
        sign_str = sign_str.concat("key=".concat(xMerchant.getKey()));
        String sign =  MD5Util.getMD5Str(sign_str).toUpperCase();
        log.info("开始验签 签名字符串 {}  加密值 {} 订单号 {}", sign_str, sign, rechareParams.getMerchant_order_no());
        if(!sign.equals(rechareParams.getSign())){
            log.info("当前我方签名 {} 签名字符串  对方签名 {}", sign, sign_str, rechareParams.getSign());
            throw new BaseException(ResultEnum.SIGN_ERROR);
        }
    }

    public static void bDPay(QueryParams queryParams, XMerchant xMerchant) {
        TreeMap<String, Object>  map = new TreeMap<>();
        map.put("mcc", queryParams.getMerchant_id());
        map.put("order_no", queryParams.getMerchant_order_no());
        String sign_str = new CommonUtil().getSign(map);
        sign_str = sign_str.concat("key=".concat(xMerchant.getKey()));
        String sign =  MD5Util.getMD5Str(sign_str).toUpperCase();
        log.info("订单号 {} 当前我方签名 {} 签名字符串  对方签名 {}", queryParams.getMerchant_order_no(), sign, sign_str, queryParams.getSign());
        if (!sign.equals(queryParams.getSign())) {
            throw new BaseException(ResultEnum.SIGN_ERROR);
        }
    }


    /**
     * 代付除开
     * @param transParams
     * @param xMerchant
     */
    public static void cJPay(TransferParams transParams, XMerchant xMerchant) {
        TreeMap<String, Object>  map = new TreeMap<>();
        map.put("bank_of_deposit", transParams.getCard_address());
        map.put("account_of_pople", transParams.getCard_name());
        map.put("bank_card_number", transParams.getCard_account());
        map.put("bank_name", transParams.getCard_type());
        map.put("mcc_id", transParams.getMerchant_id());
        map.put("notify_url", transParams.getNotify_url());
        map.put("order_no", transParams.getMerchant_order_no());
        map.put("order_pay_amount", transParams.getPay_amount());
        String sign_str = new CommonUtil().getSign(map);
        sign_str = sign_str.concat("key=".concat(xMerchant.getKey()));
        String sign =  MD5Util.getMD5Str(sign_str).toUpperCase();
        if (!sign.equals(transParams.getSign())) {
            log.info("订单号 {} 当前我方签名 {} 签名字符串  对方签名 {}", transParams.getMerchant_order_no(), sign, sign_str, transParams.getSign());
            throw new BaseException(ResultEnum.SIGN_ERROR);
        }
    }

    public static void cJPay(RechareParams rechareParams, XMerchant xMerchant) {
        TreeMap<String, Object> map = new TreeMap<>();
        map.put("mcc_id", rechareParams.getMerchant_id());
        map.put("order_no", rechareParams.getMerchant_order_no());
        map.put("order_pay_amount", rechareParams.getPay_amount());
        map.put("notify_url", rechareParams.getNotify_url());
        map.put("pay_name", rechareParams.getName());
        String sign_str = new CommonUtil().getSign(map);
        sign_str = sign_str.concat("key=".concat(xMerchant.getKey()));
        String sign =  MD5Util.getMD5Str(sign_str).toUpperCase();
        log.info("开始验签 签名字符串 {}  加密值 {} 订单号 {}", sign_str, sign, rechareParams.getMerchant_order_no());
        if(!sign.equals(rechareParams.getSign())){
            log.info("当前我方签名 {} 签名字符串  对方签名 {}", sign, sign_str, rechareParams.getSign());
            throw new BaseException(ResultEnum.SIGN_ERROR);
        }
    }

    public static void cJPay(QueryParams queryParams, XMerchant xMerchant) {
        TreeMap<String, Object>  map = new TreeMap<>();
        map.put("mcc_id", queryParams.getMerchant_id());
        map.put("order_no", queryParams.getMerchant_order_no());
        String sign_str = new CommonUtil().getSign(map);
        sign_str = sign_str.concat("key=".concat(xMerchant.getKey()));
        String sign =  MD5Util.getMD5Str(sign_str).toUpperCase();
        log.info("订单号 {} 当前我方签名 {} 签名字符串  对方签名 {}", queryParams.getMerchant_order_no(), sign, sign_str, queryParams.getSign());
        if (!sign.equals(queryParams.getSign())) {
            throw new BaseException(ResultEnum.SIGN_ERROR);
        }
    }
}
