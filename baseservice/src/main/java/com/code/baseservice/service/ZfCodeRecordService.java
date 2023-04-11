package com.code.baseservice.service;

import com.code.baseservice.dto.payapi.RechareParams;
import com.code.baseservice.entity.ZfCode;
import com.code.baseservice.entity.ZfCodeRecord;
import com.code.baseservice.entity.ZfRecharge;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

/**
 * (ZfCodeRecord)表服务接口
 *
 * @author makejava
 * @since 2023-03-19 23:07:26
 */
public interface ZfCodeRecordService {

    /*
     * 修改数据
     *
     * @param zfCodeRecord 实例对象
     * @return 实例对象
     */
    void update(ZfCodeRecord zfCodeRecord);


    void updateRecord(ZfCodeRecord zfCodeRecord);
}
