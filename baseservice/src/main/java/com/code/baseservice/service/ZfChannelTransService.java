package com.code.baseservice.service;

import com.code.baseservice.entity.ZfChannelTrans;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

/**
 * (ZfChannelTrans)表服务接口
 *
 * @author makejava
 * @since 2023-09-03 10:45:19
 */
public interface ZfChannelTransService {

    /**
     * 通过ID查询单条数据
     *
     * @param channelId 主键
     * @return 实例对象
     */
    ZfChannelTrans queryById(Integer channelId);

    /**
     * 分页查询
     *
     * @param zfChannelTrans 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    Page<ZfChannelTrans> queryByPage(ZfChannelTrans zfChannelTrans, PageRequest pageRequest);

    /**
     * 新增数据
     *
     * @param zfChannelTrans 实例对象
     * @return 实例对象
     */
    ZfChannelTrans insert(ZfChannelTrans zfChannelTrans);

    /**
     * 修改数据
     *
     * @param zfChannelTrans 实例对象
     * @return 实例对象
     */
    ZfChannelTrans update(ZfChannelTrans zfChannelTrans);

    /**
     * 通过主键删除数据
     *
     * @param channelId 主键
     * @return 是否成功
     */
    boolean deleteById(Integer channelId);

}
