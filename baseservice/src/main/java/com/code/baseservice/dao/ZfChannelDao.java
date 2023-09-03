package com.code.baseservice.dao;

import com.code.baseservice.dto.payapi.RechareParams;
import com.code.baseservice.dto.payapi.TransferParams;
import com.code.baseservice.entity.ZfChannel;

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
     * 筛选渠道
     * @param rechareParams
     * @return
     */
    List<ZfChannel> selectChannel(RechareParams rechareParams);

    List<ZfChannel> selectChannelByTrans(TransferParams transParams);

    void updateChannelFee(ZfChannel zfChannel);
}

