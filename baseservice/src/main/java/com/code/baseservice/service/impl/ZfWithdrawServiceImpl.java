package com.code.baseservice.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.code.baseservice.base.constant.RedisConstant;
import com.code.baseservice.base.enums.ResultEnum;
import com.code.baseservice.base.enums.TransOrderTypeEnum;
import com.code.baseservice.base.enums.TransTypeEnum;
import com.code.baseservice.base.exception.BaseException;
import com.code.baseservice.dao.ZfWithdrawDao;
import com.code.baseservice.dto.autoapi.TransParams;
import com.code.baseservice.dto.backapi.OperaOrderParams;
import com.code.baseservice.dto.payapi.QueryParams;
import com.code.baseservice.dto.payapi.TransferParams;
import com.code.baseservice.entity.*;
import com.code.baseservice.service.*;
import com.code.baseservice.util.CommonUtil;
import com.code.baseservice.util.HttpClientUtil;
import com.code.baseservice.util.MD5Util;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

/**
 * 提款订单表(ZfWithdraw)表服务实现类
 *
 * @author makejava
 * @since 2023-03-19 23:08:41
 */
@Service("zfWithdrawService")
@Slf4j
public class ZfWithdrawServiceImpl implements ZfWithdrawService {
    @Resource
    private ZfWithdrawDao zfWithdrawDao;

    @Autowired
    private RedisUtilService redisUtilService;

    @Autowired
    private ZfMerchantService zfMerchantService;

    @Autowired
    private ZfMerchantRecordService zfMerchantRecordService;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private ZfMerchantTransService  zfMerchantTransService;

    @Autowired
    private ZfChannelService zfChannelService;

    @Autowired
    private CommonService commonService;

    @Autowired
    private ZfAgentService zfAgentService;

    @Autowired
    private ZfTransRecordService zfTransRecordService;

    @Value("${order-trans-prefix:W}")
    private String orderPrex;

    /**
     * 通过ID查询单条数据
     *
     * @param orderNo 主键
     * @return 实例对象
     */
    @Override
    public ZfWithdraw queryById(String orderNo) {
        return this.zfWithdrawDao.queryById(orderNo);
    }




    /**
     * 创建出款订单
     * @param transParams
     * @return
     */
    @Override
    public JSONObject create(TransferParams transParams) {
        //去重
        buildRepeat(transParams);
        redisUtilService.mlock(RedisConstant.TRANS_LOCK,2000);
        ZfMerchant xMerchant = zfMerchantService.vaildMerchant(transParams.getMerchant_id().toString());
        //验证有效商户
        zfMerchantService.verifSign(transParams,xMerchant);
        //支持银行
        zfMerchantService.verifMerchantBalance(xMerchant, transParams);

        vaildResfuse(transParams);
        //代付反查
        vaildOrderByMerchant(transParams, xMerchant);
        //校验商户余额
        List<ZfChannel> xChannels =  zfChannelService.selectChannel(transParams);
        ZfChannel zfChannel = selectOneCardByRobin(xChannels);

        ZfWithdraw zfWithdraw = createOrder(transParams, zfChannel, xMerchant);
        String key = transParams.getMerchant_id() + "_" +transParams.getMerchant_order_no();
        redisUtilService.set(key, 1);
        redisUtilService.set("notice:admin:", 1,1200);
        //结束商户余额
        zfMerchantService.sumMerchantBalance(xMerchant.getMerchantId(), BigDecimal.ZERO.subtract(transParams.getPay_amount()).subtract(zfWithdraw.getChannelFee()));
        zfMerchantTransService.insert(new ZfMerchantTrans(zfWithdraw, xMerchant,TransTypeEnum.TRANSFER.getValue()));
        try {
            JSONObject jsonObject = commonService.create(zfChannel, zfWithdraw);
            if(jsonObject.getInteger("code") == 200){
                zfWithdraw.setOrderStatus(1);
                zfWithdrawDao.update(zfWithdraw);
            }
        }catch (Exception e){
            log.error("代付入单异常失败", e.getStackTrace());
        }
        JSONObject result = buildResult(transParams, zfWithdraw, xMerchant);
        //组着参数返回
        return result;
    }

