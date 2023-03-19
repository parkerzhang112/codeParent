package com.code.baseservice.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

.service.impl;
        .entity.ZfMerchantRecord;
        .dao.ZfMerchantRecordDao;
        .service.ZfMerchantRecordService;

/**
 * (ZfMerchantRecord)表服务实现类
 *
 * @author makejava
 * @since 2023-03-19 23:08:08
 */
@Service("zfMerchantRecordService")
public class ZfMerchantRecordServiceImpl implements ZfMerchantRecordService {
    @Resource
    private ZfMerchantRecordDao zfMerchantRecordDao;

    /**
     * 通过ID查询单条数据
     *
     * @param  主键
     * @return 实例对象
     */
    @Override
    public ZfMerchantRecord queryById( ) {
        return this.zfMerchantRecordDao.queryById();
    }

    /**
     * 分页查询
     *
     * @param zfMerchantRecord 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    @Override
    public Page<ZfMerchantRecord> queryByPage(ZfMerchantRecord zfMerchantRecord, PageRequest pageRequest) {
        long total = this.zfMerchantRecordDao.count(zfMerchantRecord);
        return new PageImpl<>(this.zfMerchantRecordDao.queryAllByLimit(zfMerchantRecord, pageRequest), pageRequest, total);
    }

    /**
     * 新增数据
     *
     * @param zfMerchantRecord 实例对象
     * @return 实例对象
     */
    @Override
    public ZfMerchantRecord insert(ZfMerchantRecord zfMerchantRecord) {
        this.zfMerchantRecordDao.insert(zfMerchantRecord);
        return zfMerchantRecord;
    }

    /**
     * 修改数据
     *
     * @param zfMerchantRecord 实例对象
     * @return 实例对象
     */
    @Override
    public ZfMerchantRecord update(ZfMerchantRecord zfMerchantRecord) {
        this.zfMerchantRecordDao.update(zfMerchantRecord);
        return this.queryById(zfMerchantRecord.get());
    }

    /**
     * 通过主键删除数据
     *
     * @param  主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById( ) {
        return this.zfMerchantRecordDao.deleteById() > 0;
    }
}
