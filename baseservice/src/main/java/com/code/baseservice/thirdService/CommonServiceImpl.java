package com.code.baseservice.thirdService;

import com.alibaba.fastjson.JSONObject;
import com.code.baseservice.base.enums.ResultEnum;
import com.code.baseservice.base.exception.BaseException;
import com.code.baseservice.dto.payapi.QueryParams;
import com.code.baseservice.entity.ZfChannel;
import com.code.baseservice.entity.ZfRecharge;
import com.code.baseservice.service.BaseService;
import com.code.baseservice.service.CommonService;
import com.code.baseservice.service.ZfChannelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CommonServiceImpl implements CommonService {


    @Autowired
    private SanShiServiceImpl sanShiService;

    @Autowired
    private ZfChannelService zfChannelService;

    public BaseService transToService(String thirdMerchantId){
        ZfChannel zfChannel = zfChannelService.queryByMerchantId(thirdMerchantId);
        switch (zfChannel.getChannelCode()){
            case "SANSHI":
                return sanShiService;
            default:
                throw new BaseException(ResultEnum.ERROR);
        }
    }

    @Override
    public JSONObject create(ZfChannel zfChannel, ZfRecharge zfRecharge) {
        BaseService baseService = transToService(zfChannel.getThirdMerchantId());
        return  baseService.create(zfChannel, zfRecharge);
    }

    @Override
    public JSONObject notify(QueryParams queryParams) {
        BaseService baseService = transToService(queryParams.getMerchant_Id());
        return  baseService.notify(queryParams);
    }
}
