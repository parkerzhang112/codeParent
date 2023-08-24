package com.code.baseservice.dao;

import com.code.baseservice.entity.ZfAgentRechargeOrder;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * (ZfAgentRechargeOrder)表数据库访问层
 *
 * @author makejava
 * @since 2023-08-21 23:23:46
 */
public interface ZfAgentRechargeOrderDao {

    /**
     * 通过ID查询单条数据
     *
     * @param orderNo 主键
     * @return 实例对象
     */
    ZfAgentRechargeOrder queryById(String orderNo);

    /**
     * 查询指定行数据
     *
     * @param zfAgentRechargeOrder 查询条件
     * @param pageable         分页对象
     * @return 对象列表
     */
    List<ZfAgentRechargeOrder> queryAllByLimit(ZfAgentRechargeOrder zfAgentRechargeOrder, @Param("pageable") Pageable pageable);

    /**
     * 统计总行数
     *
     * @param zfAgentRechargeOrder 查询条件
     * @return 总行数
     */
    long count(ZfAgentRechargeOrder zfAgentRechargeOrder);

    /**
     * 新增数据
     *
     * @param zfAgentRechargeOrder 实例对象
     * @return 影响行数
     */
    int insert(ZfAgentRechargeOrder zfAgentRechargeOrder);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<ZfAgentRechargeOrder> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<ZfAgentRechargeOrder> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<ZfAgentRechargeOrder> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<ZfAgentRechargeOrder> entities);

    /**
     * 修改数据
     *
     * @param zfAgentRechargeOrder 实例对象
     * @return 影响行数
     */
    int update(ZfAgentRechargeOrder zfAgentRechargeOrder);

    /**
     * 通过主键删除数据
     *
     * @param orderNo 主键
     * @return 影响行数
     */
    int deleteById(String orderNo);

}

