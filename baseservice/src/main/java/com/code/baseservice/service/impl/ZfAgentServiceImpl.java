package com.code.baseservice.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.code.baseservice.entity.ZfAgent;
import com.code.baseservice.dao.ZfAgentDao;
import com.code.baseservice.entity.ZfAgentRecord;
import com.code.baseservice.entity.ZfAgentTrans;
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
     * 计算代理费用
     * @param paidAmount
     * @param zfAgent
     */
    @Override
    public void updateAgentFee(BigDecimal paidAmount, ZfAgent zfAgent, BigDecimal fee) {
        BigDecimal agentFee =  sumAgentFee(paidAmount, zfAgent.getRate());
        if(agentFee.compareTo(BigDecimal.ZERO) == 0){
            return;
        }
        //更新代理余额
        zfAgentDao.updateAgentFee(zfAgent.getAgentId(), agentFee.subtract(fee));
        //新增代理流水
        zfAgentTransService.insert(new ZfAgentTrans(zfAgent,  agentFee.subtract(fee)));
        //新增代理报表
        zfAgentRecordService.updateRecord(new ZfAgentRecord(zfAgent,  agentFee.subtract(fee), paidAmount));
        if(zfAgent.getParentId() != null){
            zfAgentDao.queryById(zfAgent.getParentId());
            updateAgentFee(paidAmount, zfAgent, agentFee);
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
