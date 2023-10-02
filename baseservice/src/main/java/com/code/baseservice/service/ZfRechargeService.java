package com.code.baseservice.service;

import com.alibaba.fastjson.JSONObject;
import com.code.baseservice.dto.backapi.OperaBalanceParams;
import com.code.baseservice.dto.backapi.OperaOrderParams;
import com.code.baseservice.dto.payapi.QueryParams;
import com.code.baseservice.dto.payapi.RechareParams;
import com.code.baseservice.entity.ZfCode;
import com.code.baseservice.entity.ZfRecharge;
import com.code.baseservice.entity.ZfTransRecord;

import java.util.List;
import java.util.Map;

/**
 * (ZfRecharge)表服务接口
 *
 * @author makejava
 * @since 2023-03-19 23:08:26
 */
public interface ZfRechargeService {

    List<ZfRecharge> queryByLimit(int page, int pagenum);


    /**
     * 通过ID查询单条数据
     *
     * @param merchantOrderNo 主键
     * @return 实例对象
     */
    ZfRecharge queryById(String merchantOrderNo);

    ZfRecharge queryByMerchantOrderNo(String merchantOrderNo);


    JSONObject query(QueryParams queryParams);

    ZfRecharge tryFindOrderByTrans(ZfTransRecord zfTransRecord, ZfCode zfCode);

    void paidOrder(ZfRecharge zfRecharge);

    void confirmOrder(OperaOrderParams operaOrderParams);

    void cancelOrder(OperaOrderParams operaOrderParams);

    public void notify(ZfRecharge zfRecharge);

    JSONObject getOrderStatus(String  orderno);

    void operatOrder(OperaBalanceParams operaBalanceParams);

    void autocancel(OperaOrderParams operaOrderParams);

    void postName(Map<String, Object> map);

    JSONObject createA(RechareParams rechareParams);

    ZfRecharge queryByOrderNo(String merchant_order_no);
}
