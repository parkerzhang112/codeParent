package com.code.baseservice.service.impl;

import com.code.baseservice.dao.ZfTransRecordDao;
import com.code.baseservice.entity.ZfTransRecord;
import com.code.baseservice.service.ZfTransRecordService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * (ZfTransRecord)表服务实现类
 *
 * @author makejava
 * @since 2023-03-19 23:08:34
 */
@Service("zfTransRecordService")
public class ZfTransRecordServiceImpl implements ZfTransRecordService {
    @Resource
    private ZfTransRecordDao zfTransRecordDao;

    /**
     * 通过ID查询单条数据
     *
     * @param transId 主键
     * @return 实例对象
     */
    @Override
    public ZfTransRecord queryById(Integer transId) {
        return this.zfTransRecordDao.queryById(transId);
    }

    /**
     * 分页查询
     *
     * @param zfTransRecord 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    @Override
    public Page<ZfTransRecord> queryByPage(ZfTransRecord zfTransRecord, PageRequest pageRequest) {
        long total = this.zfTransRecordDao.count(zfTransRecord);
        return new PageImpl<>(this.zfTransRecordDao.queryAllByLimit(zfTransRecord, pageRequest), pageRequest, total);
    }

    /**
     * 新增数据
     *
     * @param zfTransRecord 实例对象
     * @return 实例对象
     */
    @Override
    public ZfTransRecord insert(ZfTransRecord zfTransRecord) {
        this.zfTransRecordDao.insert(zfTransRecord);
        return zfTransRecord;
    }

    /**
     * 修改数据
     *
     * @param zfTransRecord 实例对象
     * @return 实例对象
     */
    @Override
    public ZfTransRecord update(ZfTransRecord zfTransRecord) {
        this.zfTransRecordDao.update(zfTransRecord);
        return this.queryById(zfTransRecord.getTransId());
    }

    /**
     * 通过主键删除数据
     *
     * @param transId 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer transId) {
        return this.zfTransRecordDao.deleteById(transId) > 0;
    }
}