    @Override
    public JSONObject query(QueryParams queryParams) {
        //验证有效商户
        ZfMerchant xMerchant =  zfMerchantService.vaildMerchant(queryParams.getMerchant_Id());
        //验签
        zfMerchantService.verifSign(queryParams, xMerchant);
        //查询订单
        ZfWithdraw zfWithdraw = zfWithdrawDao.queryByParams(queryParams.getMerchant_order_no(), Integer.valueOf(queryParams.getMerchant_Id()));
        if(Objects.isNull(zfWithdraw)){
            throw  new BaseException(ResultEnum.ORDER_NO_EXIST);
        }
        JSONObject result  = buildViewResult(zfWithdraw, xMerchant);
        //组合参数返回
        return result;
    }

    @Override
    public ZfWithdraw tryFindOrderByTrans(TransParams transParams1) {
        return null;
    }

    @Override
    public void onPaidOrderThird(ZfWithdraw zfWithdraw) {
        String redisKey = RedisConstant.PAID_ORDER + zfWithdraw.getOrderNo();
        RLock rLock = redissonClient.getLock(redisKey);
        try {
            if (rLock.tryLock(2, 5, TimeUnit.SECONDS)) {
                //订单信息的写入
                zfChannelService.updateChannelFee(zfWithdraw);
                zfWithdraw.setOrderStatus(2);
                zfWithdrawDao.updatePaidOrder(zfWithdraw);
            }
        } catch (Exception e) {
            log.error("订单号 {} 系统异常", zfWithdraw.getMerchantOrderNo(), e);
        } finally {
            redisUtilService.unlock(redisKey);
        }
    }

    @Override
    public void confirmOrder(OperaOrderParams operaOrderParams) {
        log.info("开始确认订单 {}", operaOrderParams);
        ZfWithdraw zfWithdraw = zfWithdrawDao.queryById(operaOrderParams.getOrderNo());
        if (zfWithdraw.getOrderStatus() != 1) {
            log.info("订单已处理 订单号 {}", zfWithdraw.getMerchantOrderNo());
            throw new BaseException(ResultEnum.ORDER_STATUS_ERROR);
        }

        //如果已经有银行卡接单，清空
        if(!zfWithdraw.getAgentId().equals( operaOrderParams.getAgentId() )){
            log.info("订单分配者异常 订单号 {}", zfWithdraw.getMerchantOrderNo());
            throw new BaseException(ResultEnum.DE_ERR);
        }
        ZfAgent zfAgent = zfAgentService.queryById(operaOrderParams.getAgentId());
        zfWithdraw.setAgentId(zfAgent.getAgentId());
        zfWithdraw.setImage(operaOrderParams.getImage());
        zfWithdraw.setRemark(operaOrderParams.getCloseReason());
        zfWithdraw.setPaidAmount(operaOrderParams.getPaid_amt());
        onPaidOrder(zfWithdraw);
    }

    @Override
    public void cancelOrder(OperaOrderParams operaOrderParams) {
        //修改订单数据
        ZfWithdraw zfWithdraw = zfWithdrawDao.queryById(operaOrderParams.getOrderNo());
        if(zfWithdraw == null){
            throw  new BaseException(ResultEnum.ORDER_NO_EXIST);
        }
        zfWithdraw.setRemark(operaOrderParams.getCloseReason());

        cancaleOrderByWithdraw(zfWithdraw);
    }

