package com.code.baseservice.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

.service.impl;
        .entity.ZfChannel;
        .dao.ZfChannelDao;
        .service.ZfChannelService;

/**
 * (ZfChannel)表服务实现类
 *
 * @author makejava
 * @since 2023-03-19 23:06:53
 */
@Service("zfChannelService")
public class ZfChannelServiceImpl implements ZfChannelService {
    @Resource
    private ZfChannelDao zfChannelDao;

    /**
     * 通过ID查询单条数据
     *
     * @param channelId 主键
     * @return 实例对象
     */
    @Override
    public ZfChannel queryById(Integer channelId) {
        return this.zfChannelDao.queryById(channelId);
    }

    /**
     * 分页查询
     *
     * @param zfChannel 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    @Override
    public Page<ZfChannel> queryByPage(ZfChannel zfChannel, PageRequest pageRequest) {
        long total = this.zfChannelDao.count(zfChannel);
        return new PageImpl<>(this.zfChannelDao.queryAllByLimit(zfChannel, pageRequest), pageRequest, total);
    }

    /**
     * 新增数据
     *
     * @param zfChannel 实例对象
     * @return 实例对象
     */
    @Override
    public ZfChannel insert(ZfChannel zfChannel) {
        this.zfChannelDao.insert(zfChannel);
        return zfChannel;
    }

    /**
     * 修改数据
     *
     * @param zfChannel 实例对象
     * @return 实例对象
     */
    @Override
    public ZfChannel update(ZfChannel zfChannel) {
        this.zfChannelDao.update(zfChannel);
        return this.queryById(zfChannel.getChannelId());
    }

    /**
     * 通过主键删除数据
     *
     * @param channelId 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer channelId) {
        return this.zfChannelDao.deleteById(channelId) > 0;
    }
}
