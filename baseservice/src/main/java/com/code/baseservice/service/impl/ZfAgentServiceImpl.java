package com.code.baseservice.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.code.baseservice.base.enums.ResultEnum;
import com.code.baseservice.base.enums.TransTypeEnum;
import com.code.baseservice.base.exception.BaseException;
import com.code.baseservice.dto.backapi.OperaAgentParams;
import com.code.baseservice.dto.backapi.OperaBalanceParams;
import com.code.baseservice.entity.*;
import com.code.baseservice.dao.ZfAgentDao;
import com.code.baseservice.service.ZfAgentRechargeOrderService;
import com.code.baseservice.service.ZfAgentRecordService;
import com.code.baseservice.service.ZfAgentService;
import com.code.baseservice.service.ZfAgentTransService;
import com.code.baseservice.util.Telegram;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * (ZfAgent)表服务实现类
 *
 * @author makejava
 * @since 2023-03-19 22:49:50
 */
@Service("zfAgentService")
@Slf4j
public class ZfAgentServiceImpl implements ZfAgentService {
    @Resource
    private ZfAgentDao zfAgentDao;

    @Autowired
    private ZfAgentTransService zfAgentTransService;

    @Autowired
    private ZfAgentRecordService zfAgentRecordService;

    @Autowired
    private RedisUtilServiceImpl redisUtilService;

    @Autowired
    private ZfAgentRechargeOrderService zfAgentRechargeOrderService;


    /**
     * 通过ID查询单条数据
     *
     * @param agentId 主键
     * @return 实例对象
     */
    @Override
    public ZfAgent queryById(Integer agentId) {
        return this.zfAgentDao.queryById(agentId);
    }


    /**
     * 修改数据
     *
     * @param zfAgent 实例对象
     * @return 实例对象
     */
    @Override
    public int update(ZfAgent zfAgent) {
       return this.zfAgentDao.update(zfAgent);

    }

    /**
     * 修改数据
     *
     * @param zfAgent 实例对象
     * @return 实例对象
     */
    @Override
    public void updateAgentCreditAmount(ZfRecharge zfRecharge, Integer agentId) {
        try{
            log.info("开始更新代理额度  {} 时间 {}", zfRecharge.getMerchantOrderNo(), System.currentTimeMillis());
            if(redisUtilService.tryLock(agentId.toString())){
                log.info("计算代理可收 订单号 {}", zfRecharge.getMerchantOrderNo());
                ZfAgent zfAgent = zfAgentDao.queryById(agentId);
                ZfAgent zfAgent1 = new ZfAgent();
                zfAgent1.setAgentId(zfAgent.getAgentId());
                zfAgent.setAgentId(agentId);
                if(zfRecharge.getOrderStatus() == 1){
                    zfAgent.setAcceptAmount(zfAgent.getAcceptAmount().subtract(zfRecharge.getPayAmount()));
                    zfAgent1.setAcceptAmount(BigDecimal.ZERO.subtract(zfRecharge.getPayAmount()));
                }else if(zfRecharge.getOrderStatus() == 3 ){
                    zfAgent1.setAcceptAmount(BigDecimal.ZERO.add(zfRecharge.getPayAmount()));
                    zfAgent.setAcceptAmount(zfAgent.getAcceptAmount().add(zfRecharge.getPayAmount()));
                }else if(zfRecharge.getOrderStatus() == 5){
                    ZfAgentTrans zfAgentTrans = zfAgentTransService.queryAddTransBySub(zfRecharge.getMerchantOrderNo());
                    if(zfAgentTrans != null){
                        zfAgent1.setAcceptAmount(BigDecimal.ZERO.add(zfRecharge.getPayAmount()));
                        zfAgent.setAcceptAmount(zfAgent.getAcceptAmount().add(zfRecharge.getPayAmount()));
                    }else {
                        log.info("订单无入款减分流水，不给加分 订单号 {}", zfRecharge.getMerchantOrderNo());
                        return;
                    }
                }
                zfAgentTransService.insert(new ZfAgentTrans(zfRecharge, zfAgent,  BigDecimal.ZERO));
               int r =   zfAgentDao.updateAgentFee(zfAgent1);
               if(r == 0){
                   Telegram telegram = new Telegram();
                   telegram.sendWarrnException(zfRecharge, "代理积分扣分失败");
                   log.error("更新代理费用失败");
               }
            }else {
                Telegram telegram = new Telegram();
                telegram.sendWarrnException(zfRecharge, "代理积分扣分失败");
                log.error("终端异常 订单号 {}",zfRecharge.getMerchantOrderNo());
            }
        }catch (Exception e){
            Telegram telegram = new Telegram();
            telegram.sendWarrnException(zfRecharge, "代理积分扣分失败");
            log.error("更新代理余额异常 {}", e);
        }finally {
            log.info("释放锁时间  {} 时间 {}", zfRecharge.getMerchantOrderNo(), System.currentTimeMillis());
            redisUtilService.unlock(agentId.toString());
        }
    }

