package com.code.baseservice.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

.service.impl;
        .entity.ZfMerchantTrans;
        .dao.ZfMerchantTransDao;
        .service.ZfMerchantTransService;

/**
 * (ZfMerchantTrans)表服务实现类
 *
 * @author makejava
 * @since 2023-03-19 23:08:17
 */
@Service("zfMerchantTransService")
public class ZfMerchantTransServiceImpl implements ZfMerchantTransService {
    @Resource
    private ZfMerchantTransDao zfMerchantTransDao;

    /**
     * 通过ID查询单条数据
     *
     * @param merchantId 主键
     * @return 实例对象
     */
    @Override
    public ZfMerchantTrans queryById(Integer merchantId) {
        return this.zfMerchantTransDao.queryById(merchantId);
    }

    /**
     * 分页查询
     *
     * @param zfMerchantTrans 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    @Override
    public Page<ZfMerchantTrans> queryByPage(ZfMerchantTrans zfMerchantTrans, PageRequest pageRequest) {
        long total = this.zfMerchantTransDao.count(zfMerchantTrans);
        return new PageImpl<>(this.zfMerchantTransDao.queryAllByLimit(zfMerchantTrans, pageRequest), pageRequest, total);
    }

    /**
     * 新增数据
     *
     * @param zfMerchantTrans 实例对象
     * @return 实例对象
     */
    @Override
    public ZfMerchantTrans insert(ZfMerchantTrans zfMerchantTrans) {
        this.zfMerchantTransDao.insert(zfMerchantTrans);
        return zfMerchantTrans;
    }

    /**
     * 修改数据
     *
     * @param zfMerchantTrans 实例对象
     * @return 实例对象
     */
    @Override
    public ZfMerchantTrans update(ZfMerchantTrans zfMerchantTrans) {
        this.zfMerchantTransDao.update(zfMerchantTrans);
        return this.queryById(zfMerchantTrans.getMerchantId());
    }

    /**
     * 通过主键删除数据
     *
     * @param merchantId 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer merchantId) {
        return this.zfMerchantTransDao.deleteById(merchantId) > 0;
    }
}
