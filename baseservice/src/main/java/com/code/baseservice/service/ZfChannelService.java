package com.code.baseservice.service;

import com.code.baseservice.dto.payapi.RechareParams;
import com.code.baseservice.dto.payapi.TransferParams;
import com.code.baseservice.entity.ZfChannel;
import com.code.baseservice.entity.ZfRecharge;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.util.List;


/**
 * (ZfChannel)表服务接口
 *
 * @author makejava
 * @since 2023-03-19 23:06:52
 */
public interface ZfChannelService {

    /**
     * 通过ID查询单条数据
     *
     * @param channelId 主键
     * @return 实例对象
     */
    ZfChannel queryById(Integer channelId);


    ZfChannel queryChannelByParams(RechareParams rechareParams);

    void sumMerchantBalance(Integer merchantId, BigDecimal subtract);

    List<ZfChannel> selectChannel(TransferParams transParams);


    BigDecimal sumChannelFee(@NonNull BigDecimal pay_amount, ZfChannel zfChannel);

    void updateAgentFee(ZfRecharge zfRecharge);
}
