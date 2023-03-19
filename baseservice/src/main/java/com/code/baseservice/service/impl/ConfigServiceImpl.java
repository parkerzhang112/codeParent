package com.code.baseservice.service.impl;

import com.xiagao.baseservice.dao.XConfigDao;
import com.code.baseservice.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("ConfigService")
public class ConfigServiceImpl implements ConfigService {

    @Autowired
    XConfigDao xConfigDao;

    public XConfig queryMerchantByKey(String key){
        return  xConfigDao.queryConfigByKey(key);
    }
}
