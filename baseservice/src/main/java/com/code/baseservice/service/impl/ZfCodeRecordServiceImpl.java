package com.code.baseservice.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

.service.impl;
        .entity.ZfCodeRecord;
        .dao.ZfCodeRecordDao;
        .service.ZfCodeRecordService;

/**
 * (ZfCodeRecord)表服务实现类
 *
 * @author makejava
 * @since 2023-03-19 23:07:26
 */
@Service("zfCodeRecordService")
public class ZfCodeRecordServiceImpl implements ZfCodeRecordService {
    @Resource
    private ZfCodeRecordDao zfCodeRecordDao;

    /**
     * 通过ID查询单条数据
     *
     * @param codeId 主键
     * @return 实例对象
     */
    @Override
    public ZfCodeRecord queryById(Integer codeId) {
        return this.zfCodeRecordDao.queryById(codeId);
    }

    /**
     * 分页查询
     *
     * @param zfCodeRecord 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    @Override
    public Page<ZfCodeRecord> queryByPage(ZfCodeRecord zfCodeRecord, PageRequest pageRequest) {
        long total = this.zfCodeRecordDao.count(zfCodeRecord);
        return new PageImpl<>(this.zfCodeRecordDao.queryAllByLimit(zfCodeRecord, pageRequest), pageRequest, total);
    }

    /**
     * 新增数据
     *
     * @param zfCodeRecord 实例对象
     * @return 实例对象
     */
    @Override
    public ZfCodeRecord insert(ZfCodeRecord zfCodeRecord) {
        this.zfCodeRecordDao.insert(zfCodeRecord);
        return zfCodeRecord;
    }

    /**
     * 修改数据
     *
     * @param zfCodeRecord 实例对象
     * @return 实例对象
     */
    @Override
    public ZfCodeRecord update(ZfCodeRecord zfCodeRecord) {
        this.zfCodeRecordDao.update(zfCodeRecord);
        return this.queryById(zfCodeRecord.getCodeId());
    }

    /**
     * 通过主键删除数据
     *
     * @param codeId 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer codeId) {
        return this.zfCodeRecordDao.deleteById(codeId) > 0;
    }
}
