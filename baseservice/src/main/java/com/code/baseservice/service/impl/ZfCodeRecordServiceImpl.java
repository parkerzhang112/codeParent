package com.code.baseservice.service.impl;

import com.code.baseservice.dao.ZfCodeRecordDao;
import com.code.baseservice.dto.payapi.RechareParams;
import com.code.baseservice.entity.ZfCode;
import com.code.baseservice.entity.ZfCodeRecord;
import com.code.baseservice.entity.ZfRecharge;
import com.code.baseservice.service.ZfCodeRecordService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * (ZfCodeRecord)表服务实现类
 *
 * @author makejava
 * @since 2023-03-19 23:07:26
 */
@Service("zfCodeRecordService")
public class ZfCodeRecordServiceImpl implements ZfCodeRecordService {
    @Resource
    private ZfCodeRecordDao zfCodeRecordDao;


    /**
     * 修改数据
     *
     * @param zfCodeRecord 实例对象
     * @return 实例对象
     */
    @Override
    public void update(ZfCodeRecord zfCodeRecord) {
        this.zfCodeRecordDao.update(zfCodeRecord);
        return;
    }

    @Override
    public void updateRecord(ZfCodeRecord zfCodeRecord) {
        ZfCodeRecord zfCodeRecord1 =  zfCodeRecordDao.queryByIdAndDate(zfCodeRecord);
        if(zfCodeRecord1 == null){
            zfCodeRecordDao.insert(zfCodeRecord);
        }else {
            zfCodeRecordDao.updateRecord(zfCodeRecord);
        }
    }

}
