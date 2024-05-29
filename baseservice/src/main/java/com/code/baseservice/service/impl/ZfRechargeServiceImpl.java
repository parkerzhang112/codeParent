package com.code.baseservice.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.code.baseservice.base.constant.RedisConstant;
import com.code.baseservice.base.enums.PaytypeEnum;
import com.code.baseservice.base.enums.ResultEnum;
import com.code.baseservice.base.exception.BaseException;
import com.code.baseservice.dao.ZfRechargeDao;
import com.code.baseservice.dto.backapi.OperaBalanceParams;
import com.code.baseservice.dto.backapi.OperaOrderParams;
import com.code.baseservice.dto.payapi.QueryParams;
import com.code.baseservice.dto.payapi.RechareParams;
import com.code.baseservice.entity.*;
import com.code.baseservice.service.*;
import com.code.baseservice.util.*;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;


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
    private RedissonClient redissonClient;

    @Autowired
    private ZfAgentService zfAgentService;

    @Value("${app.viewurl:}")
    private String viewUrl;

    @Autowired
    private ZfMerchantTransService zfMerchantTransService;

    @Autowired
    private ZfMerchantRecordService zfMerchantRecordService;

    @Autowired
    private CommonService commonService;

    @Autowired
    private ZfChannelRecordService zfChannelRecordService;

    @Override
    public List<ZfRecharge> queryByLimit(int page, int pagenum) {
        ZfRecharge zfRecharge = new ZfRecharge();
        zfRecharge.setOrderStatus(1);
        List<ZfRecharge> zfRecharges = zfRechargeDao.queryAllByLimit(1, page, pagenum * page);
        return zfRecharges;
    }

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
    public ZfRecharge queryByMerchantOrderNo(String merchanOrderNo) {
        return this.zfRechargeDao.queryByMerchantOrderNo(merchanOrderNo);
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
        ZfChannel zfChannel = zfChannelService.queryChannelByParams(rechareParams);
        //入单
        ZfRecharge zfRecharge = createOrder(zfChannel, rechareParams, zfMerchant);
        try {
            JSONObject jsonObject;
            if(zfChannel.getPayType() != PaytypeEnum.微信原生.getValue() && zfChannel.getPayType() != PaytypeEnum.微信小程序.getValue()){
                 jsonObject = commonService.create(zfChannel, zfRecharge);
                 log.info("单号 {} 三方请求的结果 {}", zfRecharge.getMerchantOrderNo(),jsonObject.toJSONString());
                if(null != jsonObject.getString("payurl")){
                    zfRecharge.setPayUrl(jsonObject.getString("payurl"));
                }
                 zfRecharge.setOrderStatus(1);
                int r = zfRechargeDao.update(zfRecharge);
                log.info("单号 {} 订单更新结果  {}", zfRecharge.getMerchantOrderNo(),  r);
            }else {
                List<ZfCode> zfCodes = zfCodeService.queryCodeByParamAndChannel(zfRecharge);

                ZfCode  zfCode = selectOneCardByRobin(zfCodes, zfRecharge);
                zfRecharge.setCodeId(zfCode.getCodeId());
                jsonObject = buildReuslt(zfMerchant, zfRecharge);
            }
            zfRechargeDao.insert(zfRecharge);
            ZfChannelRecord zfChannelRecord = new ZfChannelRecord();
            zfChannelRecord.setRechargeTimesTotal(1);
            zfChannelRecord.setChannelId(zfRecharge.getChannelId());
            zfChannelRecord.setRecordDate(DateUtil.format(new Date(), DateUtil.YYYY_MM_DD));
            zfChannelRecord.setMerchantId(zfRecharge.getMerchantId());
            zfChannelRecord.setRechargeAmount(BigDecimal.ZERO);
            zfChannelRecord.setChannelFee(BigDecimal.ZERO);
            zfChannelRecord.setChannelName(zfChannel.getChannelName());
            zfChannelRecordService.update(zfChannelRecord);
            return jsonObject;
        } catch (Exception e) {
            log.error("订单：{} 下单异常：", rechareParams.getMerchant_order_no(), e);
            zfRecharge.setRemark(e.getMessage());
            zfRecharge.setOrderStatus(0);
            zfRechargeDao.update(zfRecharge);
        }
        throw new BaseException(ResultEnum.ERROR);
    }

    @Override
    public ZfRecharge queryByOrderNo(String order_no) {
        return zfRechargeDao.queryByOrderNo(order_no);
    }

    private JSONObject buildReuslt(ZfMerchant zfMerchant, ZfRecharge zfRecharge) {
        TreeMap<String, Object> map = new TreeMap<>();
        map.put("merchant_order_no", zfRecharge.getMerchantOrderNo());
        map.put("order_no", zfRecharge.getOrderNo());
        map.put("pay_amount", zfRecharge.getPayAmount());
        map.put("payurl", zfMerchant.getDomain() + "/recharge/order/" + zfRecharge.getOrderNo());
        String sign_str = new CommonUtil().getSign(map);
        sign_str = sign_str.concat("key=".concat(zfMerchant.getKey()));
        String sign = MD5Util.getMD5Str(sign_str).toUpperCase();
        log.info("订单号 {}  签名字符串 {} ", zfRecharge.getOrderNo(), sign_str);
        map.put("sign", sign);
        return new JSONObject(map);

    }

    private JSONObject buildViewReuslt(ZfMerchant zfMerchant, ZfRecharge zfRecharge) {
        Integer orderStatus = zfRecharge.getOrderStatus() == 4 ? 2 : zfRecharge.getOrderStatus();
        TreeMap<String, Object> map = new TreeMap<>();
        map.put("merchant_order_no", zfRecharge.getMerchantOrderNo());
        map.put("pay_amount", zfRecharge.getPayAmount());
        map.put("order_status", orderStatus);
        String sign_str = new CommonUtil().getSign(map);
        sign_str = sign_str.concat("key=".concat(zfMerchant.getKey()));
        String sign =  MD5Util.getMD5Str(sign_str).toUpperCase();
        log.info("订单号 {}  签名字符串 {} ", zfRecharge.getOrderNo(), sign_str);
        map.put("sign", sign);
        return new JSONObject(map);

    }

    private ZfRecharge createOrder(ZfChannel zfChannel, RechareParams rechareParams, ZfMerchant zfMerchant) {
        try {
            String orderNo = CommonUtil.getOrderNo(zfMerchant.getMerchantCode(), "D");
            ZfRecharge xRecharge = new ZfRecharge(rechareParams);
            xRecharge.setOrderNo(orderNo);
            xRecharge.setPayType(zfChannel.getPayType());
            xRecharge.setChannelId(zfChannel.getChannelId());
            xRecharge.setIsThird(StringUtils.isNotEmpty(zfChannel.getThirdMerchantId()) ? 1 : 0);
//            xRecharge.setCodeId(zfCode.getCodeId());
//            xRecharge.setAgentId(zfCode.getAgentId());
            return xRecharge;
        } catch (Exception e) {
            log.info("创建订单异常 订单号 {}", rechareParams.getMerchant_order_no(), e);
            throw new BaseException(ResultEnum.CREATE_ERROR);
        }
    }

    private Integer selectOneAgentByRobin(List<Integer> zfAgents, String key, Integer merchantId) {
        Object currentAgent = redisUtilService.get(key);
        if (currentAgent == null) {
            redisUtilService.set(key, zfAgents.get(0).intValue());
            return zfAgents.get(0);
        }

        for (int i = 0; i < zfAgents.size(); i++) {
            if (zfAgents.get(i) < (Integer) currentAgent) {
                String weightAgent = merchantId + RedisConstant.WEIFHT_AGENT + zfAgents.get(i);
                ZfAgent zfAgent = zfAgentService.queryById(zfAgents.get(i));
                BigDecimal weight = (BigDecimal) redisUtilService.get(weightAgent);
                if (null == weight) {
                    weight = BigDecimal.ZERO;
                }
                if (weight.compareTo(BigDecimal.ONE) > -1) {
                    redisUtilService.set(weightAgent, weight.subtract(BigDecimal.ONE));
                    redisUtilService.set(key, (Integer) currentAgent);
                    return zfAgents.get(i);
                } else {
                    redisUtilService.set(weightAgent, weight.add(zfAgent.getWeight().subtract(BigDecimal.ONE)));
                    redisUtilService.set(key, zfAgents.get(i).intValue());
                    return zfAgents.get(i);
                }
            }
            log.info("代理轮训下一位 {}", zfAgents.get(i));
        }
        if (zfAgents.size() > 1) {
            String weightAgent = merchantId + RedisConstant.WEIFHT_AGENT + zfAgents.get(0).intValue();
            ZfAgent zfAgent = zfAgentService.queryById(zfAgents.get(0).intValue());
            BigDecimal weight = (BigDecimal) redisUtilService.get(weightAgent);
            if (null == weight) {
                weight = BigDecimal.ZERO;
            }
            if (weight.compareTo(BigDecimal.ONE) > -1) {
                redisUtilService.set(weightAgent, weight.subtract(BigDecimal.ONE));
                return zfAgents.get(0).intValue();
            } else {
                redisUtilService.set(weightAgent, weight.add(zfAgent.getWeight().subtract(BigDecimal.ONE)));
            }
        }
        log.info("代理轮询重置 取第一位 {}", zfAgents.get(0));
        redisUtilService.set(key, zfAgents.get(0).intValue());
        return zfAgents.get(0);
    }

    private ZfCode selectOneCardByRobin(List<ZfCode> zfCodes, ZfRecharge zfRecharge) {

        Set<Integer> agengids = zfCodes.stream().map(ZfCode::getAgentId).collect(Collectors.toSet());
        List<Integer> sortagentIds = agengids.stream().sorted(((o1, o2) -> o2.compareTo(o1))).collect(Collectors.toList());
        String agentKey = RedisConstant.CURRENT_AGENT;
        Integer agentId = selectOneAgentByRobin(sortagentIds, agentKey, zfRecharge.getMerchantId());
        zfCodes = zfCodes.stream().filter(o1 -> o1.getAgentId().equals(agentId)).collect(Collectors.toList());
        List<String> codeDistinctList = zfCodes.stream().map(ZfCode::getName).collect(Collectors.toList());
        String amountBettwen = getAmountBettwen(zfRecharge);
        String key = agentId + "_" + amountBettwen + RedisConstant.CURRENT_CODE;
        Object currentCard = redisUtilService.get(key);
        log.info("当前码池 {}", codeDistinctList);

        log.info("当前轮训的码 {}", currentCard);
        if (Objects.isNull(currentCard)) {
            log.info("轮训码信息为空");
            redisUtilService.set(key, zfCodes.get(0).getCodeId().intValue());
            return zfCodes.get(0);
        }
        for (int i = 0; i < zfCodes.size(); i++) {
            String amountKey = "onlyAmount" + zfRecharge.getPayAmount().toBigInteger() + zfCodes.get(i).getCodeId();
            if (zfCodes.get(i).getCodeId() < (Integer) currentCard && zfCodes.get(i).getAgentId().equals(agentId)) {
                log.info("码轮训下一位 {}", zfCodes.get(i));
                redisUtilService.set(key, zfCodes.get(i).getCodeId().intValue());
                return zfCodes.get(i);
            }
        }
        redisUtilService.set(key, zfCodes.get(0).getCodeId().intValue());
        log.info("单一码 {}", zfCodes);
        return zfCodes.get(0);
    }

    private String getAmountBettwen(ZfRecharge zfRecharge) {
        if (zfRecharge.getPayAmount().compareTo(new BigDecimal("5000")) > 0) {
            return "5001_20000";
        } else {
            return "800_5000";
        }
    }

    private void vaildRepeat(RechareParams rechareParams) {
        log.info("订单号 去重开始", rechareParams.getMerchant_order_no());
        String key = rechareParams.getMerchant_id() + "_" + rechareParams.getMerchant_order_no();
        long num = redisUtilService.incr(key, 1);
        redisUtilService.expire(key, 24 * 3600);
        if (num > 1) {
            log.info("订单号 重复 {}", rechareParams.getMerchant_order_no());
            throw new BaseException(ResultEnum.ORDER_REPEAT);
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
        String sign = MD5Util.getMD5Str(sign_str).toUpperCase();
        log.info("开始验签 签名字符串 {}  加密值 {} 订单号 {}", sign_str, sign, rechareParams.getMerchant_order_no());
        if (!sign.equals(rechareParams.getSign())) {
            log.info("当前我方签名 {} 签名字符串  对方签名 {}", sign, sign_str, rechareParams.getSign());
            throw new BaseException(ResultEnum.SIGN_ERROR);
        }
    }

    @Override
    public JSONObject query(QueryParams queryParams) {
        ZfMerchant zfMerchant = zfMerchantService.vaildMerchant(queryParams.getMerchant_Id());
        zfMerchantService.verifSign(queryParams, zfMerchant);
        ZfRecharge zfRecharge = zfRechargeDao.queryByParam(queryParams);
        if (Objects.isNull(zfRecharge)) {
            throw new BaseException(ResultEnum.ORDER_NO_EXIST);
        }
        JSONObject jsonObject = buildViewReuslt(zfMerchant, zfRecharge);
        return jsonObject;
    }

    @Override
    public ZfRecharge tryFindOrderByTrans(ZfTransRecord zfTransRecord, ZfCode zfCode) {
        log.info("开始匹配流水 {}", zfTransRecord);
        List<ZfRecharge> zfRecharges = zfRechargeDao.tryFindOrderByTrans(zfTransRecord);
        if (zfRecharges.size() == 1) {
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
        Telegram telegram = new Telegram();
        telegram.sendWarrnMessageByNoMatch(zfTransRecord, zfCode);
        return null;
    }

    public void paidOrder(ZfRecharge zfRecharge) {
        try {
            ZfRecharge zfRecharge1 = zfRechargeDao.queryById(zfRecharge.getOrderNo());
            log.info("确认订单 订单号 {}", zfRecharge.getMerchantOrderNo());
            if (zfRecharge1.getOrderStatus() != 1 && zfRecharge1.getOrderStatus() != 5) {
                log.info("订单已处理 订单号 {}", zfRecharge1.getMerchantOrderNo());
                throw new BaseException(ResultEnum.ERROR);
            }
            ZfMerchant xMerchant = zfMerchantService.queryById(zfRecharge.getMerchantId());
            if (xMerchant == null) {
                log.info("订单完成时 查询商户不存在 {}", zfRecharge.getMerchantId());
                return;
            }
            ZfChannel xChannel = zfChannelService.queryById(zfRecharge.getChannelId());
            if (xChannel == null) {
                log.info("订单完成时 查询渠道不存在 {}", zfRecharge.getMerchantId());
                return;
            }
            //计算会员手续费
            BigDecimal fee = zfMerchantService.sumMerchantFee(zfRecharge.getPaidAmount(),xMerchant);
            zfRecharge.setMerchantFee(fee);
            zfRechargeDao.update(zfRecharge);
            if (zfRecharge.getIsThird() == 0) {
                zfAgentService.updateAgentFee(zfRecharge, zfRecharge.getAgentId(), BigDecimal.ZERO);
                //更新码日报
                zfCodeRecordService.updateRecord(new ZfCodeRecord(zfRecharge));
            } else {
                zfChannelService.updateChannelFee(zfRecharge);
            }
            //计算商户渠道余额
            zfMerchantService.sumMerchantBalance(zfRecharge.getMerchantId(), zfRecharge.getPaidAmount().subtract(fee));
            //记录商户流水
            zfMerchantTransService.insert(new ZfMerchantTrans(zfRecharge, xMerchant));
            //更新日报
            zfMerchantRecordService.updateRecord(new ZfMerchantRecord(zfRecharge));
        } catch (Exception e) {
            log.error("订单异常 {} {}", zfRecharge.getMerchantOrderNo(), e);
            throw new RuntimeException(e);
        }
        String notifyUrl = getNotifyUrl(zfRecharge.getMerchantId());
        if(StringUtils.isNotEmpty(notifyUrl)){
            log.info("开始转代通知");
            JSONObject map = new JSONObject();
            map.put("order_no", zfRecharge.getOrderNo());
            map.put("order_type", "1");
            HttpClientUtil.doPostJson(notifyUrl+ "order/notify", map.toJSONString());
        }else {
            log.info("开始正常通知");

            notify(zfRecharge);
        }
    }

    private String  getNotifyUrl(Integer merchantId){
        if(merchantId == 895466){
            return "http://dahuilang.top/";
        }
        return "";
    }


    @Override
    public void confirmOrder(OperaOrderParams operaOrderParams) {
        try {
//            Thread.sleep(10000);
            ZfRecharge zfRecharge = zfRechargeDao.queryById(operaOrderParams.getOrderNo());
            log.info("确认订单 订单号 {}", zfRecharge.getMerchantOrderNo());
            if (zfRecharge.getOrderStatus() != 1 && zfRecharge.getOrderStatus() != 5) {
                log.info("订单已处理 订单号 {}", zfRecharge.getMerchantOrderNo());
                throw new BaseException(ResultEnum.ERROR);
            }
            BigDecimal paidAmount = operaOrderParams.getPaid_amt();
            zfRecharge.setPaidAmount(paidAmount);
            zfRecharge.setOrderStatus(4);
            zfRecharge.setRemark(operaOrderParams.getCloseReason());
            paidOrder(zfRecharge);
        } catch (Exception e) {
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
        if (zfRecharge.getCodeId() != null && zfRecharge.getCodeId() != 0) {
            String amountKey = "onlyAmount" + zfRecharge.getPayAmount().toBigInteger() + zfRecharge.getCodeId();
            redisUtilService.del(amountKey);
        }
        if (zfRecharge.getIsThird() == 0) {
            zfAgentService.updateAgentCreditAmount(zfRecharge, zfRecharge.getAgentId());
        }
//        notify(zfRecharge);
    }

    /**
     * 区别于手动取消，不会通知商户，不更新订单状态，删除接单金额，重新接口，会补回代理积分
     *
     * @param operaOrderParams
     */
    @Override
    public void autocancel(OperaOrderParams operaOrderParams) {
//        RLock rLock = redisUtilService.lock(operaOrderParams.getOrderNo(),5);
        try {
//            if(rLock.tryLock(5,10, TimeUnit.SECONDS)){
                ZfRecharge zfRecharge = zfRechargeDao.queryById(operaOrderParams.getOrderNo());
                log.info("自动取消订单 订单号{}", zfRecharge.getMerchantOrderNo());
                if (zfRecharge.getOrderStatus() > 1) {
                    log.info("订单已处理 订单号 {}", zfRecharge.getMerchantOrderNo());
                    throw new BaseException(ResultEnum.ERROR);
                }
                zfRecharge.setRemark(operaOrderParams.getCloseReason());
                zfRecharge.setUpdateTime(new Date());
                zfRecharge.setOrderStatus(5);
                if(zfRecharge.getCodeId() != null && zfRecharge.getCodeId() != 0){
                    String amountKey = "onlyAmount"+zfRecharge.getPayAmount().toBigInteger()+zfRecharge.getCodeId();
                    redisUtilService.del(amountKey);
                    zfAgentService.updateAgentCreditAmount(zfRecharge, zfRecharge.getAgentId());
                }
                zfRechargeDao.update(zfRecharge);
//            } else{
//                log.error("订单执行超时异常 {} ", operaOrderParams.getMerchanOrderNo());
//            }
        }catch (Exception e){
            log.error("订单执行超时异常 {} {}", operaOrderParams.getMerchanOrderNo(), e.getStackTrace());
            throw  new RuntimeException(e);
        }finally {
//            if(rLock.isLocked() && rLock.isHeldByCurrentThread()){
//                rLock.unlock();
//            }
        }
    }

    @Override
    public void postName(Map<String, Object> map) {
        ZfRecharge zfRecharge = zfRechargeDao.queryById(map.get("orderNo").toString());
        if(zfRecharge == null){
            return;
        }

        zfRecharge.setPayName(map.get("name").toString());
        zfRechargeDao.update(zfRecharge);
    }



    @Override
    public void notify(ZfRecharge zfRecharge) {
        if(!zfRecharge.getNotifyUrl().contains("127.0.0.1")){
            ZfMerchant xMerchant = zfMerchantService.queryById(zfRecharge.getMerchantId());
            Integer OrderStatus = zfRecharge.getOrderStatus().equals(4) ? 2:zfRecharge.getOrderStatus() ;
            TreeMap<String, Object>  map = new TreeMap<>();
            map.put("merchant_id", zfRecharge.getMerchantId());
            map.put("merchant_order_no", zfRecharge.getMerchantOrderNo());
            map.put("order_status", OrderStatus);
            map.put("pay_amount", zfRecharge.getPayAmount().toString());
            map.put("paid_amount", zfRecharge.getPaidAmount().toString());
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

    }

    @Override
    public JSONObject getOrderStatus(String orderno) {
        TreeMap<String, Object>  map = new TreeMap<>();
        RLock rLockOrder = redissonClient.getLock("recharge:order" + orderno);
        log.info("开始获取二维码 {}", orderno);
        try {
                //查单码
                ZfRecharge zfRecharge = queryById(orderno);
                map.put("order_no", zfRecharge.getMerchantOrderNo());
                map.put("pay_amount", zfRecharge.getPayAmount());
                map.put("time",DateUtil.format1(new Date(zfRecharge.getCreateTime().getTime() + 300000), DateUtil.YYYY_MM_DD_HH_MM_SS1) );
                //检查订单状态 超时，
//                if(new Date().getTime() - zfRecharge.getCreateTime().getTime()  > 600000){
//                    map.put("order_status", 3);
//                    return new JSONObject(map);
//                }
                if(!zfRecharge.getOrderStatus().equals(0)){
                    if(zfRecharge.getOrderStatus().equals(1)){
                        map.put("payurl", zfRecharge.getPayUrl());
                    }
                    map.put("order_status", zfRecharge.getOrderStatus());
                    return new JSONObject(map);
                }
                ZfChannel zfChannel  = zfChannelService.queryById(zfRecharge.getChannelId());
                JSONObject jsonObject =  commonService.create(zfChannel, zfRecharge);
                if(jsonObject == null){
                    map.put("order_status", 0);
                    return new JSONObject(map);
                }
                ZfCode zfCode = zfCodeService.queryById(zfRecharge.getCodeId());
                if(zfCode.getLimitSends() != 0){
                    redisUtilService.set(RedisConstant.LIMIT + zfRecharge.getCodeId(), 1, zfCode.getLimitSends());
                }
                zfRecharge.setPayUrl(jsonObject.getString("payurl"));
                zfRecharge.setCreateTime(new Date());
                zfRecharge.setOrderStatus(1);
                zfRechargeDao.update(zfRecharge);
                //增加已收额度
                //更新订单信息
                map.put("order_status", 1);
                map.put("payurl", zfRecharge.getPayUrl());
                return new JSONObject(map);

            //返回订单信息
        }catch (BaseException e){
            throw  e;
        }catch (Exception e) {
            log.error("创建订单异常 ", e);
            map.put("order_status", 3);
            return new JSONObject(map);
        }finally {
            if(rLockOrder.isLocked() && rLockOrder.isHeldByCurrentThread()){
                rLockOrder.unlock();
            }
        }
    }

    @Override
    public void operatOrder(OperaBalanceParams operaBalanceParams) {
        ZfRecharge zfRecharge = queryByMerchantOrderNo(operaBalanceParams.getMerchantOrderNo());
        //订单状态处理中
        ZfAgent zfAgent = zfAgentService.queryById(zfRecharge.getAgentId());
        if (zfAgent == null) {
            log.info("订单完成时 查询代理不存在 {}", zfRecharge.getMerchantId());
            return;
        }
        ZfChannel xChannel = zfChannelService.queryById(zfRecharge.getChannelId());
        if (xChannel == null) {
            log.info("订单完成时 查询渠道不存在 {}", zfRecharge.getMerchantId());
            return;
        }
        ZfMerchant zfMerchant = zfMerchantService.queryById(zfRecharge.getMerchantId());
        BigDecimal fee = zfMerchantService.sumMerchantFee(zfRecharge.getPaidAmount(),zfMerchant);
        //计算会员手续费
        zfRecharge.setMerchantFee(fee);
        zfRechargeDao.update(zfRecharge);
        zfAgentService.updateAgentFee(zfRecharge, zfAgent.getAgentId(), BigDecimal.ZERO);
        //更新码日报
        zfCodeRecordService.updateRecord(new ZfCodeRecord(zfRecharge));
        String amountKey = "onlyAmount" + zfRecharge.getPayAmount().toBigInteger() + zfRecharge.getCodeId();
        redisUtilService.del(amountKey);
    }



}
