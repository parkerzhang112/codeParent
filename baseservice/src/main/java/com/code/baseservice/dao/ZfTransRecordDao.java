package com.code.baseservice.dao;

import com.code.baseservice.entity.ZfTransRecord;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;

import java.util.List;


/**
 * (ZfTransRecord)表数据库访问层
 *
 * @author makejava
 * @since 2023-03-19 23:08:34
 */
public interface ZfTransRecordDao {

    /**
     * 通过ID查询单条数据
     *
     * @param transId 主键
     * @return 实例对象
     */
    ZfTransRecord queryById(Integer transId);

    /**
     * 查询指定行数据
     *
     * @param zfTransRecord 查询条件
     * @param pageable         分页对象
     * @return 对象列表
     */
    List<ZfTransRecord> queryAllByLimit(ZfTransRecord zfTransRecord, @Param("pageable") Pageable pageable);

    /**
     * 统计总行数
     *
     * @param zfTransRecord 查询条件
     * @return 总行数
     */
    long count(ZfTransRecord zfTransRecord);

    /**
     * 新增数据
     *
     * @param zfTransRecord 实例对象
     * @return 影响行数
     */
    int insert(ZfTransRecord zfTransRecord);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<ZfTransRecord> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<ZfTransRecord> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<ZfTransRecord> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<ZfTransRecord> entities);

    /**
     * 修改数据
     *
     * @param zfTransRecord 实例对象
     * @return 影响行数
     */
    int update(ZfTransRecord zfTransRecord);

    /**
     * 通过主键删除数据
     *
     * @param transId 主键
     * @return 影响行数
     */
    int deleteById(Integer transId);

}

