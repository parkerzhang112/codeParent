package com.code.baseservice.service.impl;

import com.code.baseservice.dao.ZfMerchantRecordDao;
import com.code.baseservice.entity.ZfMerchantRecord;
import com.code.baseservice.service.ZfMerchantRecordService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


/**
 * (ZfMerchantRecord)表服务实现类
 *
 * @author makejava
 * @since 2023-03-19 23:08:08
 */
@Service("zfMerchantRecordService")
public class ZfMerchantRecordServiceImpl implements ZfMerchantRecordService {
    @Resource
    private ZfMerchantRecordDao zfMerchantRecordDao;


    @Override
    public void updateRecord(ZfMerchantRecord zfMerchantRecord) {
        ZfMerchantRecord zfMerchantRecord1 = zfMerchantRecordDao.queryById(zfMerchantRecord);
        if(zfMerchantRecord1 != null){
            zfMerchantRecordDao.updateByRecord(zfMerchantRecord);
        }else {
            zfMerchantRecordDao.insert(zfMerchantRecord);
        }
    }

}
