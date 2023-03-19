package com.code.baseservice.service.impl;

import com.code.baseservice.dao.ZfCodeDao;
import com.code.baseservice.entity.ZfCode;
import com.code.baseservice.service.ZfCodeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * (ZfCode)表服务实现类
 *
 * @author makejava
 * @since 2023-03-19 23:07:16
 */
@Service("zfCodeService")
public class ZfCodeServiceImpl implements ZfCodeService {
    @Resource
    private ZfCodeDao zfCodeDao;

    /**
     * 通过ID查询单条数据
     *
     * @param codeId 主键
     * @return 实例对象
     */
    @Override
    public ZfCode queryById(Integer codeId) {
        return this.zfCodeDao.queryById(codeId);
    }

    /**
     * 分页查询
     *
     * @param zfCode 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    @Override
    public Page<ZfCode> queryByPage(ZfCode zfCode, PageRequest pageRequest) {
        long total = this.zfCodeDao.count(zfCode);
        return new PageImpl<>(this.zfCodeDao.queryAllByLimit(zfCode, pageRequest), pageRequest, total);
    }

    /**
     * 新增数据
     *
     * @param zfCode 实例对象
     * @return 实例对象
     */
    @Override
    public ZfCode insert(ZfCode zfCode) {
        this.zfCodeDao.insert(zfCode);
        return zfCode;
    }

    /**
     * 修改数据
     *
     * @param zfCode 实例对象
     * @return 实例对象
     */
    @Override
    public ZfCode update(ZfCode zfCode) {
        this.zfCodeDao.update(zfCode);
        return this.queryById(zfCode.getCodeId());
    }

    /**
     * 通过主键删除数据
     *
     * @param codeId 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer codeId) {
        return this.zfCodeDao.deleteById(codeId) > 0;
    }
}
