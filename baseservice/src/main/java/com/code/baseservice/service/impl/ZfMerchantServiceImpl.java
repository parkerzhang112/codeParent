package com.code.baseservice.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.code.baseservice.base.constant.PayTypeRateConstans;
import com.code.baseservice.base.constant.RedisConstant;
import com.code.baseservice.base.enums.CommonStatusEnum;
import com.code.baseservice.base.enums.ResultEnum;
import com.code.baseservice.base.exception.BaseException;
import com.code.baseservice.dao.ZfMerchantDao;
import com.code.baseservice.dto.backapi.OperaBalanceParams;
import com.code.baseservice.dto.payapi.MerchantParams;
import com.code.baseservice.dto.payapi.QueryParams;
import com.code.baseservice.dto.payapi.TransferParams;
import com.code.baseservice.entity.*;
import com.code.baseservice.service.RedisUtilService;
import com.code.baseservice.service.ZfMerchantService;
import com.code.baseservice.service.ZfMerchantTransService;
import com.code.baseservice.service.ZfWithdrawService;
import com.code.baseservice.util.CommonUtil;
import com.code.baseservice.util.MD5Util;
import com.code.baseservice.util.Telegram;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.TreeMap;

/**
 * (ZfMerchant)表服务实现类
 *
 * @author makejava
 * @since 2023-03-19 23:07:58
 */
@Service("zfMerchantService")
@Slf4j
public class ZfMerchantServiceImpl implements ZfMerchantService {
    @Resource
    private ZfMerchantDao zfMerchantDao;

    @Autowired
    RedisUtilService redisUtilService;

    @Autowired
    private ZfMerchantTransService zfMerchantTransService;

    @Autowired
    private ZfWithdrawService zfWithdrawService;

    /**
     * 通过ID查询单条数据
     *
     * @param merchantId 主键
     * @return 实例对象
     */
    @Override
    public ZfMerchant queryById(Integer merchantId) {
        return this.zfMerchantDao.queryById(merchantId);
    }

    /**
     *
     * @param zfMerchant
     * @return
     */
    @Override
    public int update(ZfMerchant zfMerchant) {
        return zfMerchantDao.update(zfMerchant);
    }

    @Override
    public ZfMerchant vaildMerchant(Integer merchant_id) {
        log.info("验证商户合法性开始 {}", merchant_id);
        ZfMerchant xMerchant =  zfMerchantDao.queryById(merchant_id);
        if(Objects.isNull(xMerchant) && xMerchant.getStatus().equals(CommonStatusEnum.STATR.getValue())){
            log.info("无效商户 {}", merchant_id);
            throw new BaseException(ResultEnum.NO_VAILD_MERCHANT);
        }
        return xMerchant;
    }

    @Override
    public void verifSign(QueryParams queryParams, ZfMerchant zfMerchant) {
        TreeMap<String, Object> map = new TreeMap<>();
        map.put("merchant_id", queryParams.getMerchant_Id());
        map.put("merchant_order_no", queryParams.getMerchant_order_no());
        String sign_str = new CommonUtil().getSign(map);
        sign_str = sign_str.concat("key=".concat(zfMerchant.getKey()));
        String sign =  MD5Util.getMD5Str(sign_str).toUpperCase();
        log.info("订单号 {} 当前我方签名 {} 签名字符串 {}  对方签名 {}", queryParams.getMerchant_order_no(), sign, sign_str, queryParams.getSign());
        if (!sign.equals(queryParams.getSign())) {
            throw new BaseException(ResultEnum.SIGN_ERROR);
        }
    }

    @Override
    public void verifSign(TransferParams transParams, ZfMerchant zfMerchant) {
        TreeMap<String, Object>  map = new TreeMap<>();
        map.put("card_address", transParams.getCard_address());
        map.put("card_name", transParams.getCard_name());
        map.put("card_account", transParams.getCard_account());
        map.put("card_type", transParams.getCard_type());
        map.put("merchant_id", transParams.getMerchant_id());
        map.put("notify_url", transParams.getNotify_url());
        map.put("merchant_order_no", transParams.getMerchant_order_no());
        map.put("pay_amount", transParams.getPay_amount());
        String sign_str = new CommonUtil().getSign(map);
        sign_str = sign_str.concat("key=".concat(zfMerchant.getKey()));
        String sign =  MD5Util.getMD5Str(sign_str).toUpperCase();
        if (!sign.equals(transParams.getSign())) {
            log.info("订单号 {} 当前我方签名 {} 签名字符串  对方签名 {}", transParams.getMerchant_order_no(), sign, sign_str, transParams.getSign());
            throw new BaseException(ResultEnum.SIGN_ERROR);
        }
    }

    @Override
    public void verifMerchantBalance(ZfMerchant zfMerchant, TransferParams transParams) {
        if(zfMerchant.getBalance().compareTo(transParams.getPay_amount())< 0){
            //如果是yb的，则不验证余额
            if(!zfMerchant.getMerchantCode().equals("YB")){
                log.info("订单号 {} 当前有效余额 {} 交易基恩 {}", transParams.getMerchant_order_no(), zfMerchant.getBalance(), transParams.getPay_amount());
                throw new BaseException(ResultEnum.MERCHANT_BALANCE_NO_ENOUGH) ;
            }
        }
    }

    @Override
    public JSONObject query(MerchantParams merchantParams) {
        //验证商户
        ZfMerchant xMerchant = vaildMerchant(merchantParams.getMerchant_id());
        //校对签名
        verifSign(merchantParams, xMerchant);
        //构建返回参数
        JSONObject jsonObject =  buildReuslt(xMerchant);
        return jsonObject;
    }

