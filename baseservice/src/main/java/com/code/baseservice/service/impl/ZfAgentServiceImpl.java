package .service.impl;

import .entity.ZfAgent;
import .dao.ZfAgentDao;
import .service.ZfAgentService;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import javax.annotation.Resource;

/**
 * (ZfAgent)表服务实现类
 *
 * @author makejava
 * @since 2023-03-19 22:49:50
 */
@Service("zfAgentService")
public class ZfAgentServiceImpl implements ZfAgentService {
    @Resource
    private ZfAgentDao zfAgentDao;

    /**
     * 通过ID查询单条数据
     *
     * @param agentId 主键
     * @return 实例对象
     */
    @Override
    public ZfAgent queryById(Integer agentId) {
        return this.zfAgentDao.queryById(agentId);
    }

    /**
     * 分页查询
     *
     * @param zfAgent 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    @Override
    public Page<ZfAgent> queryByPage(ZfAgent zfAgent, PageRequest pageRequest) {
        long total = this.zfAgentDao.count(zfAgent);
        return new PageImpl<>(this.zfAgentDao.queryAllByLimit(zfAgent, pageRequest), pageRequest, total);
    }

    /**
     * 新增数据
     *
     * @param zfAgent 实例对象
     * @return 实例对象
     */
    @Override
    public ZfAgent insert(ZfAgent zfAgent) {
        this.zfAgentDao.insert(zfAgent);
        return zfAgent;
    }

    /**
     * 修改数据
     *
     * @param zfAgent 实例对象
     * @return 实例对象
     */
    @Override
    public ZfAgent update(ZfAgent zfAgent) {
        this.zfAgentDao.update(zfAgent);
        return this.queryById(zfAgent.getAgentId());
    }

    /**
     * 通过主键删除数据
     *
     * @param agentId 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer agentId) {
        return this.zfAgentDao.deleteById(agentId) > 0;
    }
}