    private void cancaleOrderByWithdraw(ZfWithdraw zfWithdraw){
        if (zfWithdraw.getOrderStatus() > 1) {
            log.info("订单已处理 订单号 {}", zfWithdraw.getMerchantOrderNo());
            throw new BaseException(ResultEnum.ERROR);
        }
//        List<ZfTransRecord> zfTransRecords = zfTransRecordService.queryTransByWithdraw(zfWithdraw);
//        if(zfTransRecords.size() == 1 && zfTransRecords.get(0).getTransType().equals(TransTypeEnum.TRANSFER.getValue())){
//            throw new BaseException(ResultEnum.TRANS_EXIST_TRANSFER);
//        }
        if("交易失败".equals(zfWithdraw.getRemark())){
            redisUtilService.set("refuseNameTrans"+zfWithdraw.getCardName(), 3600*24);
        }
        ZfMerchant xMerchant  = zfMerchantService.queryById(zfWithdraw.getMerchantId());
        zfWithdraw.setOrderStatus(3);
        zfWithdraw.setAgentId(0);
        if(zfWithdraw.getParentOrderNo().equals("") &&
                !zfWithdraw.getOrderType().equals(TransOrderTypeEnum.INI_ORDER.getValue())
                && !zfWithdraw.getOrderType().equals(TransOrderTypeEnum.OUT_ORDER.getValue())
        ){
            zfMerchantService.sumMerchantBalance(zfWithdraw.getMerchantId(), zfWithdraw.getPayAmount().add(zfWithdraw.getChannelFee()));
            //cho商户余额
            zfMerchantTransService.insert(new ZfMerchantTrans(zfWithdraw, xMerchant,TransTypeEnum.RRCHARGE.getValue()));
            // 冲正商户余额
        }
        //父订单和未拆单订单才可以分配
        notify(zfWithdraw);
    }

    private void onPaidOrder(ZfWithdraw zfWithdraw) {
        String redisKey = RedisConstant.PAID_ORDER+zfWithdraw.getOrderNo();
        RLock rLock = redissonClient.getLock(redisKey);
        try {
            if(rLock.tryLock( 2,5, TimeUnit.SECONDS)){
                //订单信息的写入
                zfAgentService.updateAgentFee(zfWithdraw, zfWithdraw.getAgentId(), BigDecimal.ZERO);
                if(zfWithdraw.getOrderType().equals(TransOrderTypeEnum.INI_ORDER.getValue())){
                    return;
                }
                zfWithdraw.setOrderStatus(2);
                zfWithdrawDao.updatePaidOrder(zfWithdraw);
                //订单通知
                notify(zfWithdraw);
                //更新日报
                zfMerchantRecordService.updateRecord(new ZfMerchantRecord(zfWithdraw));
                if(zfWithdraw.getPayAmount().compareTo(zfWithdraw.getPaidAmount()) != 0){
                    //商户余额计算
                    log.info("出款金额不一致 订单号 {}", zfWithdraw.getMerchantOrderNo());
                }
            }
        }catch (Exception e) {
            log.error("订单号 {} 系统异常", zfWithdraw.getMerchantOrderNo(), e);
        }finally {
            if(rLock.isLocked() && rLock.isHeldByCurrentThread()){
                rLock.unlock();
            }
        }
    }

