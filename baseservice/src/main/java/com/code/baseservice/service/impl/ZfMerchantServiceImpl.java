package com.code.baseservice.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

.service.impl;
        .entity.ZfMerchant;
        .dao.ZfMerchantDao;
        .service.ZfMerchantService;

/**
 * (ZfMerchant)表服务实现类
 *
 * @author makejava
 * @since 2023-03-19 23:07:58
 */
@Service("zfMerchantService")
public class ZfMerchantServiceImpl implements ZfMerchantService {
    @Resource
    private ZfMerchantDao zfMerchantDao;

    /**
     * 通过ID查询单条数据
     *
     * @param merchantId 主键
     * @return 实例对象
     */
    @Override
    public ZfMerchant queryById(Integer merchantId) {
        return this.zfMerchantDao.queryById(merchantId);
    }

    /**
     * 分页查询
     *
     * @param zfMerchant 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    @Override
    public Page<ZfMerchant> queryByPage(ZfMerchant zfMerchant, PageRequest pageRequest) {
        long total = this.zfMerchantDao.count(zfMerchant);
        return new PageImpl<>(this.zfMerchantDao.queryAllByLimit(zfMerchant, pageRequest), pageRequest, total);
    }

    /**
     * 新增数据
     *
     * @param zfMerchant 实例对象
     * @return 实例对象
     */
    @Override
    public ZfMerchant insert(ZfMerchant zfMerchant) {
        this.zfMerchantDao.insert(zfMerchant);
        return zfMerchant;
    }

    /**
     * 修改数据
     *
     * @param zfMerchant 实例对象
     * @return 实例对象
     */
    @Override
    public ZfMerchant update(ZfMerchant zfMerchant) {
        this.zfMerchantDao.update(zfMerchant);
        return this.queryById(zfMerchant.getMerchantId());
    }

    /**
     * 通过主键删除数据
     *
     * @param merchantId 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer merchantId) {
        return this.zfMerchantDao.deleteById(merchantId) > 0;
    }
}
