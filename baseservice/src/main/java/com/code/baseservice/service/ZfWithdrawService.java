package com.code.baseservice.service;

import com.alibaba.fastjson.JSONObject;
import com.code.baseservice.dto.autoapi.TransParams;
import com.code.baseservice.dto.backapi.OperaOrderParams;
import com.code.baseservice.dto.payapi.QueryParams;
import com.code.baseservice.dto.payapi.TransferParams;
import com.code.baseservice.entity.ZfChannel;
import com.code.baseservice.entity.ZfMerchant;
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
     * 创建订单
     * @param transParams
     * @return
     */
    JSONObject create(TransferParams transParams);

    /**
     * 查单订单
     * @param queryParams
     * @return
     */
    JSONObject query(QueryParams queryParams);

    ZfWithdraw tryFindOrderByTrans(TransParams transParams1);

    void paidOrder(ZfWithdraw zfWithdraw);

    void confirmOrder(OperaOrderParams operaOrderParams);

    void cancelOrder(OperaOrderParams operaOrderParams);

    /**
     * 提款订单通知
     * @param zfWithdraw
     */
    public void notify(ZfWithdraw zfWithdraw);

    /**
     * 创建提款订单
     * @param transParams
     * @param zfChannel
     * @param xMerchant
     * @return
     */
    public ZfWithdraw createOrder(TransferParams transParams, ZfChannel zfChannel, ZfMerchant xMerchant);

    public ZfWithdraw createIssueOrder(TransferParams transParams, ZfMerchant xMerchant);

}
