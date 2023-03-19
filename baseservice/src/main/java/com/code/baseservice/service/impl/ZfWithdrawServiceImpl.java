package com.code.baseservice.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

.service.impl;
        .entity.ZfWithdraw;
        .dao.ZfWithdrawDao;
        .service.ZfWithdrawService;

/**
 * 提款订单表(ZfWithdraw)表服务实现类
 *
 * @author makejava
 * @since 2023-03-19 23:08:41
 */
@Service("zfWithdrawService")
public class ZfWithdrawServiceImpl implements ZfWithdrawService {
    @Resource
    private ZfWithdrawDao zfWithdrawDao;

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
     * 分页查询
     *
     * @param zfWithdraw 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    @Override
    public Page<ZfWithdraw> queryByPage(ZfWithdraw zfWithdraw, PageRequest pageRequest) {
        long total = this.zfWithdrawDao.count(zfWithdraw);
        return new PageImpl<>(this.zfWithdrawDao.queryAllByLimit(zfWithdraw, pageRequest), pageRequest, total);
    }

    /**
     * 新增数据
     *
     * @param zfWithdraw 实例对象
     * @return 实例对象
     */
    @Override
    public ZfWithdraw insert(ZfWithdraw zfWithdraw) {
        this.zfWithdrawDao.insert(zfWithdraw);
        return zfWithdraw;
    }

    /**
     * 修改数据
     *
     * @param zfWithdraw 实例对象
     * @return 实例对象
     */
    @Override
    public ZfWithdraw update(ZfWithdraw zfWithdraw) {
        this.zfWithdrawDao.update(zfWithdraw);
        return this.queryById(zfWithdraw.getOrderNo());
    }

    /**
     * 通过主键删除数据
     *
     * @param orderNo 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(String orderNo) {
        return this.zfWithdrawDao.deleteById(orderNo) > 0;
    }
}
