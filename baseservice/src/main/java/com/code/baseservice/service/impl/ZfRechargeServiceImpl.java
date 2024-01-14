package com.code.baseservice.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.code.baseservice.base.constant.RedisConstant;
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
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;
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
    private ZfAgentTransService zfAgentTransService;

    @Override
    public List<ZfRecharge> queryByLimit(int page, int pagenum) {
        ZfRecharge zfRecharge =  new ZfRecharge();
        zfRecharge.setOrderStatus(1);
        List<ZfRecharge> zfRecharges = zfRechargeDao.queryAllByLimit(1, page,pagenum * page);
        return  zfRecharges;
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
        ZfChannel zfChannel =  zfChannelService.queryChannelByParams(rechareParams);
        //查码
//        List<ZfCode> zfCodes = zfCodeService.queryCodeByParamAndChannel(zfChannels, rechareParams, zfMerchant);
        //轮码
//        ZfCode  zfCode = selectOneCardByRobin(zfCodes, zfMerchant, rechareParams);
//        commonService.request(zfChannel, rechareParams);
        //入单
        ZfRecharge zfRecharge = createOrder(zfChannel, rechareParams, zfMerchant);

        //返回
        return buildReuslt(zfMerchant,zfRecharge);
    }

    @Override
    public JSONObject createCard(RechareParams rechareParams) {
        //验证商户有效性
        ZfMerchant zfMerchant = zfMerchantService.vaildMerchant(rechareParams.getMerchant_id());
        //延签
        vaildSign(rechareParams, zfMerchant);
        //去重
        vaildRepeat(rechareParams);
        //查渠道
        ZfChannel zfChannel =  zfChannelService.queryChannelByParams(rechareParams);

        //入单
        ZfRecharge zfRecharge = createOrder(zfChannel, rechareParams, zfMerchant);
        //没有分配码，则尝试分配
        List<ZfCode> zfCodes = zfCodeService.queryCodeByParamAndChannel(zfRecharge);
        //没有找到二维码，则继续等待
        if(zfCodes.size() == 0){
            throw  new BaseException(ResultEnum.ERROR);
        }
        ZfCode  zfCode = selectOneCardByRobin(zfCodes, zfRecharge);
        if(zfCode == null){
            throw  new BaseException(ResultEnum.ERROR);
        }
        zfRecharge.setAgentId(zfCode.getAgentId());
        zfRecharge.setCodeId(zfCode.getCodeId());
        zfRecharge.setCreateTime(new Date());
        zfRecharge.setOrderStatus(1);
        zfRechargeDao.update(zfRecharge);
        //增加已收额度
        redisUtilService.set("notice:agent:" + zfCode.getAgentId(), 1,1200);
        zfAgentService.updateAgentCreditAmount(zfRecharge, zfCode.getAgentId());
        //返回
        return buildReusltCard(zfMerchant,zfRecharge, zfCode);
    }

    private JSONObject buildReusltCard(ZfMerchant zfMerchant, ZfRecharge zfRecharge, ZfCode zfCode) {
        List<String> infos = Arrays.asList(zfCode.getAccount().split("\\|"));
        TreeMap<String, Object>  map = new TreeMap<>();
        map.put("bank_card_name", zfCode.getName());
        map.put("bank_card_num", infos.get(0));
        map.put("bank_card_type", infos.get(1));
        map.put("bank_address", infos.get(2));
        map.put("merchant_order_no", zfRecharge.getMerchantOrderNo());
        map.put("order_no", zfRecharge.getOrderNo());
        map.put("pay_amount", zfRecharge.getPayAmount());

        String sign_str = new CommonUtil().getSign(map);
        sign_str = sign_str.concat("key=".concat(zfMerchant.getKey()));
        String sign =  MD5Util.getMD5Str(sign_str).toUpperCase();
        log.info("订单号 {}  签名字符串 {} ", zfRecharge.getOrderNo(), sign_str);
        map.put("sign", sign);
        return new JSONObject(map);

    }

    @Override
    public JSONObject createA(RechareParams rechareParams) {
        //验证商户有效性
        ZfMerchant zfMerchant = zfMerchantService.vaildMerchant(rechareParams.getMerchant_id());
        //延签
        vaildSign(rechareParams, zfMerchant);
        //去重
        vaildRepeat(rechareParams);
        //查渠道
        ZfChannel zfChannel =  zfChannelService.queryChannelByParams(rechareParams);
        //入单
        ZfRecharge zfRecharge = createOrder(zfChannel, rechareParams, zfMerchant);
        return  commonService.request(zfChannel, rechareParams);
    }

    private JSONObject buildReuslt( ZfMerchant zfMerchant,ZfRecharge zfRecharge) {
        TreeMap<String, Object>  map = new TreeMap<>();
        map.put("merchant_order_no", zfRecharge.getMerchantOrderNo());
        map.put("order_no", zfRecharge.getOrderNo());
        map.put("pay_amount", zfRecharge.getPayAmount());
        String url = StringUtils.isNotEmpty(zfMerchant.getDomain()) ? zfMerchant.getDomain() : viewUrl;
        map.put("payurl", url+"/recharge/order/"+zfRecharge.getOrderNo());
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
        log.info("订单号 {}  签名字符串 {} 加密字符串 {}", zfRecharge.getOrderNo(), sign_str, sign);
        map.put("sign", sign);
        return new JSONObject(map);

    }

    private ZfRecharge createOrder(ZfChannel zfChannel, RechareParams rechareParams, ZfMerchant zfMerchant) {
        try {
            String orderNo= CommonUtil.getOrderNo(zfMerchant.getMerchantCode(), "D");
            ZfRecharge xRecharge = new ZfRecharge(rechareParams);
            xRecharge.setOrderNo(orderNo);
            xRecharge.setPayType(zfChannel.getPayType());
            xRecharge.setChannelId(zfChannel.getChannelId());
            if(zfChannel.getPayType().equals(4)){
                xRecharge.setRemark("恭喜"+StringUtil.createRandomStr1(3));
            }
            xRecharge.setPayName(rechareParams.getName());
//            xRecharge.setCodeId(zfCode.getCodeId());
//            xRecharge.setAgentId(zfCode.getAgentId());
            zfRechargeDao.insert(xRecharge);
            return  xRecharge;
        }catch (Exception e){
            log.info("创建订单异常 订单号 {}", rechareParams.getMerchant_order_no(), e);
            throw  new BaseException(ResultEnum.CREATE_ERROR);
        }
    }

    private Integer selectOneAgentByRobin(List<Integer> zfAgents,String key, Integer merchantId){
        if(zfAgents.contains(335)){
            return 335;
        }
        Object currentAgent  =  redisUtilService.get(key);
        if(currentAgent == null){
            redisUtilService.set(key, zfAgents.get(0).intValue());
            return zfAgents.get(0);
        }

        for (int i= 0;i < zfAgents.size(); i++) {
            if(zfAgents.get(i) < (Integer) currentAgent ){
                String weightAgent = merchantId+RedisConstant.WEIFHT_AGENT + zfAgents.get(i);
                ZfAgent zfAgent = zfAgentService.queryById(zfAgents.get(i));
                BigDecimal weight  = (BigDecimal) redisUtilService.get(weightAgent);
                if (null == weight){
                    weight = BigDecimal.ZERO;
                }
                if(weight.compareTo(BigDecimal.ONE) > -1){
                    redisUtilService.set(weightAgent, weight.subtract(BigDecimal.ONE));
                    redisUtilService.set(key, (Integer) currentAgent);
                    return zfAgents.get(i);
                }else {
                    if(weight.compareTo(zfAgent.getWeight())> -1){
                        weight = zfAgent.getWeight();
                    }
                    redisUtilService.set(weightAgent, weight.add(zfAgent.getWeight().subtract(BigDecimal.ONE)));
                    redisUtilService.set(key, zfAgents.get(i).intValue());
                    return zfAgents.get(i);
                }
            }
            log.info("代理轮训下一位 {}", zfAgents.get(i));
        }
        if(zfAgents.size() > 1){
            String weightAgent = merchantId+RedisConstant.WEIFHT_AGENT +  zfAgents.get(0).intValue();
            ZfAgent zfAgent = zfAgentService.queryById( zfAgents.get(0).intValue());
            BigDecimal weight  = (BigDecimal) redisUtilService.get(weightAgent);
            if (null == weight){
                weight = BigDecimal.ZERO;
            }
            if(weight.compareTo(BigDecimal.ONE) > -1) {
                redisUtilService.set(weightAgent, weight.subtract(BigDecimal.ONE));
                return zfAgents.get(0).intValue();
            }else {
                redisUtilService.set(weightAgent, weight.add(zfAgent.getWeight().subtract(BigDecimal.ONE)));
            }
        }
        log.info("代理轮询重置 取第一位 {}", zfAgents.get(0));
        redisUtilService.set(key, zfAgents.get(0).intValue());
        return zfAgents.get(0);
    }

    private ZfCode selectOneCardByRobin(List<ZfCode> zfCodes, ZfRecharge zfRecharge) {

        Telegram telegram = new Telegram();
        Set<Integer > agengids = zfCodes.stream().map(ZfCode::getAgentId).collect(Collectors.toSet());
        List<Integer> sortagentIds =  agengids.stream().sorted(((o1, o2) -> o2.compareTo(o1))).collect(Collectors.toList());
        String agentKey = RedisConstant.CURRENT_AGENT.concat(zfRecharge.getPayType().toString());
        Integer agentId = selectOneAgentByRobin(sortagentIds, agentKey, zfRecharge.getMerchantId());
        zfCodes =  zfCodes.stream().filter(o1-> o1.getAgentId().equals(agentId)).collect(Collectors.toList());
        List<String > codeDistinctList = zfCodes.stream().map(ZfCode::getName).collect(Collectors.toList());
//        telegram.sendWarrnSmsMessage(zfRecharge, "存款出码", String.join("-", codeDistinctList));
        String amountBettwen = getAmountBettwen(zfRecharge);
        String key =  agentId + "_" +amountBettwen+RedisConstant.CURRENT_CODE;
        Object currentCard  =  redisUtilService.get(key);
        log.info("当前码池 {}", codeDistinctList);

        log.info("当前轮训的码 {}", currentCard);
        if(Objects.isNull(currentCard)){
            String amountKey = zfRecharge.getPayType()+ "onlyAmount"+zfRecharge.getPayAmount().toBigInteger()+zfCodes.get(0).getCodeId();
            log.info("轮训码信息为空");
            redisUtilService.set(amountKey, 1, 600);
            redisUtilService.set(key, zfCodes.get(0).getCodeId().intValue());
            return  zfCodes.get(0);
        }
        for (int i= 0;i < zfCodes.size(); i++){
            String amountKey = zfRecharge.getPayType() +"onlyAmount"+zfRecharge.getPayAmount().toBigInteger()+zfCodes.get(i).getCodeId();
            if(zfCodes.get(i).getCodeId() < (Integer) currentCard && zfCodes.get(i).getAgentId().equals(agentId)){
                log.info("码轮训下一位 {}", zfCodes.get(i));
                redisUtilService.set(key, zfCodes.get(i).getCodeId().intValue());
                redisUtilService.set(amountKey, 1, 600);
                return  zfCodes.get(i);
            }
        }
        String amountKey = zfRecharge.getPayType()+ "onlyAmount"+zfRecharge.getPayAmount().toBigInteger()+zfCodes.get(0).getCodeId();
        redisUtilService.set(amountKey, 1, 600);
        redisUtilService.set(key, zfCodes.get(0).getCodeId().intValue());
        log.info("单一码 {}", zfCodes);
        return  zfCodes.get(0);
    }

    private String getAmountBettwen(ZfRecharge zfRecharge) {
        if(zfRecharge.getPayAmount().compareTo(new BigDecimal("5000")) > 0){
            return  "5001_20000";
        }else if(zfRecharge.getPayAmount().compareTo(new BigDecimal("3000")) > 0 &&
                zfRecharge.getPayAmount().compareTo(new BigDecimal("5000")) <= 0 ) {
            return "3000_5000";
        }else if(zfRecharge.getPayAmount().compareTo(new BigDecimal("800")) > 0 &&
                zfRecharge.getPayAmount().compareTo(new BigDecimal("3000")) <= 0){
            return "800_3000";
        }else {
            return "1_800";
        }
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
    public ZfRecharge tryFindOrderByTrans(ZfTransRecord zfTransRecord, ZfCode zfCode) {
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
        Telegram telegram = new Telegram();
        telegram.sendWarrnMessageByNoMatch(zfTransRecord, zfCode);
        return null;
    }

    @Transactional()
    public void paidOrder(ZfRecharge zfRecharge) {
        try {
            ZfMerchant xMerchant = zfMerchantService.queryById(zfRecharge.getMerchantId());
            if (xMerchant == null){
                log.info("订单完成时 查询商户不存在 {}", zfRecharge.getMerchantId());
                return;
            }
            ZfChannel xChannel = zfChannelService.queryById(zfRecharge.getChannelId());
            if (xChannel == null){
                log.info("订单完成时 查询渠道不存在 {}", zfRecharge.getMerchantId());
                return;
            }
            //计算会员手续费
            BigDecimal fee = zfChannelService.sumChannelFee(zfRecharge.getPaidAmount(), xChannel);
            zfRecharge.setMerchantFee(fee);
            zfRechargeDao.update(zfRecharge);
            zfAgentService.updateAgentFee(zfRecharge, zfRecharge.getAgentId(), BigDecimal.ZERO);
            //更新码日报
            zfCodeRecordService.updateRecord(new ZfCodeRecord(zfRecharge));
            //计算商户渠道余额
            zfMerchantService.sumMerchantBalance(zfRecharge.getMerchantId(), zfRecharge.getPaidAmount().subtract(fee));
            //记录商户流水
            zfMerchantTransService.insert(new ZfMerchantTrans(zfRecharge, xMerchant));
            //更新日报
            zfMerchantRecordService.updateRecord(new ZfMerchantRecord(zfRecharge));
        }catch (Exception e){
            log.error("订单异常 {} {}",zfRecharge.getMerchantOrderNo(), e.getStackTrace());
            throw new RuntimeException(e);
        }
        notify(zfRecharge);
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
        if(zfRecharge.getCodeId() != null && zfRecharge.getCodeId() != 0){
            String amountKey = "onlyAmount"+zfRecharge.getPayAmount().toBigInteger()+zfRecharge.getCodeId();
            redisUtilService.del(amountKey);
        }

//        notify(zfRecharge);
        zfAgentService.updateAgentCreditAmount(zfRecharge, zfRecharge.getAgentId());
    }

    /**
     * 区别于手动取消，不会通知商户，不更新订单状态，删除接单金额，重新接口，会补回代理积分
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
                if(zfRecharge.getAgentId() != null && zfRecharge.getAgentId() != 0){
                    String amountKey = "onlyAmount"+zfRecharge.getPayAmount().toBigInteger()+zfRecharge.getCodeId();
                    redisUtilService.del(amountKey);
                    zfAgentService.updateAgentCreditAmount(zfRecharge, zfRecharge.getAgentId());
                }else {
                    List<ZfAgentTrans> zfAgentTrans = zfAgentTransService.queryAddTransBySubNear(zfRecharge.getMerchantOrderNo());
                    if(zfAgentTrans.size() == 1){
                        log.info("订单无二维码。仍然有流水，进行回滚 {}", zfAgentTrans);
                        zfAgentService.updateAgentCreditAmount(zfRecharge, zfAgentTrans.get(0).getAgentId());
                    }
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
        log.info("开始查询订单 {}",orderno);
        RLock rLockOrder = redissonClient.getLock("recharge:order" + orderno);
        try {
                //查单码
                ZfRecharge zfRecharge = queryById(orderno);
                map.put("order_no", zfRecharge.getMerchantOrderNo());
                map.put("pay_amount", zfRecharge.getPayAmount());
                map.put("time",DateUtil.format1(new Date(zfRecharge.getCreateTime().getTime() + 300000), DateUtil.YYYY_MM_DD_HH_MM_SS1) );
                //检查订单状态 超时，
                if(new Date().getTime() - zfRecharge.getCreateTime().getTime()  > 300000){
                    map.put("order_status", 3);
                    return new JSONObject(map);
                }
            String isExist = "get:code:" + zfRecharge.getOrderNo();
            if(redisUtilService.hasKey(isExist)){
                ZfCode zfCode = zfCodeService.queryById(zfRecharge.getCodeId());
                map.put("payurl", zfCode.getImage());
                map.put("trans_account", zfCode.getAccount());
                map.put("trans_name", zfCode.getName());
                map.put("remark", zfRecharge.getRemark());
                map.put("order_status", 1);
                return new JSONObject(map);
            }
            if(rLockOrder.tryLock(2,5, TimeUnit.SECONDS)){

                if(!zfRecharge.getOrderStatus().equals(0)){
                    if(zfRecharge.getOrderStatus().equals(1)){
                        ZfCode zfCode = zfCodeService.queryById(zfRecharge.getCodeId());
                        map.put("payurl", zfCode.getImage());
                        map.put("trans_account", zfCode.getAccount());
                        map.put("trans_name", zfCode.getName());
                        map.put("remark", zfRecharge.getRemark());
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
                    if(zfCode == null){
                        map.put("order_status", 0);
                        return new JSONObject(map);
                    }
                    redisUtilService.set(isExist, 1,1200);
                    zfRecharge.setMerchantOrderNo(zfRecharge.getMerchantOrderNo());
                    zfRecharge.setAgentId(zfCode.getAgentId());
                    zfRecharge.setCodeId(zfCode.getCodeId());
                    zfRecharge.setUpdateTime(new Date());
                    zfRecharge.setOrderStatus(1);
                    int r = zfRechargeDao.updateProcess(zfRecharge);
                    if(r == 0){
                        log.error("订单更新失败 ", zfRecharge);
                    }
                    //增加已收额度
                    redisUtilService.set("notice:agent:" + zfCode.getAgentId(), 1,1200);
                    zfAgentService.updateAgentCreditAmount(zfRecharge, zfCode.getAgentId());
                    //更新订单信息
                    map.put("order_status", 1);
                    map.put("trans_account", zfCode.getAccount());
                    map.put("trans_name", zfCode.getName());
                    map.put("remark", zfRecharge.getRemark());
                    map.put("payurl", zfCode.getImage());
                    return new JSONObject(map);
                }else {
                    map.put("order_status", 0);
                    return new JSONObject(map);
                }
            //返回订单信息
        }catch (Exception e){
            log.error("查码异常 ", e);
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
        if (zfAgent == null){
            log.info("订单完成时 查询代理不存在 {}", zfRecharge.getMerchantId());
            return;
        }
        ZfChannel xChannel = zfChannelService.queryById(zfRecharge.getChannelId());
        if (xChannel == null){
            log.info("订单完成时 查询渠道不存在 {}", zfRecharge.getMerchantId());
            return;
        }
        //计算会员手续费
        BigDecimal fee = zfChannelService.sumChannelFee(zfRecharge.getPaidAmount(), xChannel);
        zfRecharge.setMerchantFee(fee);
        zfRechargeDao.update(zfRecharge);
        zfAgentService.updateAgentFee(zfRecharge, zfAgent.getAgentId(), BigDecimal.ZERO);
        //更新码日报
        zfCodeRecordService.updateRecord(new ZfCodeRecord(zfRecharge));
        String amountKey = "onlyAmount"+zfRecharge.getPayAmount().toBigInteger()+zfRecharge.getCodeId();
        redisUtilService.del(amountKey);
    }



}
