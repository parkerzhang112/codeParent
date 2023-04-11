package com.code.baseservice.dao;

import com.code.baseservice.entity.ZfAgentRecord;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;

import java.util.List;


/**
 * (ZfAgentRecord)表数据库访问层
 *
 * @author makejava
 * @since 2023-03-19 23:06:09
 */
public interface ZfAgentRecordDao {

    /**
     * 通过ID查询单条数据
     *
     * @param agentId 主键
     * @return 实例对象
     */
    ZfAgentRecord queryById(Integer agentId);

    /**
     * 查询指定行数据
     *
     * @param zfAgentRecord 查询条件
     * @param pageable         分页对象
     * @return 对象列表
     */
    List<ZfAgentRecord> queryAllByLimit(ZfAgentRecord zfAgentRecord, @Param("pageable") Pageable pageable);

    /**
     * 统计总行数
     *
     * @param zfAgentRecord 查询条件
     * @return 总行数
     */
    long count(ZfAgentRecord zfAgentRecord);

    /**
     * 新增数据
     *
     * @param zfAgentRecord 实例对象
     * @return 影响行数
     */
    int insert(ZfAgentRecord zfAgentRecord);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<ZfAgentRecord> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<ZfAgentRecord> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<ZfAgentRecord> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<ZfAgentRecord> entities);

    /**
     * 修改数据
     *
     * @param zfAgentRecord 实例对象
     * @return 影响行数
     */
    int update(ZfAgentRecord zfAgentRecord);

    /**
     * 通过主键删除数据
     *
     * @param agentId 主键
     * @return 影响行数
     */
    int deleteById(Integer agentId);

    ZfAgentRecord queryByIdAndRecord(ZfAgentRecord zfAgentRecord);
}

