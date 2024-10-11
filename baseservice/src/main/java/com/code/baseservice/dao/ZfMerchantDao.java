package com.code.baseservice.dao;

import com.code.baseservice.entity.ZfMerchant;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;



/**
 * (ZfMerchant)表数据库访问层
 *
 * @author makejava
 * @since 2023-03-19 23:07:55
 */
public interface ZfMerchantDao {

    /**
     * 通过ID查询单条数据
     *
     * @param merchantId 主键
     * @return 实例对象
     */
    ZfMerchant queryById(Integer merchantId);

    /**
     * 查询指定行数据
     *
     * @param zfMerchant 查询条件
     * @param pageable         分页对象
     * @return 对象列表
     */
    List<ZfMerchant> queryAllByLimit(ZfMerchant zfMerchant, @Param("pageable") Pageable pageable);

    /**
     * 统计总行数
     *
     * @param zfMerchant 查询条件
     * @return 总行数
     */
    long count(ZfMerchant zfMerchant);

    /**
     * 新增数据
     *
     * @param zfMerchant 实例对象
     * @return 影响行数
     */
    int insert(ZfMerchant zfMerchant);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<ZfMerchant> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<ZfMerchant> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<ZfMerchant> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<ZfMerchant> entities);

    /**
     * 修改数据
     *
     * @param zfMerchant 实例对象
     * @return 影响行数
     */
    int update(ZfMerchant zfMerchant);

    /**
     * 通过主键删除数据
     *
     * @param merchantId 主键
     * @return 影响行数
     */
    int deleteById(Integer merchantId);

    void sumMerchantBalance(@Param("merchantId") Integer merchantId, @Param("balance") BigDecimal subtract);

    ZfMerchant queryByName(@Param("merchantName") String merchantName);
}

