package com.code.baseservice.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

.service.impl;
        .entity.ZfRecharge;
        .dao.ZfRechargeDao;
        .service.ZfRechargeService;

/**
 * (ZfRecharge)表服务实现类
 *
 * @author makejava
 * @since 2023-03-19 23:08:26
 */
@Service("zfRechargeService")
public class ZfRechargeServiceImpl implements ZfRechargeService {
    @Resource
    private ZfRechargeDao zfRechargeDao;

    /**
     * 通过ID查询单条数据
     *
     * @param merchantOrderNo 主键
     * @return 实例对象
     */
    @Override
    public ZfRecharge queryById(String merchantOrderNo) {
        return this.zfRechargeDao.queryById(merchantOrderNo);
    }

    /**
     * 分页查询
     *
     * @param zfRecharge 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    @Override
    public Page<ZfRecharge> queryByPage(ZfRecharge zfRecharge, PageRequest pageRequest) {
        long total = this.zfRechargeDao.count(zfRecharge);
        return new PageImpl<>(this.zfRechargeDao.queryAllByLimit(zfRecharge, pageRequest), pageRequest, total);
    }

    /**
     * 新增数据
     *
     * @param zfRecharge 实例对象
     * @return 实例对象
     */
    @Override
    public ZfRecharge insert(ZfRecharge zfRecharge) {
        this.zfRechargeDao.insert(zfRecharge);
        return zfRecharge;
    }

    /**
     * 修改数据
     *
     * @param zfRecharge 实例对象
     * @return 实例对象
     */
    @Override
    public ZfRecharge update(ZfRecharge zfRecharge) {
        this.zfRechargeDao.update(zfRecharge);
        return this.queryById(zfRecharge.getMerchantOrderNo());
    }

    /**
     * 通过主键删除数据
     *
     * @param merchantOrderNo 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(String merchantOrderNo) {
        return this.zfRechargeDao.deleteById(merchantOrderNo) > 0;
    }
}
