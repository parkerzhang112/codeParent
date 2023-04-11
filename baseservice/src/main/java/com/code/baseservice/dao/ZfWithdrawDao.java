package com.code.baseservice.dao;

import com.code.baseservice.entity.ZfWithdraw;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;

import java.util.List;
/**
 * 提款订单表(ZfWithdraw)表数据库访问层
 *
 * @author makejava
 * @since 2023-03-19 23:08:41
 */
public interface ZfWithdrawDao {

    /**
     * 通过ID查询单条数据
     *
     * @param orderNo 主键
     * @return 实例对象
     */
    ZfWithdraw queryById(@Param("orderNo") String orderNo);

    /**
     * 查询指定行数据
     *
     * @param zfWithdraw 查询条件
     * @param pageable         分页对象
     * @return 对象列表
     */
    List<ZfWithdraw> queryAllByLimit(ZfWithdraw zfWithdraw, @Param("pageable") Pageable pageable);

    /**
     * 统计总行数
     *
     * @param zfWithdraw 查询条件
     * @return 总行数
     */
    long count(ZfWithdraw zfWithdraw);

    /**
     * 新增数据
     *
     * @param zfWithdraw 实例对象
     * @return 影响行数
     */
    int insert(ZfWithdraw zfWithdraw);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<ZfWithdraw> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<ZfWithdraw> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<ZfWithdraw> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<ZfWithdraw> entities);

    /**
     * 修改数据
     *
     * @param zfWithdraw 实例对象
     * @return 影响行数
     */
    int update(ZfWithdraw zfWithdraw);

    /**
     * 通过主键删除数据
     *
     * @param orderNo 主键
     * @return 影响行数
     */
    int deleteById(String orderNo);

    ZfWithdraw queryByParams(@Param("merchantOrderNo") String merchant_order_no, @Param("merchantId") Integer merchant_id);

    void updatePaidOrder(ZfWithdraw zfWithdraw);
}

