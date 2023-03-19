package com.code.baseservice.service;

import com.code.baseservice.entity.ZfCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

/**
 * (ZfCode)表服务接口
 *
 * @author makejava
 * @since 2023-03-19 23:07:16
 */
public interface ZfCodeService {

    /**
     * 通过ID查询单条数据
     *
     * @param codeId 主键
     * @return 实例对象
     */
    ZfCode queryById(Integer codeId);

    /**
     * 分页查询
     *
     * @param zfCode 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    Page<ZfCode> queryByPage(ZfCode zfCode, PageRequest pageRequest);

    /**
     * 新增数据
     *
     * @param zfCode 实例对象
     * @return 实例对象
     */
    ZfCode insert(ZfCode zfCode);

    /**
     * 修改数据
     *
     * @param zfCode 实例对象
     * @return 实例对象
     */
    ZfCode update(ZfCode zfCode);

    /**
     * 通过主键删除数据
     *
     * @param codeId 主键
     * @return 是否成功
     */
    boolean deleteById(Integer codeId);

}
