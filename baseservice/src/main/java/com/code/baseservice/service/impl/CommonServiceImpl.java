package com.code.baseservice.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.code.baseservice.dto.payapi.RechareParams;
import com.code.baseservice.entity.ZfChannel;
import com.code.baseservice.service.CommonService;
import com.sun.org.apache.bcel.internal.generic.RETURN;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CommonServiceImpl implements CommonService {


    @Override
    public JSONObject request(ZfChannel zfChannel, RechareParams rechareParams) {
        return  new JSONObject();
    }
}