    /**
     * 更新代理可收额度
     *
     * @param zfAgent 实例对象
     * @return 实例对象
     */
    @Override
    public void updateAgentCreditAmount(ZfWithdraw zfWithdraw, Integer agentId) {
        ZfAgent zfAgent = new ZfAgent();
        zfAgent.setAgentId(agentId);
        if(zfWithdraw.getOrderStatus() == 2){
            zfAgent.setAcceptAmount(BigDecimal.ZERO.subtract(zfWithdraw.getPayAmount()));
        }

        update(zfAgent);
    }



    /**
     * 计算代理费用
     * @param paidAmount
     * @param zfAgent
     */
    @Override
    public void updateAgentFee(ZfRecharge zfRecharge, ZfAgent zfAgent, BigDecimal fee) {
        try {
            log.info("更新代理手续费 {}", zfAgent);

            if(redisUtilService.tryLock(zfAgent.getAgentId().toString())){
                ZfAgent updateAgent  = new ZfAgent();
                updateAgent.setAgentId(zfAgent.getAgentId());
                BigDecimal agentFee =  sumAgentFee(zfRecharge.getPaidAmount(), zfAgent.getRate());
                if(agentFee.compareTo(BigDecimal.ZERO) < 0 ){
                    log.info("代理费率设置错误 {}", zfAgent);
                    return;
                }
                if(fee.compareTo(BigDecimal.ZERO)>0 && agentFee.compareTo(fee) < 0){
                    log.info("代理费率设置错误 {}", zfAgent);
                    return;
                }
                updateAgent.setBalance(agentFee.subtract(fee));
                zfAgent.setBalance(zfAgent.getBalance().add(agentFee.subtract(fee)));
                //新增代理流水
                zfAgentTransService.insert(new ZfAgentTrans(zfRecharge, zfAgent,  agentFee.subtract(fee)));
                //查询订单是否有代理流水补分记录，如果有说明已经超时补分过
                ZfAgentTrans zfAgentTrans = zfAgentTransService.queryAddTransByOrderNo(zfRecharge.getMerchantOrderNo());
                log.info("代理超时流水 {}", zfAgentTrans);
                if(null != zfAgentTrans && zfAgentTrans.getAgentId().equals(zfAgent.getAgentId())){
                    //扣除积分
                    zfAgent.setAcceptAmount(zfAgent.getAcceptAmount().subtract(zfRecharge.getPayAmount()));
                    updateAgent.setAcceptAmount(BigDecimal.ZERO.subtract(zfRecharge.getPayAmount()));
                    //说明订单自动取消过，这个时候，要新增吧积分重新扣回
                    ZfAgentTrans zfAgentTrans1 = new ZfAgentTrans();
                    zfAgentTrans1.buildFailOrderRollbackBySuccess(zfRecharge,zfAgent);
                    zfAgentTransService.insert(zfAgentTrans1);
                }
                //更新代理余额
                int r = zfAgentDao.updateAgentFee(updateAgent);
                if(r == 0){
                    Telegram telegram = new Telegram();
                    telegram.sendWarrnException(zfRecharge, "代理积分扣分失败");
                }
                //如果代理费用为0，假设为一级代理，不然则上下级平点位代理，则不管
                if(fee.compareTo(BigDecimal.ZERO) == 0){
                    //新增代理报表
                    zfAgentRecordService.updateRecord(new ZfAgentRecord(zfAgent,  agentFee.subtract(fee), zfRecharge.getPaidAmount()));
                }else {
                    ZfAgentRecord zfAgentRecord = new ZfAgentRecord();
                    zfAgentRecord.buildAgentRecordBySunAgentSuccessAndParent(zfAgent,agentFee.subtract(fee));
                    zfAgentRecordService.updateRecord(zfAgentRecord);
                }
                if(zfAgent.getParentId() != null && zfAgent.getParentId() != 0){
                    ZfAgent parentAgent = zfAgentDao.queryById(zfAgent.getParentId());
                    log.info("父级代理 {}", zfAgent.getParentId());

                    if(parentAgent == null){
                        return;
                    }
                    updateAgentFee(zfRecharge, parentAgent, agentFee);
                }
            }else {
                Telegram telegram = new Telegram();
                telegram.sendWarrnException(zfRecharge, "代理积分扣分失败");
                log.error("终端异常, {}", zfRecharge.getMerchantOrderNo());
            }

        }catch (Exception e){
            Telegram telegram = new Telegram();
            telegram.sendWarrnException(zfRecharge, "代理积分扣分失败");
            log.error("更新代理余额失败, {}", e);
        }finally {
            redisUtilService.unlock(zfAgent.getAgentId().toString());
        }
    }

