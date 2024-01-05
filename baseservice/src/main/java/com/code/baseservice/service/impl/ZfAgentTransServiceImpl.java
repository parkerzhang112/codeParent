package com.code.baseservice.service.impl;

import com.code.baseservice.dao.ZfAgentTransDao;
import com.code.baseservice.entity.ZfAgentTrans;
import com.code.baseservice.service.ZfAgentTransService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * (ZfAgentTrans)表服务实现类
 *
 * @author makejava
 * @since 2023-03-19 23:06:24
 */
@Service("zfAgentTransService")
public class ZfAgentTransServiceImpl implements ZfAgentTransService {
    @Resource
    private ZfAgentTransDao zfAgentTransDao;

    /**
     * 通过ID查询单条数据
     *
     * @param agentId 主键
     * @return 实例对象
     */
    @Override
    public ZfAgentTrans queryById(Integer agentId) {
        return this.zfAgentTransDao.queryById(agentId);
    }

    /**
     * 分页查询
     *
     * @param zfAgentTrans 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    @Override
    public Page<ZfAgentTrans> queryByPage(ZfAgentTrans zfAgentTrans, PageRequest pageRequest) {
        long total = this.zfAgentTransDao.count(zfAgentTrans);
        return new PageImpl<>(this.zfAgentTransDao.queryAllByLimit(zfAgentTrans, pageRequest), pageRequest, total);
    }

    /**
     * 新增数据
     *
     * @param zfAgentTrans 实例对象
     * @return 实例对象
     */
    @Override
    public ZfAgentTrans insert(ZfAgentTrans zfAgentTrans) {
        this.zfAgentTransDao.insert(zfAgentTrans);
        return zfAgentTrans;
    }

    /**
     * 修改数据
     *
     * @param zfAgentTrans 实例对象
     * @return 实例对象
     */
    @Override
    public ZfAgentTrans update(ZfAgentTrans zfAgentTrans) {
        this.zfAgentTransDao.update(zfAgentTrans);
        return this.queryById(zfAgentTrans.getAgentId());
    }

    /**
     * 通过主键删除数据
     *
     * @param agentId 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer agentId) {
        return this.zfAgentTransDao.deleteById(agentId) > 0;
    }

    @Override
    public ZfAgentTrans queryAddTransByOrderNo(String merchantOrderNo) {
        return this.zfAgentTransDao.queryAddTransByOrderNo(merchantOrderNo);
    }

    @Override
    public ZfAgentTrans queryAddTransBySub(String merchantOrderNo) {
        return this.zfAgentTransDao.queryAddTransBySub(merchantOrderNo);
    }

    @Override
    public List<ZfAgentTrans> queryAddTransBySubNear(String merchantOrderNo) {
        return this.zfAgentTransDao.queryAddTransBySubNear(merchantOrderNo);
    }
}
