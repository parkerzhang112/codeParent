package com.code.baseservice.service;

import com.code.baseservice.entity.ZfAgentRechargeOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

/**
 * (ZfAgentRechargeOrder)表服务接口
 *
 * @author makejava
 * @since 2023-08-21 23:23:46
 */
public interface ZfAgentRechargeOrderService {

    /**
     * 通过ID查询单条数据
     *
     * @param orderNo 主键
     * @return 实例对象
     */
    ZfAgentRechargeOrder queryById(String orderNo);

    /**
     * 分页查询
     *
     * @param zfAgentRechargeOrder 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    Page<ZfAgentRechargeOrder> queryByPage(ZfAgentRechargeOrder zfAgentRechargeOrder, PageRequest pageRequest);

    /**
     * 新增数据
     *
     * @param zfAgentRechargeOrder 实例对象
     * @return 实例对象
     */
    ZfAgentRechargeOrder insert(ZfAgentRechargeOrder zfAgentRechargeOrder);

    /**
     * 修改数据
     *
     * @param zfAgentRechargeOrder 实例对象
     * @return 实例对象
     */
    ZfAgentRechargeOrder update(ZfAgentRechargeOrder zfAgentRechargeOrder);

    /**
     * 通过主键删除数据
     *
     * @param orderNo 主键
     * @return 是否成功
     */
    boolean deleteById(String orderNo);

}
