package com.code.baseservice.dao;

import com.code.baseservice.entity.ZfCode;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * (ZfCode)表数据库访问层
 *
 * @author makejava
 * @since 2023-03-19 23:07:15
 */
public interface ZfCodeDao {

    /**
     * 通过ID查询单条数据
     *
     * @param codeId 主键
     * @return 实例对象
     */
    ZfCode queryById(Integer codeId);

    /**
     * 查询指定行数据
     *
     * @param zfCode 查询条件
     * @param pageable         分页对象
     * @return 对象列表
     */
    List<ZfCode> queryAllByLimit(ZfCode zfCode, @Param("pageable") Pageable pageable);

    /**
     * 统计总行数
     *
     * @param zfCode 查询条件
     * @return 总行数
     */
    long count(ZfCode zfCode);

    /**
     * 新增数据
     *
     * @param zfCode 实例对象
     * @return 影响行数
     */
    int insert(ZfCode zfCode);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<ZfCode> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<ZfCode> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<ZfCode> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<ZfCode> entities);

    /**
     * 修改数据
     *
     * @param zfCode 实例对象
     * @return 影响行数
     */
    int update(ZfCode zfCode);

    /**
     * 通过主键删除数据
     *
     * @param codeId 主键
     * @return 影响行数
     */
    int deleteById(Integer codeId);

}

