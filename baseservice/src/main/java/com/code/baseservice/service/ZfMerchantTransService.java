package com.code.baseservice.service;

import com.code.baseservice.entity.ZfMerchantTrans;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;


/**
 * (ZfMerchantTrans)表服务接口
 *
 * @author makejava
 * @since 2023-03-19 23:08:17
 */
public interface ZfMerchantTransService {

    /**
     * 通过ID查询单条数据
     *
     * @param merchantId 主键
     * @return 实例对象
     */
    ZfMerchantTrans queryById(Integer merchantId);

    /**
     * 分页查询
     *
     * @param zfMerchantTrans 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    Page<ZfMerchantTrans> queryByPage(ZfMerchantTrans zfMerchantTrans, PageRequest pageRequest);

    /**
     * 新增数据
     *
     * @param zfMerchantTrans 实例对象
     * @return 实例对象
     */
    ZfMerchantTrans insert(ZfMerchantTrans zfMerchantTrans);

    /**
     * 修改数据
     *
     * @param zfMerchantTrans 实例对象
     * @return 实例对象
     */
    ZfMerchantTrans update(ZfMerchantTrans zfMerchantTrans);

    /**
     * 通过主键删除数据
     *
     * @param merchantId 主键
     * @return 是否成功
     */
    boolean deleteById(Integer merchantId);

}
