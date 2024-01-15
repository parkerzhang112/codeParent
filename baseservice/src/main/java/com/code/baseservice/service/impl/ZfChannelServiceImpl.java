package com.code.baseservice.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.code.baseservice.base.enums.ResultEnum;
import com.code.baseservice.base.exception.BaseException;
import com.code.baseservice.dao.ZfChannelDao;
import com.code.baseservice.dto.XChannelRate;
import com.code.baseservice.dto.payapi.RechareParams;
import com.code.baseservice.dto.payapi.TransferParams;
import com.code.baseservice.entity.ZfChannel;
import com.code.baseservice.service.ZfChannelService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * (ZfChannel)表服务实现类
 *
 * @author makejava
 * @since 2023-03-19 23:06:53
 */
@Service("zfChannelService")
@Slf4j
public class ZfChannelServiceImpl implements ZfChannelService {
    @Resource
    private ZfChannelDao zfChannelDao;

    /**
     * 通过ID查询单条数据
     *
     * @param channelId 主键
     * @return 实例对象
     */
    @Override
    public ZfChannel queryById(Integer channelId) {
        return this.zfChannelDao.queryById(channelId);
    }

    @Override
    public ZfChannel queryChannelByParams(RechareParams rechareParams) {
        log.info("开始查询渠道 订单号 {}", rechareParams.getMerchant_order_no());
        List<ZfChannel> channels = zfChannelDao.selectChannel(rechareParams);
        if (channels.size() == 0) {
            log.info("无可用渠道 订单号 {}", rechareParams.getMerchant_order_no());
            throw new BaseException(ResultEnum.NO_CHANNEL);
        }
        log.info("渠道查询结果 订单号 {}", rechareParams.getMerchant_order_no(), channels);
       return channels.get((int) (Math.random() * channels.size()));
//        return channels.get(0);
    }

    @Override
    public void sumMerchantBalance(Integer merchantId, BigDecimal subtract) {

    }

    @Override
    public List<ZfChannel> selectChannel(TransferParams transParams) {
        log.info("开始查询渠道 订单号 {}", transParams.getMerchant_order_no());
        List<ZfChannel> channels = zfChannelDao.selectChannelByTrans(transParams);
        if (channels.size() == 0) {
            log.info("无可用渠道 订单号 {}", transParams.getMerchant_order_no());
            throw new BaseException(ResultEnum.NO_CHANNEL);
        }
        return channels;
    }

    @Override
    public BigDecimal sumChannelFee(@NonNull BigDecimal PaidAmount, ZfChannel zfChannel) {
        try {
            if (Strings.isEmpty(zfChannel.getChannelRate())) {
                return BigDecimal.ZERO;
            }
            XChannelRate xChannelRate = JSONObject.parseObject(zfChannel.getChannelRate(), XChannelRate.class);
            if (xChannelRate.getRate_type() == 0) {
                return BigDecimal.valueOf(xChannelRate.getRate_value());
            } else {
                BigDecimal rate = BigDecimal.valueOf(xChannelRate.getRate_value()).divide(new BigDecimal("100"));
                return PaidAmount.multiply(rate);
            }

        } catch (Exception e) {
            log.error("手续费计算异常", e);
        }
        return BigDecimal.ZERO;
    }


}
