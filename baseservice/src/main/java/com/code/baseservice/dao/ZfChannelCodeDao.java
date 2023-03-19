package com.code.baseservice.dao;

import com.code.baseservice.entity.ZfChannelCode;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * (ZfChannelCode)表数据库访问层
 *
 * @author makejava
 * @since 2023-03-19 23:07:05
 */
public interface ZfChannelCodeDao {

    /**
     * 通过ID查询单条数据
     *
     * @param channeId 主键
     * @return 实例对象
     */
    ZfChannelCode queryById(Integer channeId);

    /**
     * 查询指定行数据
     *
     * @param zfChannelCode 查询条件
     * @param pageable         分页对象
     * @return 对象列表
     */
    List<ZfChannelCode> queryAllByLimit(ZfChannelCode zfChannelCode, @Param("pageable") Pageable pageable);

    /**
     * 统计总行数
     *
     * @param zfChannelCode 查询条件
     * @return 总行数
     */
    long count(ZfChannelCode zfChannelCode);

    /**
     * 新增数据
     *
     * @param zfChannelCode 实例对象
     * @return 影响行数
     */
    int insert(ZfChannelCode zfChannelCode);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<ZfChannelCode> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<ZfChannelCode> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<ZfChannelCode> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<ZfChannelCode> entities);

    /**
     * 修改数据
     *
     * @param zfChannelCode 实例对象
     * @return 影响行数
     */
    int update(ZfChannelCode zfChannelCode);

    /**
     * 通过主键删除数据
     *
     * @param channeId 主键
     * @return 影响行数
     */
    int deleteById(Integer channeId);

}

