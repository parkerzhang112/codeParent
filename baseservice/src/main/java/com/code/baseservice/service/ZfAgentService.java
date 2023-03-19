package com.code.baseservice.service;

import com.code.baseservice.entity.ZfAgent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

/**
 * (ZfAgent)表服务接口
 *
 * @author makejava
 * @since 2023-03-19 22:49:50
 */
public interface ZfAgentService {

    /**
     * 通过ID查询单条数据
     *
     * @param agentId 主键
     * @return 实例对象
     */
    ZfAgent queryById(Integer agentId);

    /**
     * 分页查询
     *
     * @param zfAgent 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    Page<ZfAgent> queryByPage(ZfAgent zfAgent, PageRequest pageRequest);

    /**
     * 新增数据
     *
     * @param zfAgent 实例对象
     * @return 实例对象
     */
    ZfAgent insert(ZfAgent zfAgent);

    /**
     * 修改数据
     *
     * @param zfAgent 实例对象
     * @return 实例对象
     */
    ZfAgent update(ZfAgent zfAgent);

    /**
     * 通过主键删除数据
     *
     * @param agentId 主键
     * @return 是否成功
     */
    boolean deleteById(Integer agentId);

}
