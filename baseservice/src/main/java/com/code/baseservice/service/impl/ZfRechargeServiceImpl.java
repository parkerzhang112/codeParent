package com.code.baseservice.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.code.baseservice.base.constant.BankTypeConstans;
import com.code.baseservice.base.constant.RedisConstant;
import com.code.baseservice.base.enums.ResultEnum;
import com.code.baseservice.base.exception.BaseException;
import com.code.baseservice.dao.ZfRechargeDao;
import com.code.baseservice.dto.backapi.OperaOrderParams;
import com.code.baseservice.dto.payapi.QueryParams;
import com.code.baseservice.dto.payapi.RechareParams;
import com.code.baseservice.entity.*;
import com.code.baseservice.service.*;
import com.code.baseservice.util.CommonUtil;
import com.code.baseservice.util.DateUtil;
import com.code.baseservice.util.HttpClientUtil;
import com.code.baseservice.util.MD5Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;


/**
 * (ZfRecharge)表服务实现类
 *
 * @author makejava
 * @since 2023-03-19 23:08:26
 */
@Service("zfRechargeService")
@Slf4j
public class ZfRechargeServiceImpl implements ZfRechargeService {
    @Resource
    private ZfRechargeDao zfRechargeDao;

    @Autowired
    private ZfMerchantService zfMerchantService;

    @Autowired
    private ZfChannelService zfChannelService;

    @Autowired
    private ZfCodeService zfCodeService;

    @Autowired
    private ZfCodeRecordService zfCodeRecordService;

    @Autowired
    private RedisUtilService redisUtilService;

    @Autowired
    private ZfAgentService zfAgentService;

    @Value("${app.viewurl:}")
    private String viewUrl;

    @Autowired
    private ZfMerchantTransService zfMerchantTransService;

    @Autowired
    private ZfMerchantRecordService zfMerchantRecordService;

    /**
     * 通过ID查询单条数据
     *
     * @param merchantOrderNo 主键
     * @return 实例对象
     */
    @Override
    public ZfRecharge queryById(String orderNo) {
        return this.zfRechargeDao.queryById(orderNo);
    }


    @Override
    public JSONObject create(RechareParams rechareParams) {
        //验证商户有效性
        ZfMerchant zfMerchant = zfMerchantService.vaildMerchant(rechareParams.getMerchant_id());
        //延签
        vaildSign(rechareParams, zfMerchant);
        //去重
        vaildRepeat(rechareParams);
        //查渠道
        ZfChannel zfChannel =  zfChannelService.queryChannelByParams(rechareParams);
        //查码
//        List<ZfCode> zfCodes = zfCodeService.queryCodeByParamAndChannel(zfChannels, rechareParams, zfMerchant);
        //轮码
//        ZfCode  zfCode = selectOneCardByRobin(zfCodes, zfMerchant, rechareParams);
        //入单
        ZfRecharge zfRecharge = createOrder(zfChannel, rechareParams, zfMerchant);
        //返回
        return buildReuslt(zfMerchant,zfRecharge);
    }

    private JSONObject buildReuslt( ZfMerchant zfMerchant,ZfRecharge zfRecharge) {
        TreeMap<String, Object>  map = new TreeMap<>();
        map.put("merchant_order_no", zfRecharge.getMerchantOrderNo());
        map.put("order_no", zfRecharge.getOrderNo());
        map.put("pay_amount", zfRecharge.getPayAmount());
        map.put("payurl", viewUrl+"/recharge/order/"+zfRecharge.getOrderNo());
        String sign_str = new CommonUtil().getSign(map);
        sign_str = sign_str.concat("key=".concat(zfMerchant.getKey()));
        String sign =  MD5Util.getMD5Str(sign_str).toUpperCase();
        log.info("订单号 {}  签名字符串 {} ", zfRecharge.getOrderNo(), sign_str);
        map.put("sign", sign);
        return new JSONObject(map);

    }

    private JSONObject buildViewReuslt( ZfMerchant zfMerchant,ZfRecharge zfRecharge) {
       Integer orderStatus =  zfRecharge.getOrderStatus() == 4 ? 2: zfRecharge.getOrderStatus();
        TreeMap<String, Object>  map = new TreeMap<>();
        map.put("merchant_order_no", zfRecharge.getMerchantOrderNo());
        map.put("pay_amount", zfRecharge.getPayAmount());
        map.put("order_status",orderStatus);
        String sign_str = new CommonUtil().getSign(map);
        sign_str = sign_str.concat("key=".concat(zfMerchant.getKey()));
        String sign =  MD5Util.getMD5Str(sign_str).toUpperCase();
        log.info("订单号 {}  签名字符串 {} ", zfRecharge.getOrderNo(), sign_str);
        map.put("sign", sign);
        return new JSONObject(map);

    }

