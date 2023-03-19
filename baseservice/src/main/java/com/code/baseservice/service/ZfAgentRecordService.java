package com.code.baseservice.service;

import com.code.baseservice.entity.ZfAgentRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

/**
 * (ZfAgentRecord)表服务接口
 *
 * @author makejava
 * @since 2023-03-19 23:06:12
 */
public interface ZfAgentRecordService {

    /**
     * 通过ID查询单条数据
     *
     * @param agentId 主键
     * @return 实例对象
     */
    ZfAgentRecord queryById(Integer agentId);

    /**
     * 分页查询
     *
     * @param zfAgentRecord 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    Page<ZfAgentRecord> queryByPage(ZfAgentRecord zfAgentRecord, PageRequest pageRequest);

    /**
     * 新增数据
     *
     * @param zfAgentRecord 实例对象
     * @return 实例对象
     */
    ZfAgentRecord insert(ZfAgentRecord zfAgentRecord);

    /**
     * 修改数据
     *
     * @param zfAgentRecord 实例对象
     * @return 实例对象
     */
    ZfAgentRecord update(ZfAgentRecord zfAgentRecord);

    /**
     * 通过主键删除数据
     *
     * @param agentId 主键
     * @return 是否成功
     */
    boolean deleteById(Integer agentId);

}
