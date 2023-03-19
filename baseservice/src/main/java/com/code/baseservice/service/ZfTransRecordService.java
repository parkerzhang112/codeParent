package com.code.baseservice.service;

import com.code.baseservice.entity.ZfTransRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

/**
 * (ZfTransRecord)表服务接口
 *
 * @author makejava
 * @since 2023-03-19 23:08:34
 */
public interface ZfTransRecordService {

    /**
     * 通过ID查询单条数据
     *
     * @param transId 主键
     * @return 实例对象
     */
    ZfTransRecord queryById(Integer transId);

    /**
     * 分页查询
     *
     * @param zfTransRecord 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    Page<ZfTransRecord> queryByPage(ZfTransRecord zfTransRecord, PageRequest pageRequest);

    /**
     * 新增数据
     *
     * @param zfTransRecord 实例对象
     * @return 实例对象
     */
    ZfTransRecord insert(ZfTransRecord zfTransRecord);

    /**
     * 修改数据
     *
     * @param zfTransRecord 实例对象
     * @return 实例对象
     */
    ZfTransRecord update(ZfTransRecord zfTransRecord);

    /**
     * 通过主键删除数据
     *
     * @param transId 主键
     * @return 是否成功
     */
    boolean deleteById(Integer transId);

}
