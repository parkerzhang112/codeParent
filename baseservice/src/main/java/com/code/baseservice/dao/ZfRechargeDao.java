package com.code.baseservice.dao;

import com.code.baseservice.entity.ZfRecharge;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;

import java.util.List;


/**
 * (ZfRecharge)表数据库访问层
 *
 * @author makejava
 * @since 2023-03-19 23:08:26
 */
public interface ZfRechargeDao {

    /**
     * 通过ID查询单条数据
     *
     * @param merchantOrderNo 主键
     * @return 实例对象
     */
    ZfRecharge queryById(String merchantOrderNo);

    /**
     * 查询指定行数据
     *
     * @param zfRecharge 查询条件
     * @param pageable         分页对象
     * @return 对象列表
     */
    List<ZfRecharge> queryAllByLimit(ZfRecharge zfRecharge, @Param("pageable") Pageable pageable);

    /**
     * 统计总行数
     *
     * @param zfRecharge 查询条件
     * @return 总行数
     */
    long count(ZfRecharge zfRecharge);

    /**
     * 新增数据
     *
     * @param zfRecharge 实例对象
     * @return 影响行数
     */
    int insert(ZfRecharge zfRecharge);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<ZfRecharge> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<ZfRecharge> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<ZfRecharge> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<ZfRecharge> entities);

    /**
     * 修改数据
     *
     * @param zfRecharge 实例对象
     * @return 影响行数
     */
    int update(ZfRecharge zfRecharge);

    /**
     * 通过主键删除数据
     *
     * @param merchantOrderNo 主键
     * @return 影响行数
     */
    int deleteById(String merchantOrderNo);

}

