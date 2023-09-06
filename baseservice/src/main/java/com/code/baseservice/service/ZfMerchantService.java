package com.code.baseservice.service;

import com.alibaba.fastjson.JSONObject;
import com.code.baseservice.dto.backapi.OperaBalanceParams;
import com.code.baseservice.dto.payapi.MerchantParams;
import com.code.baseservice.dto.payapi.QueryParams;
import com.code.baseservice.dto.payapi.TransferParams;
import com.code.baseservice.entity.ZfAgent;
import com.code.baseservice.entity.ZfMerchant;
import com.code.baseservice.entity.ZfRecharge;

import java.math.BigDecimal;

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


    ZfMerchant vaildMerchant(String merchant_id);

    void verifSign(QueryParams queryParams, ZfMerchant zfMerchant);

    void verifSign(TransferParams transferParams, ZfMerchant zfMerchant);

    void verifMerchantBalance(ZfMerchant xMerchant, TransferParams transParams);

    JSONObject query(MerchantParams merchantParams);

    void sumMerchantBalance(Integer merchantId, BigDecimal subtract);

    void warrnBalance(ZfMerchant xMerchant, ZfRecharge zfRecharge, ZfAgent zfAgent);

    void operatBalance(OperaBalanceParams operaBalanceParams);

    void issue(TransferParams transParams);
}