    @Override
    public void operatBalance(OperaAgentParams operaAgentParams) {
        ZfAgentTrans zfAgentTrans = new ZfAgentTrans();
        ZfAgent zfAgent = new ZfAgent();
        zfAgent.setAgentId(operaAgentParams.getAgentId());
        zfAgentTrans.setAgentId(operaAgentParams.getAgentId());
        ZfAgent zfAgent1 = zfAgentDao.queryById(operaAgentParams.getAgentId());
        if(null  != zfAgent1.getParentId() && zfAgent1.getParentId() != 0 && !operaAgentParams.getIsAdmin() && !operaAgentParams.getIsFinsh()){
            //给下级加积分扣上级，计算上级积分
            if(operaAgentParams.getTransType().equals(TransTypeEnum.RRCHARGE.getValue())) {
                ZfAgent zfAgentParent = zfAgentDao.queryById(zfAgent1.getParentId());
                if(zfAgentParent.getAcceptAmount().compareTo(operaAgentParams.getAcceptAmount()) < 0){
                    log.info("余额不够");
                    throw new BaseException(ResultEnum.NO_ENOUGH_AMOUNT);
                }
            }
        }
        if(zfAgent1.getAcceptAmount().compareTo(operaAgentParams.getAcceptAmount())<0
                && operaAgentParams.getTransType() == TransTypeEnum.TRANSFER.getValue()){
            log.info("分数不能扣成负数");
            throw new BaseException(ResultEnum.NO_ENOUGH_AMOUNT);
        }
        zfAgentTrans.setPreBalance(zfAgent1.getBalance());
        zfAgentTrans.setBalance(zfAgent1.getBalance());
        zfAgentTrans.setTransType(operaAgentParams.getTransType());
        zfAgentTrans.setAcceptAmount(zfAgent1.getAcceptAmount());
        if(operaAgentParams.getTransType().equals(TransTypeEnum.RRCHARGE.getValue())){
            zfAgentTrans.setAcceptAmount(operaAgentParams.getAcceptAmount().add(zfAgent1.getAcceptAmount()));
            zfAgentTrans.setAmount(operaAgentParams.getAcceptAmount());
            zfAgentTrans.setPreAcceptAmount(zfAgent1.getAcceptAmount());
            zfAgent.setAcceptAmount(operaAgentParams.getAcceptAmount());
        }else {
            zfAgent.setAcceptAmount(BigDecimal.ZERO.subtract(operaAgentParams.getAcceptAmount()));
            zfAgentTrans.setAmount(operaAgentParams.getAcceptAmount());
            zfAgentTrans.setAcceptAmount(zfAgent1.getAcceptAmount().subtract(operaAgentParams.getAcceptAmount()));
            zfAgentTrans.setPreAcceptAmount(zfAgent1.getAcceptAmount());

        }
        zfAgentTrans.setTransType(operaAgentParams.getTransType());
        zfAgentTrans.setRemark(operaAgentParams.getRemark());
        zfAgentTransService.insert(zfAgentTrans);
        log.info("更新代理信息 {}", zfAgent);
        zfAgentDao.updateAgentFee(zfAgent);
        //如果有上级代理，则返回上级代理增加积分,不能无限制递归增分，只限制一次
        if( !operaAgentParams.getIsFinsh() && (zfAgent1.getParentId() != null && zfAgent1.getParentId() != 0)   && !operaAgentParams.getIsAdmin() ){
            operaAgentParams.setAgentId(zfAgent1.getParentId());
            if(operaAgentParams.getTransType() == TransTypeEnum.TRANSFER.getValue()){
                operaAgentParams.setTransType(TransTypeEnum.RRCHARGE.getValue());
            }else {
                operaAgentParams.setTransType(TransTypeEnum.TRANSFER.getValue());
            }
            operaAgentParams.setIsFinsh(true);
            operaAgentParams.setRemark("给下级下分扣除" + operaAgentParams.getAcceptAmount());
            log.info("开始操作上级代理积分 {}", operaAgentParams);

            operatBalance(operaAgentParams);
        }
    }

