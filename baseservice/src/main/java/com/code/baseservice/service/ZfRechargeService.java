package com.code.baseservice.service;

import com.code.baseservice.entity.ZfRecharge;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

/**
 * (ZfRecharge)表服务接口
 *
 * @author makejava
 * @since 2023-03-19 23:08:26
 */
public interface ZfRechargeService {

    /**
     * 通过ID查询单条数据
     *
     * @param merchantOrderNo 主键
     * @return 实例对象
     */
    ZfRecharge queryById(String merchantOrderNo);

    /**
     * 分页查询
     *
     * @param zfRecharge 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    Page<ZfRecharge> queryByPage(ZfRecharge zfRecharge, PageRequest pageRequest);

    /**
     * 新增数据
     *
     * @param zfRecharge 实例对象
     * @return 实例对象
     */
    ZfRecharge insert(ZfRecharge zfRecharge);

    /**
     * 修改数据
     *
     * @param zfRecharge 实例对象
     * @return 实例对象
     */
    ZfRecharge update(ZfRecharge zfRecharge);

    /**
     * 通过主键删除数据
     *
     * @param merchantOrderNo 主键
     * @return 是否成功
     */
    boolean deleteById(String merchantOrderNo);

}
