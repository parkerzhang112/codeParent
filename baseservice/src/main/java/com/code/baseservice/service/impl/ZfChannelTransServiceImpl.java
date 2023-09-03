package com.code.baseservice.service.impl;

import com.code.baseservice.entity.ZfChannelTrans;
import com.code.baseservice.dao.ZfChannelTransDao;
import com.code.baseservice.service.ZfChannelTransService;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import javax.annotation.Resource;

/**
 * (ZfChannelTrans)表服务实现类
 *
 * @author makejava
 * @since 2023-09-03 10:45:19
 */
@Service("zfChannelTransService")
public class ZfChannelTransServiceImpl implements ZfChannelTransService {
    @Resource
    private ZfChannelTransDao zfChannelTransDao;

    /**
     * 通过ID查询单条数据
     *
     * @param channelId 主键
     * @return 实例对象
     */
    @Override
    public ZfChannelTrans queryById(Integer channelId) {
        return this.zfChannelTransDao.queryById(channelId);
    }

    /**
     * 分页查询
     *
     * @param zfChannelTrans 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    @Override
    public Page<ZfChannelTrans> queryByPage(ZfChannelTrans zfChannelTrans, PageRequest pageRequest) {
        long total = this.zfChannelTransDao.count(zfChannelTrans);
        return new PageImpl<>(this.zfChannelTransDao.queryAllByLimit(zfChannelTrans, pageRequest), pageRequest, total);
    }

    /**
     * 新增数据
     *
     * @param zfChannelTrans 实例对象
     * @return 实例对象
     */
    @Override
    public ZfChannelTrans insert(ZfChannelTrans zfChannelTrans) {
        this.zfChannelTransDao.insert(zfChannelTrans);
        return zfChannelTrans;
    }

    /**
     * 修改数据
     *
     * @param zfChannelTrans 实例对象
     * @return 实例对象
     */
    @Override
    public ZfChannelTrans update(ZfChannelTrans zfChannelTrans) {
        this.zfChannelTransDao.update(zfChannelTrans);
        return this.queryById(zfChannelTrans.getChannelId());
    }

    /**
     * 通过主键删除数据
     *
     * @param channelId 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer channelId) {
        return this.zfChannelTransDao.deleteById(channelId) > 0;
    }
}
