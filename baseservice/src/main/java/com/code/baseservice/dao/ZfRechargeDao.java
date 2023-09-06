package com.code.baseservice.dao;

import com.code.baseservice.dto.payapi.QueryParams;
import com.code.baseservice.entity.ZfRecharge;
import com.code.baseservice.entity.ZfTransRecord;
import org.apache.ibatis.annotations.Param;

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
    ZfRecharge queryById(@Param("orderNo") String orderNo);

    /**
     * 通过ID查询单条数据
     *
     * @param merchantOrderNo 主键
     * @return 实例对象
     */
    ZfRecharge queryByMerchantOrderNo(@Param("merchantOrderNo") String merchantOrderNo);

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


    ZfRecharge queryByParam(QueryParams queryParams);

    List<ZfRecharge> tryFindOrderByTrans(ZfTransRecord zfTransRecord);

    void toNotifySuccess(ZfRecharge zfRecharge);

    void toNotifyException(ZfRecharge zfRecharge);

    List<ZfRecharge> queryAllByLimit(@Param("orderStatus")int status,@Param("limit") int limit, @Param("offset") int offset);

    ZfRecharge queryByOrderNo(@Param("orderNo") String order_no);
}

