package com.code.baseservice.dao;

import com.code.baseservice.entity.ZfChannel;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;

import java.util.List;


/**
 * (ZfChannel)表数据库访问层
 *
 * @author makejava
 * @since 2023-03-19 23:06:49
 */
public interface ZfChannelDao {

    /**
     * 通过ID查询单条数据
     *
     * @param channelId 主键
     * @return 实例对象
     */
    ZfChannel queryById(Integer channelId);

    /**
     * 查询指定行数据
     *
     * @param zfChannel 查询条件
     * @param pageable         分页对象
     * @return 对象列表
     */
    List<ZfChannel> queryAllByLimit(ZfChannel zfChannel, @Param("pageable") Pageable pageable);

    /**
     * 统计总行数
     *
     * @param zfChannel 查询条件
     * @return 总行数
     */
    long count(ZfChannel zfChannel);

    /**
     * 新增数据
     *
     * @param zfChannel 实例对象
     * @return 影响行数
     */
    int insert(ZfChannel zfChannel);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<ZfChannel> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<ZfChannel> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<ZfChannel> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<ZfChannel> entities);

    /**
     * 修改数据
     *
     * @param zfChannel 实例对象
     * @return 影响行数
     */
    int update(ZfChannel zfChannel);

    /**
     * 通过主键删除数据
     *
     * @param channelId 主键
     * @return 影响行数
     */
    int deleteById(Integer channelId);

}

