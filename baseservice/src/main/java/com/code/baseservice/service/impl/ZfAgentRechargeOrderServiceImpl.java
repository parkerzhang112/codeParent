package com.code.baseservice.service.impl;

import com.code.baseservice.entity.ZfAgentRechargeOrder;
import com.code.baseservice.dao.ZfAgentRechargeOrderDao;
import com.code.baseservice.service.ZfAgentRechargeOrderService;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import javax.annotation.Resource;

/**
 * (ZfAgentRechargeOrder)表服务实现类
 *
 * @author makejava
 * @since 2023-08-21 23:23:46
 */
@Service("zfAgentRechargeOrderService")
public class ZfAgentRechargeOrderServiceImpl implements ZfAgentRechargeOrderService {
    @Resource
    private ZfAgentRechargeOrderDao zfAgentRechargeOrderDao;

    /**
     * 通过ID查询单条数据
     *
     * @param orderNo 主键
     * @return 实例对象
     */
    @Override
    public ZfAgentRechargeOrder queryById(String orderNo) {
        return this.zfAgentRechargeOrderDao.queryById(orderNo);
    }

    /**
     * 分页查询
     *
     * @param zfAgentRechargeOrder 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    @Override
    public Page<ZfAgentRechargeOrder> queryByPage(ZfAgentRechargeOrder zfAgentRechargeOrder, PageRequest pageRequest) {
        long total = this.zfAgentRechargeOrderDao.count(zfAgentRechargeOrder);
        return new PageImpl<>(this.zfAgentRechargeOrderDao.queryAllByLimit(zfAgentRechargeOrder, pageRequest), pageRequest, total);
    }

    /**
     * 新增数据
     *
     * @param zfAgentRechargeOrder 实例对象
     * @return 实例对象
     */
    @Override
    public ZfAgentRechargeOrder insert(ZfAgentRechargeOrder zfAgentRechargeOrder) {
        this.zfAgentRechargeOrderDao.insert(zfAgentRechargeOrder);
        return zfAgentRechargeOrder;
    }

    /**
     * 修改数据
     *
     * @param zfAgentRechargeOrder 实例对象
     * @return 实例对象
     */
    @Override
    public ZfAgentRechargeOrder update(ZfAgentRechargeOrder zfAgentRechargeOrder) {
        this.zfAgentRechargeOrderDao.update(zfAgentRechargeOrder);
        return this.queryById(zfAgentRechargeOrder.getOrderNo());
    }

    /**
     * 通过主键删除数据
     *
     * @param orderNo 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(String orderNo) {
        return this.zfAgentRechargeOrderDao.deleteById(orderNo) > 0;
    }
}
