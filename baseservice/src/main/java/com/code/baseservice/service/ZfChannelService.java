package com.code.baseservice.service;

import com.code.baseservice.entity.ZfChannel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;


/**
 * (ZfChannel)表服务接口
 *
 * @author makejava
 * @since 2023-03-19 23:06:52
 */
public interface ZfChannelService {

    /**
     * 通过ID查询单条数据
     *
     * @param channelId 主键
     * @return 实例对象
     */
    ZfChannel queryById(Integer channelId);

    /**
     * 分页查询
     *
     * @param zfChannel 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    Page<ZfChannel> queryByPage(ZfChannel zfChannel, PageRequest pageRequest);

    /**
     * 新增数据
     *
     * @param zfChannel 实例对象
     * @return 实例对象
     */
    ZfChannel insert(ZfChannel zfChannel);

    /**
     * 修改数据
     *
     * @param zfChannel 实例对象
     * @return 实例对象
     */
    ZfChannel update(ZfChannel zfChannel);

    /**
     * 通过主键删除数据
     *
     * @param channelId 主键
     * @return 是否成功
     */
    boolean deleteById(Integer channelId);

}