    @Override
    public void notify(ZfWithdraw zfWithdraw) {
        try {
            if(zfWithdraw.getParentOrderNo().equals("")){
                if(!zfWithdraw.getNotifyUrl().contains("127.0.0.1")) {
                    ZfMerchant zfMerchant = zfMerchantService.queryById(zfWithdraw.getMerchantId());
                    TreeMap<String, Object>  map = new TreeMap<>();
                    map.put("merchant_id", zfWithdraw.getMerchantId());
                    map.put("merchant_order_no", zfWithdraw.getMerchantOrderNo());
                    map.put("order_status", zfWithdraw.getOrderStatus());
                    map.put("pay_amount", zfWithdraw.getPayAmount());
                    map.put("paid_amount", zfWithdraw.getPaidAmount());
                    String sign_str = new CommonUtil().getSign(map);
                    sign_str = sign_str.concat("key=".concat(zfMerchant.getKey()));
                    String sign = MD5Util.getMD5Str(sign_str);
                    map.put("sign", sign);
                    String response = HttpClientUtil.doPostJson(zfWithdraw.getNotifyUrl(), JSONObject.toJSONString(map));
                    log.info("订单号 {}  推送地址 {}   推送数据 {} , 回调结果 {} 签名字符串 {}", zfWithdraw.getMerchantOrderNo(), zfWithdraw.getNotifyUrl(), map, response, sign_str);
                    if ("success".equals(response) || "回调成功".equals(response)) {
                        zfWithdraw.setNotifyStatus(1);
                        zfWithdraw.setUpdateTime(new Date());
                    } else {
                        JSONObject jsonObject1 = JSONObject.parseObject(response);
                        if (jsonObject1.getInteger("code").equals(200)) {
                            zfWithdraw.setNotifyStatus(1);
                            zfWithdraw.setUpdateTime(new Date());
                        }
                    }
                }


            }
            zfWithdrawDao.update(zfWithdraw);
        } catch (Exception e) {
            log.error("通知异常 订单号 {}", zfWithdraw.getMerchantOrderNo(), e);
            zfWithdraw.setNotifyStatus(2);
            zfWithdraw.setUpdateTime(new Date());
            zfWithdrawDao.update(zfWithdraw);
        }
    }


    private JSONObject buildViewResult(ZfWithdraw zfWithdraw, ZfMerchant xMerchant) {
        TreeMap<String, Object> map = new TreeMap<>();
        map.put("merchant_id", zfWithdraw.getMerchantId());
        map.put("order_no", zfWithdraw.getMerchantOrderNo());
        map.put("order_status", zfWithdraw.getOrderStatus());
        map.put("pay_amount", zfWithdraw.getPayAmount());
        map.put("paid_amount", zfWithdraw.getPaidAmount());
        String sign_str = new CommonUtil().getSign(map);
        sign_str = sign_str.concat("key=".concat(xMerchant.getKey()));
        String sign =  MD5Util.getMD5Str(sign_str).toUpperCase();
        log.info("订单号 {}  签名字符串 {} ", map.get("order_no"), sign_str);
        map.put("sign", sign);
        return new JSONObject(map);
    }

    @Override
    public ZfWithdraw createOrder(TransferParams transParams, ZfChannel zfChannel, ZfMerchant xMerchant) {
        //插入订单号
        String orderNo = getOrderID(orderPrex, xMerchant.getMerchantCode());
        ZfWithdraw zfWithdraw = new ZfWithdraw(transParams);
        zfWithdraw.setChannelId(zfChannel.getChannelId());
        zfWithdraw.setOrderNo(orderNo);
        zfWithdraw.setMerchantId(xMerchant.getMerchantId());
        BigDecimal fee = zfMerchantService.sumMerchantFee(transParams.getPay_amount(),xMerchant);
        zfWithdraw.setChannelFee(fee.add(new BigDecimal(2)));
        zfWithdrawDao.insert(zfWithdraw);

        return zfWithdraw;
    }

    /**
     * 卡下发
     * @param transParams
     * @param xMerchant
     * @return
     */
    public ZfWithdraw createIssueOrder(TransferParams transParams, ZfMerchant xMerchant) {
        //插入订单号
        String orderNo = getOrderID(orderPrex, xMerchant.getMerchantCode());
        ZfWithdraw xTransfer = new ZfWithdraw(transParams);
        xTransfer.setMerchantOrderNo(orderNo);
        xTransfer.setChannelFee(BigDecimal.ZERO);
        xTransfer.setNotifyUrl("http://127.0.0.1");
        xTransfer.setOrderNo(orderNo);
        xTransfer.setMerchantId(xMerchant.getMerchantId());
        zfWithdrawDao.insert(xTransfer);
        return xTransfer;
    }

