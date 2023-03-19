package com.code.baseservice.dao;

import com.code.baseservice.entity.ZfMerchantRecord;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;

import java.util.List;


/**
 * (ZfMerchantRecord)表数据库访问层
 *
 * @author makejava
 * @since 2023-03-19 23:08:08
 */
public interface ZfMerchantRecordDao {

    /**
     * 通过ID查询单条数据
     *
     * @param  主键
     * @return 实例对象
     */
    ZfMerchantRecord queryById();

    /**
     * 查询指定行数据
     *
     * @param zfMerchantRecord 查询条件
     * @param pageable         分页对象
     * @return 对象列表
     */
    List<ZfMerchantRecord> queryAllByLimit(ZfMerchantRecord zfMerchantRecord, @Param("pageable") Pageable pageable);

    /**
     * 统计总行数
     *
     * @param zfMerchantRecord 查询条件
     * @return 总行数
     */
    long count(ZfMerchantRecord zfMerchantRecord);

    /**
     * 新增数据
     *
     * @param zfMerchantRecord 实例对象
     * @return 影响行数
     */
    int insert(ZfMerchantRecord zfMerchantRecord);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<ZfMerchantRecord> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<ZfMerchantRecord> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<ZfMerchantRecord> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<ZfMerchantRecord> entities);

    /**
     * 修改数据
     *
     * @param zfMerchantRecord 实例对象
     * @return 影响行数
     */
    int update(ZfMerchantRecord zfMerchantRecord);

    /**
     * 通过主键删除数据
     *
     * @param  主键
     * @return 影响行数
     */
    int deleteById();

}