    @Override
    public void changeBalance(OperaAgentParams operaAgentParams) {
        ZfAgentTrans zfAgentTrans = new ZfAgentTrans();
        ZfAgent zfAgent = new ZfAgent();
        zfAgent.setAgentId(operaAgentParams.getAgentId());
        zfAgent.setAcceptAmount(operaAgentParams.getAcceptAmount());
        zfAgentTrans.setAgentId(operaAgentParams.getAgentId());
        ZfAgent zfAgent1 = zfAgentDao.queryById(operaAgentParams.getAgentId());
        if(zfAgent1.getBalance().compareTo(operaAgentParams.getAmount()) < 0){
            throw new BaseException(ResultEnum.NO_COMMISSION_AMOUNT);
        }
        zfAgent.setBalance(zfAgent1.getBalance().subtract(operaAgentParams.getAmount()));
        zfAgent.setAcceptAmount(zfAgent1.getAcceptAmount().add(operaAgentParams.getAmount()));
        zfAgentTrans.setPreBalance(zfAgent1.getBalance());
        zfAgentTrans.setBalance(zfAgent1.getBalance().subtract(operaAgentParams.getAmount()));
        zfAgentTrans.setTransType(TransTypeEnum.INTERNAL.getValue());
        zfAgentTrans.setAmount(operaAgentParams.getAmount());
        zfAgentTrans.setAcceptAmount(zfAgent1.getAcceptAmount().add(operaAgentParams.getAmount()));
        zfAgentTrans.setPreAcceptAmount(zfAgent1.getAcceptAmount());

        zfAgentTrans.setRemark("佣金转积分金额");
        zfAgentTransService.insert(zfAgentTrans);
        zfAgentDao.update(zfAgent);
    }

    @Override
    public void recharge(OperaAgentParams operaAgentParams) {
       ZfAgentRechargeOrder zfAgentRechargeOrder =  zfAgentRechargeOrderService.queryById(operaAgentParams.getOrderNo());
        if(zfAgentRechargeOrder.getOrderStatus() != 2){
            throw new BaseException(ResultEnum.ORDER_STATUS_ERROR);
        }
        zfAgentRechargeOrder.setOrderStatus(3);
        zfAgentRechargeOrder.setPaidAmount(operaAgentParams.getAmount());
        zfAgentRechargeOrder.setRemark(operaAgentParams.getRemark());
        zfAgentRechargeOrder.setUpdateby(operaAgentParams.getUpdateBy());
        updateAgentCreditAmount(zfAgentRechargeOrder);
        zfAgentRechargeOrderService.update(zfAgentRechargeOrder);
    }

    /**
     * 修改数据
     *
     * @param zfAgent 实例对象
     * @return 实例对象
     */
    @Override
    public void updateAgentCreditAmount(ZfAgentRechargeOrder zfAgentRechargeOrder) {
        Integer agentId = zfAgentRechargeOrder.getAgentId();

        try{
            log.info("开始更新代理额度  {} 时间 {}", zfAgentRechargeOrder.getOrderNo(), System.currentTimeMillis());
            if(redisUtilService.tryLock(zfAgentRechargeOrder.getAgentId().toString())){

                log.info("计算代理可收 订单号 {}", zfAgentRechargeOrder.getOrderNo());
                ZfAgent zfAgent = zfAgentDao.queryById(agentId);
                ZfAgent zfAgent1 = new ZfAgent();
                zfAgent1.setAgentId(agentId);
                zfAgent.setAgentId(agentId);
                if(zfAgentRechargeOrder.getOrderStatus() == 3){
                    zfAgent.setAcceptAmount(zfAgent.getAcceptAmount().add(zfAgentRechargeOrder.getPaidAmount()));
                    zfAgent1.setAcceptAmount(zfAgentRechargeOrder.getPayAmount());
                }
                zfAgentTransService.insert(new ZfAgentTrans(zfAgentRechargeOrder, zfAgent));
                zfAgentDao.updateAgentFee(zfAgent1);
            }else {
                log.error("终端异常 订单号 {}",zfAgentRechargeOrder.getOrderNo());
            }
        }catch (Exception e){
            log.error("更新代理余额异常 {}", e);
        }finally {
            log.info("释放锁时间  {} 时间 {}", zfAgentRechargeOrder.getOrderNo(), System.currentTimeMillis());
            redisUtilService.unlock(agentId.toString());
        }
    }


    public BigDecimal sumAgentFee(BigDecimal paidAmount, String rate) {
        try{
            if(Strings.isEmpty(rate)){
                return  BigDecimal.ZERO;
            }
            JSONObject jsonObject = JSONObject.parseObject(rate);
            if(jsonObject.containsKey("recharge")){
                JSONObject rechargeRate = jsonObject.getJSONObject("recharge");
                if(!Strings.isEmpty(rechargeRate.getString("rate_type"))&& !Strings.isEmpty(rechargeRate.getString("rate_value"))){
                    Integer rate_type = rechargeRate.getInteger("rate_type");
                    Double rate_value = rechargeRate.getDoubleValue("rate_value");
                    if(rate_type == 0){
                        return BigDecimal.valueOf(rate_value);
                    }else{
                        BigDecimal rate1 = BigDecimal.valueOf(rate_value).divide(new BigDecimal("100"));
                        return paidAmount.multiply(rate1);
                    }
                }
            }
            return BigDecimal.ZERO;
        }catch (Exception e){
            log.info("会员手续费异常", e);
            return BigDecimal.ZERO;
        }
    }
}
