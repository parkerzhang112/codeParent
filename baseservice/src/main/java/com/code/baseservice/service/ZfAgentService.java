package com.code.baseservice.service;

import com.code.baseservice.entity.ZfAgent;
import com.code.baseservice.entity.ZfRecharge;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

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
    ZfAgent update(ZfAgent zfAgent);


    void updateAgentFee(BigDecimal paidAmount, ZfAgent zfAgent, BigDecimal fee);

}