    private ZfRecharge createOrder(ZfChannel zfChannel, RechareParams rechareParams, ZfMerchant zfMerchant) {
        try {
            String orderNo= CommonUtil.getOrderNo(zfMerchant.getMerchantCode(), "D");
            ZfRecharge xRecharge = new ZfRecharge(rechareParams);
            xRecharge.setOrderNo(orderNo);
            xRecharge.setChannelId(zfChannel.getChannelId());
//            xRecharge.setCodeId(zfCode.getCodeId());
//            xRecharge.setAgentId(zfCode.getAgentId());
            zfRechargeDao.insert(xRecharge);
            return  xRecharge;
        }catch (Exception e){
            log.info("创建订单异常 订单号 {}", rechareParams.getMerchant_order_no(), e);
            throw  new BaseException(ResultEnum.CREATE_ERROR);
        }
    }

    private ZfCode selectOneCardByRobin(List<ZfCode> zfCodes, ZfRecharge zfRecharge) {
        String key = zfRecharge.getMerchantId()+RedisConstant.CURRENT_CODE;
        Object currentCard  =  redisUtilService.get(key);
        log.info("当前轮训的码 {}", currentCard);
        if(Objects.isNull(currentCard)){
            String amountKey = "onlyAmount"+zfRecharge.getPayAmount().toBigInteger()+zfCodes.get(0).getName();

            log.info("轮训码信息为空");
            redisUtilService.set(key, zfCodes.get(0).getCodeId().intValue());
            return  zfCodes.get(0);
        }
        for (int i= 0;i < zfCodes.size(); i++){
            String amountKey = "onlyAmount"+zfRecharge.getPayAmount().toBigInteger()+zfCodes.get(i).getName();
            if(zfCodes.get(i).getCodeId().equals (currentCard) ){
                if(i+1 == zfCodes.size()){
                    log.info("码轮训最后一位 {}", zfCodes.get(i));
                    redisUtilService.set(key, zfCodes.get(0).getCodeId().intValue());
                    redisUtilService.set(amountKey, 1, 300);
                    return zfCodes.get(0);
                }else{
                    log.info("码轮训下一位 {}", zfCodes.get(i));
                    redisUtilService.set(key, zfCodes.get(i+1).getCodeId().intValue());
                    redisUtilService.set(amountKey, 1, 300);
                    return  zfCodes.get(i+1);
                }
            }
        }
        redisUtilService.set(key, zfCodes.get(0).getCodeId().intValue());
        log.info("单一码 {}", zfCodes);
        return  zfCodes.get(0);
    }

    private void vaildRepeat(RechareParams rechareParams) {
        log.info("订单号 去重开始", rechareParams.getMerchant_order_no());
        String key = rechareParams.getMerchant_id() + "_" +rechareParams.getMerchant_order_no();
        long num  = redisUtilService.incr(key,1);
        redisUtilService.expire(key, 24*3600);
        if(num >1){
            log.info("订单号 重复 {}", rechareParams.getMerchant_order_no());
            throw  new BaseException(ResultEnum.ORDER_REPEAT);
        }
    }

    private void vaildSign(RechareParams rechareParams, ZfMerchant zfMerchant) {
        TreeMap<String, Object> map = new TreeMap<>();
        map.put("merchant_id", rechareParams.getMerchant_id());
        map.put("merchant_order_no", rechareParams.getMerchant_order_no());
        map.put("pay_amount", rechareParams.getPay_amount());
        map.put("notify_url", rechareParams.getNotify_url());
        String sign_str = new CommonUtil().getSign(map);
        sign_str = sign_str.concat("key=".concat(zfMerchant.getKey()));
        String sign =  MD5Util.getMD5Str(sign_str).toUpperCase();
        log.info("开始验签 签名字符串 {}  加密值 {} 订单号 {}", sign_str, sign, rechareParams.getMerchant_order_no());
        if(!sign.equals(rechareParams.getSign())){
            log.info("当前我方签名 {} 签名字符串  对方签名 {}", sign, sign_str, rechareParams.getSign());
            throw new BaseException(ResultEnum.SIGN_ERROR);
        }
    }

