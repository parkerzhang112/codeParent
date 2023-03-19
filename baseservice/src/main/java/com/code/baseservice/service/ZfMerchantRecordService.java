package com.code.baseservice.service;

import com.code.baseservice.entity.ZfMerchantRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

/**
 * (ZfMerchantRecord)表服务接口
 *
 * @author makejava
 * @since 2023-03-19 23:08:08
 */
public interface ZfMerchantRecordService {

    /**
     * 通过ID查询单条数据
     *
     * @param  主键
     * @return 实例对象
     */
    ZfMerchantRecord queryById();

    /**
     * 分页查询
     *
     * @param zfMerchantRecord 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    Page<ZfMerchantRecord> queryByPage(ZfMerchantRecord zfMerchantRecord, PageRequest pageRequest);

    /**
     * 新增数据
     *
     * @param zfMerchantRecord 实例对象
     * @return 实例对象
     */
    ZfMerchantRecord insert(ZfMerchantRecord zfMerchantRecord);

    /**
     * 修改数据
     *
     * @param zfMerchantRecord 实例对象
     * @return 实例对象
     */
    ZfMerchantRecord update(ZfMerchantRecord zfMerchantRecord);

    /**
     * 通过主键删除数据
     *
     * @param  主键
     * @return 是否成功
     */
    boolean deleteById();

}
