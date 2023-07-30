package com.code.baseservice.service;

import com.code.baseservice.entity.ZfAgentTrans;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;


/**
 * (ZfAgentTrans)表服务接口
 *
 * @author makejava
 * @since 2023-03-19 23:06:24
 */
public interface ZfAgentTransService {

    /**
     * 通过ID查询单条数据
     *
     * @param agentId 主键
     * @return 实例对象
     */
    ZfAgentTrans queryById(Integer agentId);

    /**
     * 分页查询
     *
     * @param zfAgentTrans 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    Page<ZfAgentTrans> queryByPage(ZfAgentTrans zfAgentTrans, PageRequest pageRequest);

    /**
     * 新增数据
     *
     * @param zfAgentTrans 实例对象
     * @return 实例对象
     */
    ZfAgentTrans insert(ZfAgentTrans zfAgentTrans);

    /**
     * 修改数据
     *
     * @param zfAgentTrans 实例对象
     * @return 实例对象
     */
    ZfAgentTrans update(ZfAgentTrans zfAgentTrans);

    /**
     * 通过主键删除数据
     *
     * @param agentId 主键
     * @return 是否成功
     */
    boolean deleteById(Integer agentId);

    ZfAgentTrans queryAddTransByOrderNo(String merchantOrderNo);

    /**
     * 查询减分流水
     * @param merchantOrderNo
     * @return
     */
    ZfAgentTrans queryAddTransBySub(String merchantOrderNo);


}
