package com.code.baseservice.dao;

import com.code.baseservice.entity.ZfChannelTrans;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * (ZfChannelTrans)表数据库访问层
 *
 * @author makejava
 * @since 2023-09-03 10:45:19
 */
public interface ZfChannelTransDao {

    /**
     * 通过ID查询单条数据
     *
     * @param channelId 主键
     * @return 实例对象
     */
    ZfChannelTrans queryById(Integer channelId);

    /**
     * 查询指定行数据
     *
     * @param zfChannelTrans 查询条件
     * @param pageable         分页对象
     * @return 对象列表
     */
    List<ZfChannelTrans> queryAllByLimit(ZfChannelTrans zfChannelTrans, @Param("pageable") Pageable pageable);

    /**
     * 统计总行数
     *
     * @param zfChannelTrans 查询条件
     * @return 总行数
     */
    long count(ZfChannelTrans zfChannelTrans);

    /**
     * 新增数据
     *
     * @param zfChannelTrans 实例对象
     * @return 影响行数
     */
    int insert(ZfChannelTrans zfChannelTrans);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<ZfChannelTrans> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<ZfChannelTrans> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<ZfChannelTrans> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<ZfChannelTrans> entities);

    /**
     * 修改数据
     *
     * @param zfChannelTrans 实例对象
     * @return 影响行数
     */
    int update(ZfChannelTrans zfChannelTrans);

    /**
     * 通过主键删除数据
     *
     * @param channelId 主键
     * @return 影响行数
     */
    int deleteById(Integer channelId);

}

