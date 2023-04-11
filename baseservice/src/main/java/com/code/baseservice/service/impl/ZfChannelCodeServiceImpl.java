package com.code.baseservice.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import com.code.baseservice .entity.ZfChannelCode;
import com.code.baseservice.dao.ZfChannelCodeDao;
import com.code.baseservice.service.ZfChannelCodeService;

/**
 * (ZfChannelCode)表服务实现类
 *
 * @author makejava
 * @since 2023-03-19 23:07:07
 */
@Service("zfChannelCodeService")
public class ZfChannelCodeServiceImpl implements ZfChannelCodeService {
    @Resource
    private ZfChannelCodeDao zfChannelCodeDao;

    /**
     * 通过ID查询单条数据
     *
     * @param channeId 主键
     * @return 实例对象
     */
    @Override
    public ZfChannelCode queryById(Integer channeId) {
        return this.zfChannelCodeDao.queryById(channeId);
    }

    /**
     * 分页查询
     *
     * @param zfChannelCode 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    @Override
    public Page<ZfChannelCode> queryByPage(ZfChannelCode zfChannelCode, PageRequest pageRequest) {
        long total = this.zfChannelCodeDao.count(zfChannelCode);
        return new PageImpl<>(this.zfChannelCodeDao.queryAllByLimit(zfChannelCode, pageRequest), pageRequest, total);
    }

    /**
     * 新增数据
     *
     * @param zfChannelCode 实例对象
     * @return 实例对象
     */
    @Override
    public ZfChannelCode insert(ZfChannelCode zfChannelCode) {
        this.zfChannelCodeDao.insert(zfChannelCode);
        return zfChannelCode;
    }

    /**
     * 修改数据
     *
     * @param zfChannelCode 实例对象
     * @return 实例对象
     */
    @Override
    public ZfChannelCode update(ZfChannelCode zfChannelCode) {
        this.zfChannelCodeDao.update(zfChannelCode);
        return this.queryById(zfChannelCode.getChanneId());
    }

    /**
     * 通过主键删除数据
     *
     * @param channeId 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer channeId) {
        return this.zfChannelCodeDao.deleteById(channeId) > 0;
    }
}