    @Override
    public void sumMerchantBalance(Integer merchantId, BigDecimal subtract) {
        zfMerchantDao.sumMerchantBalance(merchantId, subtract);
    }

    @Override
    public void warrnBalance(ZfMerchant xMerchant, ZfRecharge zfRecharge, ZfAgent zfAgent) {
        if(xMerchant.getBalance().add(zfRecharge.getPaidAmount()).compareTo(xMerchant.getCreditAmount())>0){
            String key = "merchant_warn".concat(xMerchant.getMerchantId().toString());
            if(!redisUtilService.hasKey(key)){
                Telegram telegram = new Telegram();
                telegram.sendMerchantBalanceMessage(xMerchant,zfRecharge, zfAgent.getConfig());
                redisUtilService.set(key, "1", 60);
            }
        }
    }

    @Override
    public void operatBalance(OperaBalanceParams operaBalanceParams) {
        log.info("商户余额操作开始 {}", operaBalanceParams);
        ZfMerchant zfMerchant = queryById(operaBalanceParams.getMerchantId());
        ZfMerchantTrans zfMerchantTrans = new ZfMerchantTrans(zfMerchant, operaBalanceParams);
        BigDecimal amount = operaBalanceParams.getTransType() == 0 ? BigDecimal.ZERO.subtract(operaBalanceParams.getAmount()):operaBalanceParams.getAmount();
        //商户余额上分
        log.info("实际上分 {}", amount);
        sumMerchantBalance(operaBalanceParams.getMerchantId(), amount);
        //资金日志写入
        log.info("上分记录 {}", zfMerchantTrans);
        zfMerchantTransService.insert(zfMerchantTrans);
    }

    @Override
    public void issue(TransferParams transParams) {
        redisUtilService.mlock(RedisConstant.ISSUE_LOCK,2000);
        ZfMerchant zfMerchant = zfMerchantDao.queryById(transParams.getMerchant_id());
        //支持银行
        verifMerchantBalance(zfMerchant, transParams);
        //校验商户余额
        ZfWithdraw xTransfer = zfWithdrawService.createIssueOrder(transParams, zfMerchant);
        //结束商户余额
        sumMerchantBalance(zfMerchant.getMerchantId(), BigDecimal.ZERO.subtract(transParams.getPay_amount()).subtract(xTransfer.getChannelFee()));
        zfMerchantTransService.insert(new ZfMerchantTrans(xTransfer, zfMerchant, 0));
        redisUtilService.leftPushAll("trans_size", transParams.getMerchant_order_no());
    }

    @Override
    public BigDecimal sumMerchantFee(ZfRecharge zfRecharge, ZfMerchant xMerchant) {
        String rate = xMerchant.getMerchantRate();
        try{
            if(Strings.isEmpty(rate)){
                return  BigDecimal.ZERO;
            }
            JSONObject jsonObject = JSONObject.parseObject(rate);
            if(jsonObject.containsKey("recharge")){
                JSONObject rechargeRate = jsonObject.getJSONObject("recharge");
                String rate_prex = PayTypeRateConstans.getMerchantRateString(zfRecharge.getPayType(),zfRecharge.getPaidAmount());
                log.info("结算支付方式费率 {}", rate_prex);
                String rate_type_key = "rate_type" + rate_prex;
                String rate_value_key = "rate_value" + rate_prex;
                if(!Strings.isEmpty(rechargeRate.getString(rate_type_key))&& !Strings.isEmpty(rechargeRate.getString(rate_type_key))){
                    Integer rate_type = rechargeRate.getInteger(rate_type_key);
                    Double rate_value = rechargeRate.getDoubleValue(rate_value_key);
                    if(rate_type == 0){
                        return BigDecimal.valueOf(rate_value);
                    }else{
                        BigDecimal rate1 = BigDecimal.valueOf(rate_value).divide(new BigDecimal("100"));
                        return zfRecharge.getPaidAmount().multiply(rate1);
                    }
                }
            }
            return BigDecimal.ZERO;
        }catch (Exception e){
            log.info("商户手续费异常", e);
            return BigDecimal.ZERO;
        }
    }

    @Override
    public ZfMerchant queryByName(String merchantName) {
       return zfMerchantDao.queryByName(merchantName);
    }

    @Override
    public ZfMerchant queryByGroup(Long chatid) {
        return zfMerchantDao.queryByGroup(chatid);

    }

    private JSONObject buildReuslt(ZfMerchant zfMerchant) {
        log.info("构建返回结果 商户数据 {} ");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("balance", zfMerchant.getBalance());
        jsonObject.put("merchant_id", zfMerchant.getMerchantId());
        String encode_str = "";
        for (String key : jsonObject.keySet()){
            encode_str += key +"="+jsonObject.get(key) + "&";
        }
        encode_str += "key=" + zfMerchant.getKey();
        log.info("签名字符串 {}", encode_str);
        String sign  = MD5Util.getMD5Str(encode_str).toUpperCase();
        jsonObject.put("sign", sign);
        return jsonObject;
    }

    public void  verifSign( MerchantParams merchantParams, ZfMerchant zfMerchant){
        log.info("开始校验签名", merchantParams);
        String encod_str = "merchant_id="+merchantParams.getMerchant_id()
                +"&key="+zfMerchant.getKey();
        String sign =  MD5Util.getMD5Str(encod_str).toUpperCase();
        if(!sign.equals(merchantParams.getSign())){
            log.info("订单号 {} 当前我方签名 {} 签名字符串  对方签名 {}", merchantParams.getMerchant_id(),sign, encod_str, merchantParams.getSign());
            throw new BaseException(ResultEnum.SIGN_ERROR) ;
        }
    }
}
