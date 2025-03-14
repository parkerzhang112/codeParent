package com.code.baseservice.service;

import com.code.baseservice.dto.autoapi.TransParams;
import com.code.baseservice.entity.ZfTransRecord;
import com.code.baseservice.entity.ZfWithdraw;

import java.util.List;

/**
 * (ZfTransRecord)表服务接口
 *
 * @author makejava
 * @since 2023-03-19 23:08:34
 */
public interface ZfTransRecordService {


    /**
     * 上传解析服务函数
     *
     * @author makejava
     * @since 2023-03-19 23:08:34
     */
    void upload(TransParams transParams);

    List<ZfTransRecord> queryTransByWithdraw(ZfWithdraw zfWithdraw);

    void checkTxt(TransParams transParams, String ip);

    int update(ZfTransRecord zfTransRecord);

    ZfTransRecord queryId(int trandId);
}
