package com.code.baseservice.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.code.baseservice.base.constant.RedisConstant;
import com.code.baseservice.base.enums.ResultEnum;
import com.code.baseservice.base.enums.TransTypeEnum;
import com.code.baseservice.base.exception.BaseException;
import com.code.baseservice.dao.ZfChannelDao;
import com.code.baseservice.dto.XChannelRate;
import com.code.baseservice.dto.backapi.OperaChannelParams;
import com.code.baseservice.dto.payapi.RechareParams;
import com.code.baseservice.dto.payapi.TransferParams;
import com.code.baseservice.entity.*;
import com.code.baseservice.service.RedisUtilService;
import com.code.baseservice.service.ZfChannelRecordService;
import com.code.baseservice.service.ZfChannelService;
import com.code.baseservice.service.ZfChannelTransService;
import com.code.baseservice.util.CommonUtil;
import com.code.baseservice.util.HttpClientUtil;
import com.code.baseservice.util.MD5Util;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.TreeMap;

/**
 * (ZfChannel)表服务实现类
 *
 * @author makejava
 * @since 2023-03-19 23:06:53
 */
@Service("zfChannelService")
@Slf4j
public class ZfChannelServiceImpl implements ZfChannelService {
    @Resource
    private ZfChannelDao zfChannelDao;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private ZfChannelRecordService zfChannelRecordService;

    @Autowired
    private ZfChannelTransService zfChannelTransService;

    @Autowired
    private RedisUtilService redisUtilService;

    /**
     * 通过ID查询单条数据
     *
     * @param channelId 主键
     * @return 实例对象
     */
    @Override
    public ZfChannel queryById(Integer channelId) {
        return this.zfChannelDao.queryById(channelId);
    }

    @Override
    public ZfChannel queryChannelByParams(RechareParams rechareParams) {
        log.info("开始查询渠道 订单号 {}", rechareParams.getMerchant_order_no());
        List<ZfChannel> channels = zfChannelDao.selectChannel(rechareParams);
        if (channels.size() == 0) {
            log.info("无可用渠道 订单号 {}", rechareParams.getMerchant_order_no());
            throw new BaseException(ResultEnum.NO_CHANNEL);
        }
        log.info("渠道查询结果 订单号 {},渠道集合:{}", rechareParams.getMerchant_order_no(), channels);
       return selectOneChannelByRobin(channels, rechareParams);
    }

    private ZfChannel selectOneChannelByRobin(List<ZfChannel> zfChannels,RechareParams rechareParams) {
        String key = RedisConstant.ROBIN_CARD_KEY;
        Integer currentChannel = (Integer) redisUtilService.get(key);
        log.info("当前轮训的渠道 {}", currentChannel);
        if(Objects.isNull(currentChannel)){
            log.info("轮训码信息为空");
            redisUtilService.set(key, zfChannels.get(0).getChannelId());
            return  zfChannels.get(0);
        }
        for (int i= 0;i < zfChannels.size(); i++){
            if(zfChannels.get(i).getChannelId() > currentChannel){
                log.info("渠道轮训下一位 {}", zfChannels.get(i));
                redisUtilService.set(key, zfChannels.get(i).getChannelId());
                return  zfChannels.get(i);
            }
        }
        redisUtilService.set(key, zfChannels.get(0).getChannelId());
        log.info("单一渠道 {}", zfChannels);
        return  zfChannels.get(0);
    }

    @Override
    public void sumMerchantBalance(Integer merchantId, BigDecimal subtract) {

    }

    @Override
    public List<ZfChannel> selectChannel(TransferParams transParams) {
        log.info("开始查询渠道 订单号 {}", transParams.getMerchant_order_no());
        List<ZfChannel> channels = zfChannelDao.selectChannelByTrans(transParams);
        if (channels.size() == 0) {
            log.info("无可用渠道 订单号 {}", transParams.getMerchant_order_no());
            throw new BaseException(ResultEnum.NO_CHANNEL);
        }
        return channels;
    }

    @Override
    public BigDecimal sumThirdChannelFee(@NonNull BigDecimal PaidAmount, ZfChannel zfChannel) {
        try {
            if (null == zfChannel.getThirdMerchantChannelRate()) {
                return BigDecimal.ZERO;
            }
            BigDecimal rate = zfChannel.getThirdMerchantChannelRate().divide(new BigDecimal("100"));
            return PaidAmount.multiply(rate);
        } catch (Exception e) {
            log.error("手续费计算异常", e);
        }
        return BigDecimal.ZERO;
    }