    @Override
    public void lock(OperaOrderParams operaOrderParams) {
        ZfWithdraw zfWithdraw = zfWithdrawDao.queryById(operaOrderParams.getOrderNo());
        if(zfWithdraw == null){
            log.info("暂无可出款订单");
            throw new BaseException(ResultEnum.ORDER_NO_EXIST);
        }
        if(zfWithdraw.getOrderStatus() != 0){
            throw new BaseException(ResultEnum.ORDER_LOCKED);
        }
        if(zfWithdraw.getAgentId() != null && zfWithdraw.getAgentId() != 0){
            throw new BaseException(ResultEnum.REFUSE_ORRDER);
        }
        redisUtilService.set("notice:agent:" + operaOrderParams.getAgentId(), 1,1200);
        zfWithdraw.setAgentId(operaOrderParams.getAgentId());
        zfWithdraw.setOrderStatus(1);
        zfWithdraw.setUpdateTime(new Date());
        zfWithdrawDao.update(zfWithdraw);
        return  ;
    }

    @Override
    public JSONObject queryByWithdraw( QueryParams queryParams) {
        ZfMerchant zfMerchant = zfMerchantService.queryById(Integer.valueOf(queryParams.getMerchant_Id()));
        zfMerchantService.verifSign(queryParams, zfMerchant);
        ZfWithdraw zfWithdraw = zfWithdrawDao.queryByParams(queryParams.getMerchant_order_no(), Integer.valueOf(queryParams.getMerchant_Id()) );
        if(zfWithdraw == null){
            new BaseException(ResultEnum.ORDER_NO_EXIST);
        }
        ZfChannel zfChannel = zfChannelService.queryById(zfWithdraw.getChannelId());
        if(zfChannel == null){
            new BaseException(ResultEnum.REFUSE_ORRDER);
        }
        JSONObject jsonObject =   commonService.queryByWithdraw(zfChannel,zfWithdraw);
        if("200".equals(jsonObject.getString("code"))){
            if(jsonObject.getInteger("order_status") == 2){
                onPaidOrderThird(zfWithdraw);
            }else if(jsonObject.getInteger("order_status") == 3){
                cancaleOrderByWithdraw(zfWithdraw);
            }
        }
        return buildViewResult(zfWithdraw, zfMerchant);
    }


    private JSONObject buildResult(TransferParams transParams, ZfWithdraw zfWithdraw, ZfMerchant xMerchant) {
        TreeMap<String, Object>  map = new TreeMap<>();
        map.put("order_no", transParams.getMerchant_order_no());
        map.put("order_pay_amount",transParams.getPay_amount());
        String sign_str = new CommonUtil().getSign(map);
        sign_str = sign_str.concat("key=".concat(xMerchant.getKey()));
        String sign =  MD5Util.getMD5Str(sign_str).toUpperCase();
        log.info("订单号 {}  签名字符串 {} ", transParams.getMerchant_order_no(), sign_str);
        map.put("sign", sign);
        map.put("remark", transParams.getRemark());
        return new JSONObject(map);
    }

    private String getOrderID(String orderPrex, String merchantCode) {
        return CommonUtil.getOrderNo(orderPrex, merchantCode);
    }

    private void buildRepeat(TransferParams transParams) {
        log.info("订单号 去重开始", transParams.getMerchant_order_no());
        String key = transParams.getMerchant_id() + "_" +transParams.getMerchant_order_no();
        if(redisUtilService.hasKey(key)){
            log.info("订单号 重复 {}", transParams.getMerchant_order_no());
            throw  new BaseException(ResultEnum.ORDER_REPEAT);
        }
    }

    private ZfChannel selectOneCardByRobin(List<ZfChannel> xChannels) {
        return xChannels.get(0);
    }

    private void vaildOrderByMerchant(TransferParams transParams, ZfMerchant xMerchant) {
        return;
    }

    private void vaildResfuse(TransferParams transParams) {

    }

}
