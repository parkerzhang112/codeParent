package com.code.baseservice.service;

import com.code.baseservice.entity.ZfChannelCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

/**
 * (ZfChannelCode)表服务接口
 *
 * @author makejava
 * @since 2023-03-19 23:07:07
 */
public interface ZfChannelCodeService {

    /**
     * 通过ID查询单条数据
     *
     * @param channeId 主键
     * @return 实例对象
     */
    ZfChannelCode queryById(Integer channeId);

    /**
     * 分页查询
     *
     * @param zfChannelCode 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    Page<ZfChannelCode> queryByPage(ZfChannelCode zfChannelCode, PageRequest pageRequest);

    /**
     * 新增数据
     *
     * @param zfChannelCode 实例对象
     * @return 实例对象
     */
    ZfChannelCode insert(ZfChannelCode zfChannelCode);

    /**
     * 修改数据
     *
     * @param zfChannelCode 实例对象
     * @return 实例对象
     */
    ZfChannelCode update(ZfChannelCode zfChannelCode);

    /**
     * 通过主键删除数据
     *
     * @param channeId 主键
     * @return 是否成功
     */
    boolean deleteById(Integer channeId);

}
