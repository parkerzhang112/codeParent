package com.code.baseservice.service;

import com.code.baseservice.entity.ZfMerchantRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

/**
 * (ZfMerchantRecord)表服务接口
 *
 * @author makejava
 * @since 2023-03-19 23:08:08
 */
public interface ZfMerchantRecordService {


    void updateRecord(ZfMerchantRecord zfMerchantRecord);
}
