package com.code.baseservice.service;

import com.code.baseservice.entity.ZfChannelRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

/**
 * (ZfChannelRecord)表服务接口
 *
 * @author makejava
 * @since 2023-09-03 10:45:35
 */
public interface ZfChannelRecordService {

    /**
     * 通过ID查询单条数据
     *
     * @param channelId 主键
     * @return 实例对象
     */
    ZfChannelRecord queryById(Integer channelId);

    /**
     * 分页查询
     *
     * @param zfChannelRecord 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    Page<ZfChannelRecord> queryByPage(ZfChannelRecord zfChannelRecord, PageRequest pageRequest);

    /**
     * 新增数据
     *
     * @param zfChannelRecord 实例对象
     * @return 实例对象
     */
    ZfChannelRecord insert(ZfChannelRecord zfChannelRecord);

    /**
     * 修改数据
     *
     * @param zfChannelRecord 实例对象
     * @return 实例对象
     */
    void update(ZfChannelRecord zfChannelRecord);

    /**
     * 通过主键删除数据
     *
     * @param channelId 主键
     * @return 是否成功
     */
    boolean deleteById(Integer channelId);

}
