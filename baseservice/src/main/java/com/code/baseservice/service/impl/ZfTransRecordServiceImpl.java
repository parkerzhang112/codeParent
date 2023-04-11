package com.code.baseservice.service.impl;

import com.code.baseservice.base.enums.TransOrderTypeEnum;
import com.code.baseservice.base.enums.TransTypeEnum;
import com.code.baseservice.dao.ZfTransRecordDao;
import com.code.baseservice.dto.autoapi.TransParams;
import com.code.baseservice.entity.ZfCode;
import com.code.baseservice.entity.ZfRecharge;
import com.code.baseservice.entity.ZfTransRecord;
import com.code.baseservice.entity.ZfWithdraw;
import com.code.baseservice.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * (ZfTransRecord)表服务实现类
 *
 * @author makejava
 * @since 2023-03-19 23:08:34
 */
@Service("zfTransRecordService")
@Slf4j
public class ZfTransRecordServiceImpl implements ZfTransRecordService {
    @Resource
    private ZfTransRecordDao zfTransRecordDao;

    @Autowired
    private ZfCodeService zfCodeService;

    @Autowired
    RedisUtilService redisUtilService;

    @Autowired
    ZfRechargeService zfRechargeService;

    @Autowired
    ZfWithdrawService zfWithdrawService;

    @Override
    public void upload(TransParams transParams) {
        //查询码
        ZfCode zfCode = zfCodeService.queryByAccount(transParams.getAccount());
        //更新码余额
        zfCode.setBalance(transParams.getBalance());
        zfCode.setUpdateTime(new Date());
        for (TransParams transParams1 : transParams.getTransParams()) {
            //排重
            String md5 = (DigestUtils.md5DigestAsHex(
                    transParams.getAccount()
                            .concat( transParams.getAmout().toString())
                            .concat(transParams.getTransTime().toString())
                            .getBytes()));
            if(redisUtilService.hasKey(md5)){
                log.info("流水已存在");
                return;
            }
            ZfTransRecord transRecord = new ZfTransRecord(transParams1);
            transRecord.setCodeId(zfCode.getCodeId());
            transRecord.setAccount(transParams.getAccount());
            //匹配订单
            if(transParams.getTransType().equals(TransTypeEnum.RRCHARGE.getValue())){
                ZfRecharge zfRecharge =  zfRechargeService.tryFindOrderByTrans(transRecord);
                if(zfRecharge != null){
                    transRecord.setMerchantOrderNo(zfRecharge.getMerchantOrderNo());
                    zfRechargeService.paidOrder(zfRecharge);
                }
            }else {
                ZfWithdraw zfWithdraw =  zfWithdrawService.tryFindOrderByTrans(transParams1);
                if(zfWithdraw != null){
                    transRecord.setMerchantOrderNo(zfWithdraw.getMerchantOrderNo());
                    zfWithdrawService.paidOrder(zfWithdraw);
                }
            }
            //写入流水
            zfTransRecordDao.insert(transRecord);
            //完成订单
        }
    }

    @Override
    public List<ZfTransRecord> queryTransByWithdraw(ZfWithdraw zfWithdraw) {
        return  zfTransRecordDao.queryTransByWithdraw(zfWithdraw);
    }
}
