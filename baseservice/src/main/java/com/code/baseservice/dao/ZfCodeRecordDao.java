package com.code.baseservice.dao;

import com.code.baseservice.entity.ZfCodeRecord;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * (ZfCodeRecord)表数据库访问层
 *
 * @author makejava
 * @since 2023-03-19 23:07:26
 */
public interface ZfCodeRecordDao {

    /**
     * 通过ID查询单条数据
     *
     * @param codeId 主键
     * @return 实例对象
     */
    ZfCodeRecord queryById(Integer codeId);

    /**
     * 查询指定行数据
     *
     * @param zfCodeRecord 查询条件
     * @param pageable         分页对象
     * @return 对象列表
     */
    List<ZfCodeRecord> queryAllByLimit(ZfCodeRecord zfCodeRecord, @Param("pageable") Pageable pageable);

    /**
     * 统计总行数
     *
     * @param zfCodeRecord 查询条件
     * @return 总行数
     */
    long count(ZfCodeRecord zfCodeRecord);

    /**
     * 新增数据
     *
     * @param zfCodeRecord 实例对象
     * @return 影响行数
     */
    int insert(ZfCodeRecord zfCodeRecord);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<ZfCodeRecord> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<ZfCodeRecord> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<ZfCodeRecord> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<ZfCodeRecord> entities);

    /**
     * 修改数据
     *
     * @param zfCodeRecord 实例对象
     * @return 影响行数
     */
    int update(ZfCodeRecord zfCodeRecord);

    /**
     * 通过主键删除数据
     *
     * @param codeId 主键
     * @return 影响行数
     */
    int deleteById(Integer codeId);

}