    @Override
    public BigDecimal sumChannelFee(@NonNull BigDecimal PaidAmount, ZfChannel zfChannel) {
        try {
            if (Strings.isEmpty(zfChannel.getChannelRate())) {
                return BigDecimal.ZERO;
            }
            XChannelRate xChannelRate = JSONObject.parseObject(zfChannel.getChannelRate(), XChannelRate.class);
            if (xChannelRate.getRate_type() == 0) {
                return BigDecimal.valueOf(xChannelRate.getRate_value());
            } else {
                BigDecimal rate = BigDecimal.valueOf(xChannelRate.getRate_value()).divide(new BigDecimal("100"));
                return PaidAmount.multiply(rate);
            }

        } catch (Exception e) {
            log.error("手续费计算异常", e);
        }
        return BigDecimal.ZERO;
    }

    @Override
    public void operatBalance(OperaChannelParams operaChannelParams) {
        ZfChannelTrans zfChannelTrans = new ZfChannelTrans();
        zfChannelTrans.setChannelId(operaChannelParams.getChannelId());
        ZfChannel zfChannel1 = zfChannelDao.queryById(operaChannelParams.getChannelId());
        if(null == zfChannel1.getChannelBalance()){
            throw new BaseException(ResultEnum.CHANNEL_BALANCE_NO_ENOUGH);
        }
        zfChannelTrans.setPreBalance(zfChannel1.getChannelBalance());
        zfChannelTrans.setBalance(zfChannel1.getChannelBalance().subtract(operaChannelParams.getAmount()));
        zfChannelTrans.setTransType(TransTypeEnum.TRANSFER.getValue());
        zfChannelTrans.setAmount(operaChannelParams.getAmount());
        zfChannelTrans.setRemark(operaChannelParams.getRemark());
        zfChannelTransService.insert(zfChannelTrans);
        zfChannelDao.updateChannelFee(BigDecimal.ZERO.subtract(operaChannelParams.getAmount()), operaChannelParams.getChannelId());
    }

    @Override
    public void updateChannelFee(ZfWithdraw zfWithdraw) {
        RLock rLock = redissonClient.getLock("channel"+zfWithdraw.getChannelId());
        try{
            ZfChannel zfChannel1 = new ZfChannel();
            //查询渠道信息
            ZfChannel zfChannel = zfChannelDao.queryById(zfWithdraw.getChannelId());
            BigDecimal channlFee = sumThirdChannelFee(zfWithdraw.getPayAmount(), zfChannel);
            channlFee =  channlFee.add(new BigDecimal("2"));
            //组装更新信息
            ZfChannelTrans zfChannelTrans = new ZfChannelTrans(zfWithdraw,zfChannel, channlFee);
            ZfChannelRecord zfChannelRecord = new ZfChannelRecord(zfWithdraw, zfChannel, channlFee);
            zfChannel1.setChannelBalance(BigDecimal.ZERO.subtract(zfWithdraw.getPaidAmount().add(channlFee)));
            zfChannelDao.updateChannelFee(zfChannel1.getChannelBalance(), zfWithdraw.getChannelId());
            //插入渠道流水
            zfChannelRecordService.update(zfChannelRecord);
            //更新渠道余额
            zfChannelTransService.insert(zfChannelTrans);
        }catch (Exception e){
            log.error("计算渠道费用 单号{} 异常原因 {}", zfWithdraw.getMerchantOrderNo(), e.getStackTrace());
            throw  new RuntimeException(e);
        }finally {
            if(rLock.isLocked() && rLock.isHeldByCurrentThread()){
                rLock.isLocked();
            }
        }
    }

