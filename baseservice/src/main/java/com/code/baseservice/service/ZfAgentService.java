package com.code.baseservice.service;

import com.code.baseservice.dto.backapi.OperaAgentParams;
import com.code.baseservice.entity.ZfAgent;
import com.code.baseservice.entity.ZfAgentRechargeOrder;
import com.code.baseservice.entity.ZfRecharge;
import com.code.baseservice.entity.ZfWithdraw;

import java.math.BigDecimal;

/**
 * (ZfAgent)表服务接口
 *
 * @author makejava
 * @since 2023-03-19 22:49:50
 */
public interface ZfAgentService {

    /**
     * 通过ID查询单条数据
     *
     * @param agentId 主键
     * @return 实例对象
     */
    ZfAgent queryById(Integer agentId);


    /**
     * 修改数据
     *
     * @param zfAgent 实例对象
     * @return 实例对象
     */
    int update(ZfAgent zfAgent);

   void updateAgentCreditAmount(ZfRecharge zfRecharge, Integer agentId);


    void updateAgentFee(ZfRecharge zfRecharg , Integer agentId,BigDecimal fee);
    void updateAgentFee(ZfWithdraw zfWithdraw , Integer agentId,BigDecimal fee);

    void operatBalance(OperaAgentParams operaAgentParams);

    void changeBalance(OperaAgentParams operaAgentParams);

    void recharge(OperaAgentParams operaAgentParams);

    /**
     *  代理充值
     * @param zfAgentRechargeOrder
     */
     void updateAgentCreditAmount(ZfAgentRechargeOrder zfAgentRechargeOrder) ;

    ZfAgent queryByAccount(String agentName);

    ZfAgent findGroupByAgent(Integer agentId);
}
