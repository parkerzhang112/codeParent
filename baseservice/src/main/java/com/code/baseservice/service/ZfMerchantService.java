package com.code.baseservice.service;

import com.code.baseservice.entity.ZfMerchant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

/**
 * (ZfMerchant)表服务接口
 *
 * @author makejava
 * @since 2023-03-19 23:07:58
 */
public interface ZfMerchantService {

    /**
     * 通过ID查询单条数据
     *
     * @param merchantId 主键
     * @return 实例对象
     */
    ZfMerchant queryById(Integer merchantId);

    /**
     * 分页查询
     *
     * @param zfMerchant 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    Page<ZfMerchant> queryByPage(ZfMerchant zfMerchant, PageRequest pageRequest);

    /**
     * 新增数据
     *
     * @param zfMerchant 实例对象
     * @return 实例对象
     */
    ZfMerchant insert(ZfMerchant zfMerchant);

    /**
     * 修改数据
     *
     * @param zfMerchant 实例对象
     * @return 实例对象
     */
    ZfMerchant update(ZfMerchant zfMerchant);

    /**
     * 通过主键删除数据
     *
     * @param merchantId 主键
     * @return 是否成功
     */
    boolean deleteById(Integer merchantId);

}
