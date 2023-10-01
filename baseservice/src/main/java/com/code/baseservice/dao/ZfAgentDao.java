package com.code.baseservice.dao;

import com.code.baseservice.entity.ZfAgent;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * (ZfAgent)表数据库访问层
 *
 * @author makejava
 * @since 2023-03-19 22:49:35
 */
public interface ZfAgentDao {

    /**
     * 通过ID查询单条数据
     *
     * @param agentId 主键
     * @return 实例对象
     */
    ZfAgent queryById(Integer agentId);

    /**
     * 查询指定行数据
     *
     * @param zfAgent 查询条件
     * @param pageable         分页对象
     * @return 对象列表
     */
    List<ZfAgent> queryAllByLimit(ZfAgent zfAgent, @Param("pageable") Pageable pageable);

    /**
     * 统计总行数
     *
     * @param zfAgent 查询条件
     * @return 总行数
     */
    long count(ZfAgent zfAgent);

    /**
     * 新增数据
     *
     * @param zfAgent 实例对象
     * @return 影响行数
     */
    int insert(ZfAgent zfAgent);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<ZfAgent> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<ZfAgent> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<ZfAgent> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<ZfAgent> entities);

    /**
     * 修改数据
     *
     * @param zfAgent 实例对象
     * @return 影响行数
     */
    int update(ZfAgent zfAgent);

    /**
     * 通过主键删除数据
     *
     * @param agentId 主键
     * @return 影响行数
     */
    int deleteById(Integer agentId);

    int updateAgentFee(ZfAgent zfAgent);

    ZfAgent queryByCode(@Param("agentCode") String agentCode);
}