    @Override
    public JSONObject query(QueryParams queryParams) {
        ZfMerchant zfMerchant =  zfMerchantService.vaildMerchant(queryParams.getMerchant_Id());
        zfMerchantService.verifSign(queryParams,zfMerchant);
        ZfRecharge zfRecharge = zfRechargeDao.queryByParam(queryParams);
        if(Objects.isNull(zfRecharge)){
            throw  new BaseException(ResultEnum.ORDER_NO_EXIST);
        }
        JSONObject jsonObject = buildViewReuslt(zfMerchant, zfRecharge);
        return jsonObject;
    }

    @Override
    public ZfRecharge tryFindOrderByTrans(ZfTransRecord zfTransRecord) {
        log.info("开始匹配流水 {}", zfTransRecord);
        List<ZfRecharge> zfRecharges =  zfRechargeDao.tryFindOrderByTrans(zfTransRecord);
        if(zfRecharges.size() == 1){
            ZfRecharge zfRecharge = zfRecharges.get(0);
            //修改订单状态
            zfRecharge.setPaidAmount(zfTransRecord.getAmount());
            zfRecharge.setOrderStatus(2);
            zfRecharge.setAgentId(zfTransRecord.getAgentId());
            zfRecharge.setPaidAmount(zfTransRecord.getAmount());
            zfTransRecord.setMerchantOrderNo(zfRecharge.getMerchantOrderNo());
            paidOrder(zfRecharge);
            return zfRecharge;
        }
        return null;
    }

    public void paidOrder(ZfRecharge zfRecharge) {
        //订单状态处理中
        ZfMerchant xMerchant = zfMerchantService.queryById(zfRecharge.getMerchantId());
        ZfChannel xChannel = zfChannelService.queryById(zfRecharge.getChannelId());
        ZfAgent zfAgent = zfAgentService.queryById(zfRecharge.getAgentId());
        //计算会员手续费
        BigDecimal fee = zfChannelService.sumChannelFee(zfRecharge.getPaidAmount(), xChannel);
        zfRecharge.setMerchantFee(fee);
        zfRechargeDao.update(zfRecharge);
        zfAgentService.updateAgentFee(zfRecharge, zfAgent, BigDecimal.ZERO);
        //更新码日报
        zfCodeRecordService.updateRecord(new ZfCodeRecord(zfRecharge));
        //计算商户渠道余额
        zfMerchantService.sumMerchantBalance(zfRecharge.getMerchantId(), zfRecharge.getPaidAmount().subtract(fee));
        //商户余额警告
        zfMerchantService.warrnBalance(xMerchant, zfRecharge,zfAgent);
        //记录商户流水
        zfMerchantTransService.insert(new ZfMerchantTrans(zfRecharge, xMerchant));
        //更新日报
        zfMerchantRecordService.updateRecord(new ZfMerchantRecord(zfRecharge));
        String amountKey = zfRecharge.getAgentId() + RedisConstant.UNQUINE + zfRecharge.getPayAmount().toString();
        redisUtilService.del(amountKey);
        notify(zfRecharge);
    }

    @Override
    public void confirmOrder(OperaOrderParams operaOrderParams) {
        try {
            ZfRecharge zfRecharge = zfRechargeDao.queryById(operaOrderParams.getOrderNo());
            if (zfRecharge.getOrderStatus() != 1) {
                log.info("订单已处理 订单号 {}", zfRecharge.getMerchantOrderNo());
                throw new BaseException(ResultEnum.ERROR);
            }
            BigDecimal paidAmount = operaOrderParams.getPaid_amt();
            zfRecharge.setPaidAmount(paidAmount);
            zfRecharge.setOrderStatus(4);
            zfRecharge.setRemark(operaOrderParams.getCloseReason());
            paidOrder(zfRecharge);
        }catch (Exception e){
            log.error("系统异常", e);
        }
    }

    @Override
    public void cancelOrder(OperaOrderParams operaOrderParams) {
        ZfRecharge zfRecharge = zfRechargeDao.queryById(operaOrderParams.getOrderNo());
        log.info("取消订单 订单号{}", zfRecharge.getMerchantOrderNo());
        if (zfRecharge.getOrderStatus() > 1) {
            log.info("订单已处理 订单号 {}", zfRecharge.getMerchantOrderNo());
            throw new BaseException(ResultEnum.ERROR);
        }
        zfRecharge.setRemark(operaOrderParams.getCloseReason());
        zfRecharge.setUpdateTime(new Date());
        zfRecharge.setOrderStatus(3);
        zfRechargeDao.update(zfRecharge);
        //删除唯一金额的rediskey
        String amountKey = zfRecharge.getCodeId() + RedisConstant.UNQUINE+ zfRecharge.getPayAmount().toString();
        redisUtilService.del(amountKey);
        zfAgentService.updateAgentCreditAmount(zfRecharge, zfRecharge.getAgentId());
        notify(zfRecharge);
    }

