package com.code.baseservice.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.code.baseservice.base.enums.TransTypeEnum;
import com.code.baseservice.dto.backapi.OperaAgentParams;
import com.code.baseservice.dto.backapi.OperaBalanceParams;
import com.code.baseservice.entity.*;
import com.code.baseservice.dao.ZfAgentDao;
import com.code.baseservice.service.ZfAgentRecordService;
import com.code.baseservice.service.ZfAgentService;
import com.code.baseservice.service.ZfAgentTransService;
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
    public ZfAgent update(ZfAgent zfAgent) {
        this.zfAgentDao.update(zfAgent);
        return this.queryById(zfAgent.getAgentId());
    }

    /**
     * 修改数据
     *
     * @param zfAgent 实例对象
     * @return 实例对象
     */
    @Override
    public void updateAgentCreditAmount(ZfRecharge zfRecharge, Integer agentId) {
        log.info("计算代理可收 订单号 {}", zfRecharge.getMerchantOrderNo());
        ZfAgent zfAgent = zfAgentDao.queryById(agentId);
        zfAgent.setAgentId(agentId);
        if(zfRecharge.getOrderStatus() == 1){
            zfAgent.setAcceptAmount(zfAgent.getAcceptAmount().subtract(zfRecharge.getPayAmount()));
        }else if(zfRecharge.getOrderStatus() == 3){
            zfAgent.setAcceptAmount(zfAgent.getAcceptAmount().add(zfRecharge.getPayAmount()));
        }
        zfAgentTransService.insert(new ZfAgentTrans(zfRecharge, zfAgent,  BigDecimal.ZERO));
        update(zfAgent);
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
        BigDecimal agentFee =  sumAgentFee(zfRecharge.getPaidAmount(), zfAgent.getRate());
        if(agentFee.compareTo(BigDecimal.ZERO) == 0){
            return;
        }
        //更新代理余额
        zfAgentDao.updateAgentFee(zfAgent.getAgentId(), agentFee.subtract(fee));
        //新增代理流水
        zfAgentTransService.insert(new ZfAgentTrans(zfRecharge, zfAgent,  agentFee.subtract(fee)));
        //新增代理报表
        zfAgentRecordService.updateRecord(new ZfAgentRecord(zfAgent,  agentFee.subtract(fee), zfRecharge.getPaidAmount()));
        if(zfAgent.getParentId() != null && zfAgent.getParentId() != 0){
            ZfAgent parentAgent = zfAgentDao.queryById(zfAgent.getParentId());
            if(parentAgent == null){
                log.info("父级代理不存在 {}", zfAgent.getParentId());
                return;
            }
            updateAgentFee(zfRecharge, parentAgent, agentFee);
        }
    }

    @Override
    public void operatBalance(OperaAgentParams operaAgentParams) {
        ZfAgentTrans zfAgentTrans = new ZfAgentTrans();
        ZfAgent zfAgent = new ZfAgent();
        zfAgent.setAgentId(operaAgentParams.getAgentId());
        zfAgentTrans.setAgentId(operaAgentParams.getAgentId());
        ZfAgent zfAgent1 = zfAgentDao.queryById(operaAgentParams.getAgentId());
        zfAgentTrans.setPreBalance(zfAgent1.getBalance());
        zfAgentTrans.setBalance(zfAgent1.getBalance());
        zfAgentTrans.setTransType(operaAgentParams.getTransType());
        zfAgentTrans.setAcceptAmount(zfAgent1.getAcceptAmount());
        if(operaAgentParams.getTransType().equals(TransTypeEnum.RRCHARGE.getValue())){
            if(null != operaAgentParams.getBalance()){
                zfAgentTrans.setBalance(operaAgentParams.getBalance().add(zfAgent1.getBalance()));
                zfAgentTrans.setAmount(operaAgentParams.getBalance());
                zfAgent.setBalance(operaAgentParams.getBalance().add(zfAgent1.getBalance()));
            }else {
                zfAgentTrans.setAcceptAmount(operaAgentParams.getAcceptAmount().add(zfAgent1.getAcceptAmount()));
                zfAgentTrans.setAmount(operaAgentParams.getAcceptAmount());
                zfAgent.setAcceptAmount(operaAgentParams.getAcceptAmount().add(zfAgent1.getAcceptAmount()));
            }
        }else {
            if(null != operaAgentParams.getBalance()){
                zfAgentTrans.setBalance(zfAgent1.getBalance().subtract(operaAgentParams.getBalance()));
                zfAgentTrans.setAmount(operaAgentParams.getBalance());
                zfAgent.setBalance(BigDecimal.ZERO.subtract(operaAgentParams.getBalance()));
            }else {
                zfAgent.setAcceptAmount(zfAgent1.getAcceptAmount().subtract(operaAgentParams.getAcceptAmount()));
                zfAgentTrans.setAmount(operaAgentParams.getAcceptAmount());
                zfAgentTrans.setAcceptAmount(zfAgent1.getAcceptAmount().subtract(operaAgentParams.getAcceptAmount()));
            }
        }
        zfAgentTrans.setTransType(operaAgentParams.getTransType());
        zfAgentTrans.setRemark(operaAgentParams.getRemark());
        zfAgentTransService.insert(zfAgentTrans);
        zfAgentDao.update(zfAgent);
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