    @Override
    public void updateChannelFee(ZfRecharge zfRecharge) {
        RLock rLock = redissonClient.getLock("channel"+zfRecharge.getChannelId());
        try{
            ZfChannel zfChannel1 = new ZfChannel();
            //查询渠道信息
            ZfChannel zfChannel = zfChannelDao.queryById(zfRecharge.getChannelId());
            BigDecimal channlFee = sumThirdChannelFee(zfRecharge.getPayAmount(), zfChannel);
            //组装更新信息
            ZfChannelTrans zfChannelTrans = new ZfChannelTrans(zfRecharge,zfChannel, channlFee);
            ZfChannelRecord zfChannelRecord = new ZfChannelRecord(zfRecharge, zfChannel, channlFee);
            zfChannel1.setChannelBalance(zfRecharge.getPaidAmount().subtract(channlFee));
            zfChannelDao.updateChannelFee(zfChannel1.getChannelBalance(), zfRecharge.getChannelId());
            //插入渠道流水
            zfChannelRecordService.update(zfChannelRecord);
            //更新渠道余额
            zfChannelTransService.insert(zfChannelTrans);
        }catch (Exception e){
            log.error("计算渠道费用 单号{} 异常原因 {}", zfRecharge.getMerchantOrderNo(), e.getStackTrace());
            throw  new RuntimeException(e);
        }finally {
            if(rLock.isLocked() && rLock.isHeldByCurrentThread()){
                rLock.isLocked();
            }
        }
    }

    @Override
    public ZfChannel queryByMerchantId(String thirdMerchantId) {
        return zfChannelDao.queryByMerchantId(thirdMerchantId);
    }


    public static void main(String[] args) {
        RechareParams rechareParams = new RechareParams();
        rechareParams.setMerchant_order_no("test0000003");
        rechareParams.setMerchant_id("55372");
        Random random = new Random();
        int amount = random.nextInt(107);
        rechareParams.setPay_amount(new BigDecimal("3000.00"));
        rechareParams.setNotify_url("https://testapi.jjezx.info/m_pay/pay_wuxian_notifies");
//        rechareParams.setRemark(StringUtil.createRandomStr1(3));
        TreeMap<String, Object> map = new TreeMap<>();
        map.put("merchant_id", rechareParams.getMerchant_id());
        map.put("merchant_order_no", "test0000003");
        map.put("pay_amount", "3000.00");
        map.put("notify_url", rechareParams.getNotify_url());
        String sign_str = new CommonUtil().getSign(map);
        sign_str = sign_str.concat("key=BkEuentrMfXbaVRBBkYZeHAMREwSKgsc");
        log.info("签名字符串: {}", sign_str);
        String sign =  MD5Util.getMD5Str(sign_str).toUpperCase();
        rechareParams.setSign(sign);
        try {
            String reponse = HttpClientUtil.doPostJson("http://ec55666.top/recharge/create_a", JSONObject.toJSONString(rechareParams));
            JSONObject jsonObject = JSONObject.parseObject(reponse);
            System.out.print("创建订单测试单元结果" + jsonObject);
        }catch (BaseException e){
            throw new RuntimeException(e);
        }
    }
    public void testCreateHttpA() {
        //ZfMerchant xMerchant = zfMerchantService.queryById(10019);
        RechareParams rechareParams = new RechareParams();
        rechareParams.setMerchant_order_no("test0000001");
        rechareParams.setMerchant_id("55372");
        Random random = new Random();
        int amount = random.nextInt(107);
        rechareParams.setPay_amount(new BigDecimal("1000.00"));
        rechareParams.setNotify_url("https://testapi.jjezx.info/m_pay/pay_wuxian_notifies");
//        rechareParams.setRemark(StringUtil.createRandomStr1(3));
        TreeMap<String, Object> map = new TreeMap<>();
        map.put("merchant_id", rechareParams.getMerchant_id());
        map.put("merchant_order_no", "test0000001");
        map.put("pay_amount", "1000.00");
        map.put("notify_url", rechareParams.getNotify_url());
        String sign_str = new CommonUtil().getSign(map);
        sign_str = sign_str.concat("key=BkEuentrMfXbaVRBBkYZeHAMREwSKgsc");
        log.info("签名字符串: {}", sign_str);
        String sign =  MD5Util.getMD5Str(sign_str).toUpperCase();
        rechareParams.setSign(sign);
        try {
            String reponse = HttpClientUtil.doPostJson("http://ec55666.top/recharge/create_a", JSONObject.toJSONString(rechareParams));
            JSONObject jsonObject = JSONObject.parseObject(reponse);
            System.out.print("创建订单测试单元结果" + jsonObject);
        }catch (BaseException e){
            throw new RuntimeException(e);
        }
    }
}
