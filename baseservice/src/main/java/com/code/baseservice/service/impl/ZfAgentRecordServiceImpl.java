package com.code.baseservice.service.impl;

import com.code.baseservice.entity.ZfTransRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import com.code.baseservice.entity.ZfAgentRecord;
import com.code.baseservice.dao.ZfAgentRecordDao;
import com.code.baseservice.service.ZfAgentRecordService;

/**
 * (ZfAgentRecord)表服务实现类
 *
 * @author makejava
 * @since 2023-03-19 23:06:13
 */
@Service("zfAgentRecordService")
public class ZfAgentRecordServiceImpl implements ZfAgentRecordService {
    @Resource
    private ZfAgentRecordDao zfAgentRecordDao;

    /**
     * 通过ID查询单条数据
     *
     * @param agentId 主键
     * @return 实例对象
     */
    @Override
    public ZfAgentRecord queryById(Integer agentId) {
        return this.zfAgentRecordDao.queryById(agentId);
    }

    /**
     * 分页查询
     *
     * @param zfAgentRecord 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    @Override
    public Page<ZfAgentRecord> queryByPage(ZfAgentRecord zfAgentRecord, PageRequest pageRequest) {
        long total = this.zfAgentRecordDao.count(zfAgentRecord);
        return new PageImpl<>(this.zfAgentRecordDao.queryAllByLimit(zfAgentRecord, pageRequest), pageRequest, total);
    }

    /**
     * 新增数据
     *
     * @param zfAgentRecord 实例对象
     * @return 实例对象
     */
    @Override
    public ZfAgentRecord insert(ZfAgentRecord zfAgentRecord) {
        this.zfAgentRecordDao.insert(zfAgentRecord);
        return zfAgentRecord;
    }

    /**
     * 修改数据
     *
     * @param zfAgentRecord 实例对象
     * @return 实例对象
     */
    @Override
    public int update(ZfAgentRecord zfAgentRecord) {
        return this.zfAgentRecordDao.update(zfAgentRecord);

    }

    /**
     * 通过主键删除数据
     *
     * @param agentId 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer agentId) {
        return this.zfAgentRecordDao.deleteById(agentId) > 0;
    }

    @Override
    public void updateRecord(ZfAgentRecord zfAgentRecord) {
          long count = zfAgentRecordDao.count(zfAgentRecord);
        if(count > 0){
            zfAgentRecordDao.update(zfAgentRecord);
        }else {
            zfAgentRecordDao.insert(zfAgentRecord);
        }
    }
}
