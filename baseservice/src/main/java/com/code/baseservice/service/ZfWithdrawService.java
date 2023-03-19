package com.code.baseservice.service;

import com.code.baseservice.entity.ZfWithdraw;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

/**
 * 提款订单表(ZfWithdraw)表服务接口
 *
 * @author makejava
 * @since 2023-03-19 23:08:41
 */
public interface ZfWithdrawService {

    /**
     * 通过ID查询单条数据
     *
     * @param orderNo 主键
     * @return 实例对象
     */
    ZfWithdraw queryById(String orderNo);

    /**
     * 分页查询
     *
     * @param zfWithdraw 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    Page<ZfWithdraw> queryByPage(ZfWithdraw zfWithdraw, PageRequest pageRequest);

    /**
     * 新增数据
     *
     * @param zfWithdraw 实例对象
     * @return 实例对象
     */
    ZfWithdraw insert(ZfWithdraw zfWithdraw);

    /**
     * 修改数据
     *
     * @param zfWithdraw 实例对象
     * @return 实例对象
     */
    ZfWithdraw update(ZfWithdraw zfWithdraw);

    /**
     * 通过主键删除数据
     *
     * @param orderNo 主键
     * @return 是否成功
     */
    boolean deleteById(String orderNo);

}