    @Override
    public void notify(ZfRecharge zfRecharge) {

        ZfMerchant xMerchant = zfMerchantService.queryById(zfRecharge.getMerchantId());
        JSONObject jsonObject = new JSONObject();
        ZfCode zfCode = zfCodeService.queryById(zfRecharge.getCodeId());
        redisUtilService.del(zfCode.getAccount().concat("_").concat(zfRecharge.getPayAmount().toString()));
        Integer OrderStatus = zfRecharge.getOrderStatus().equals(4) ? 2:zfRecharge.getOrderStatus() ;
        TreeMap<String, Object>  map = new TreeMap<>();
        map.put("merchant_id", zfRecharge.getMerchantId());
        map.put("merchant_order_no", zfRecharge.getMerchantOrderNo());
        map.put("order_status", OrderStatus);
        map.put("pay_amount", zfRecharge.getPayAmount());
        map.put("paid_amount", zfRecharge.getPaidAmount());
        String sign_str = new CommonUtil().getSign(map);
        sign_str = sign_str.concat("key=".concat(xMerchant.getKey()));
        String sign =  MD5Util.getMD5Str(sign_str).toUpperCase();
        log.info("订单号 {}  签名字符串 {} ", map.get("order_no"), sign_str);
        map.put("sign", sign);
        try {
            String reponse = HttpClientUtil.doPostJson(zfRecharge.getNotifyUrl(),JSONObject.toJSONString(map));
            log.info("订单号 {}   推送数据{}  结果 {}", zfRecharge.getOrderNo(), map, reponse);
            if("success".equals(reponse) || "回调成功".equals(reponse)){
                zfRechargeDao.toNotifySuccess(zfRecharge);
            }else {
                JSONObject jsonObject1 = JSONObject.parseObject(reponse);
                if (jsonObject1.getInteger("code").equals(200)) {
                    zfRechargeDao.toNotifySuccess(zfRecharge);
                }
            }
        }catch (Exception e){
            log.error("通知异常 订单号 {}", zfRecharge.getMerchantOrderNo(), e);
            zfRechargeDao.toNotifyException(zfRecharge);
        }
    }

    @Override
    public JSONObject getOrderStatus(String orderno) {

        //查单码
        ZfRecharge zfRecharge = queryById(orderno);
        TreeMap<String, Object>  map = new TreeMap<>();
        map.put("order_no", zfRecharge.getMerchantOrderNo());
        map.put("pay_amount", zfRecharge.getPayAmount());
        map.put("time",DateUtil.format1(new Date(zfRecharge.getCreateTime().getTime() + 300000), DateUtil.YYYY_MM_DD_HH_MM_SS1) );
        //检查订单状态 超时，
        if(new Date().getTime() - zfRecharge.getCreateTime().getTime()  > 300000){
            map.put("order_status", 3);
            return new JSONObject(map);
        }
        if(!zfRecharge.getOrderStatus().equals(0)){
            if(zfRecharge.getOrderStatus().equals(1)){
                ZfCode zfCode = zfCodeService.queryById(zfRecharge.getCodeId());
                map.put("payurl", zfCode.getImage());
            }
            map.put("order_status", zfRecharge.getOrderStatus());
            return new JSONObject(map);
        }

        //没有分配码，则尝试分配
        List<ZfCode> zfCodes = zfCodeService.queryCodeByParamAndChannel(zfRecharge);
        //没有找到二维码，则继续等待
        if(zfCodes.size() == 0){
            map.put("order_status", 0);
            return new JSONObject(map);
        }
        ZfCode  zfCode = selectOneCardByRobin(zfCodes, zfRecharge);
        zfRecharge.setAgentId(zfCode.getAgentId());
        zfRecharge.setCodeId(zfCode.getCodeId());
        zfRecharge.setCreateTime(new Date());
        zfRecharge.setOrderStatus(1);
        zfRechargeDao.update(zfRecharge);
        //增加已收额度
        zfAgentService.updateAgentCreditAmount(zfRecharge, zfCode.getAgentId());
        //更新订单信息
        map.put("order_status", 1);
        map.put("payurl", zfCode.getImage());
        return new JSONObject(map);
        //返回订单信息
    }
}
