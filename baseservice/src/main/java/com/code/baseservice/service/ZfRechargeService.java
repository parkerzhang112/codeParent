package com.code.baseservice.service;

import com.alibaba.fastjson.JSONObject;
import com.code.baseservice.dto.autoapi.TransParams;
import com.code.baseservice.dto.backapi.OperaOrderParams;
import com.code.baseservice.dto.payapi.QueryParams;
import com.code.baseservice.dto.payapi.RechareParams;
import com.code.baseservice.entity.ZfRecharge;
import com.code.baseservice.entity.ZfTransRecord;
import com.code.baseservice.entity.ZfWithdraw;
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

    JSONObject create(RechareParams rechareParams);

    JSONObject query(QueryParams queryParams);

    ZfRecharge tryFindOrderByTrans(ZfTransRecord zfTransRecord);

    void paidOrder(ZfRecharge zfRecharge);

    void confirmOrder(OperaOrderParams operaOrderParams);

    void cancelOrder(OperaOrderParams operaOrderParams);

    public void notify(ZfRecharge zfRecharge);

    JSONObject getOrderStatus(String  orderno);
}
