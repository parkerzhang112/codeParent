package com.code.baseservice.service;

import com.code.baseservice.entity.ZfCodeRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

/**
 * (ZfCodeRecord)表服务接口
 *
 * @author makejava
 * @since 2023-03-19 23:07:26
 */
public interface ZfCodeRecordService {

    /**
     * 通过ID查询单条数据
     *
     * @param codeId 主键
     * @return 实例对象
     */
    ZfCodeRecord queryById(Integer codeId);

    /**
     * 分页查询
     *
     * @param zfCodeRecord 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    Page<ZfCodeRecord> queryByPage(ZfCodeRecord zfCodeRecord, PageRequest pageRequest);

    /**
     * 新增数据
     *
     * @param zfCodeRecord 实例对象
     * @return 实例对象
     */
    ZfCodeRecord insert(ZfCodeRecord zfCodeRecord);

    /**
     * 修改数据
     *
     * @param zfCodeRecord 实例对象
     * @return 实例对象
     */
    ZfCodeRecord update(ZfCodeRecord zfCodeRecord);

    /**
     * 通过主键删除数据
     *
     * @param codeId 主键
     * @return 是否成功
     */
    boolean deleteById(Integer codeId);

}
