package com.code.baseservice.service.impl;

import com.code.baseservice.dao.ZfChannelRecordDao;
import com.code.baseservice.entity.ZfChannelRecord;
import com.code.baseservice.service.ZfChannelRecordService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * (ZfChannelRecord)表服务实现类
 *
 * @author makejava
 * @since 2023-09-03 10:45:36
 */
@Service("zfChannelRecordService")
public class ZfChannelRecordServiceImpl implements ZfChannelRecordService {
    @Resource
    private ZfChannelRecordDao zfChannelRecordDao;

    /**
     * 通过ID查询单条数据
     *
     * @param channelId 主键
     * @return 实例对象
     */
    @Override
    public ZfChannelRecord queryById(Integer channelId) {
        return this.zfChannelRecordDao.queryById(channelId);
    }

    /**
     * 分页查询
     *
     * @param zfChannelRecord 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    @Override
    public Page<ZfChannelRecord> queryByPage(ZfChannelRecord zfChannelRecord, PageRequest pageRequest) {
        long total = this.zfChannelRecordDao.count(zfChannelRecord);
        return new PageImpl<>(this.zfChannelRecordDao.queryAllByLimit(zfChannelRecord, pageRequest), pageRequest, total);
    }

    /**
     * 新增数据
     *
     * @param zfChannelRecord 实例对象
     * @return 实例对象
     */
    @Override
    public ZfChannelRecord insert(ZfChannelRecord zfChannelRecord) {
        this.zfChannelRecordDao.insert(zfChannelRecord);
        return zfChannelRecord;
    }

    /**
     * 修改数据
     *
     * @param zfChannelRecord 实例对象
     * @return 实例对象
     */
    @Override
    public void update(ZfChannelRecord zfChannelRecord) {
        ZfChannelRecord zfChannelRecord1 =  zfChannelRecordDao.queryByIdAndDate(zfChannelRecord);
        if(zfChannelRecord1 == null){
            zfChannelRecordDao.insert(zfChannelRecord);
        }else {
            zfChannelRecordDao.updateRecord(zfChannelRecord);
        }
    }

    /**
     * 通过主键删除数据
     *
     * @param channelId 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer channelId) {
        return this.zfChannelRecordDao.deleteById(channelId) > 0;
    }
}
