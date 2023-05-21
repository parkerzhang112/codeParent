package com.code.baseservice.service.impl;

import com.code.baseservice.base.enums.TransTypeEnum;
import com.code.baseservice.dao.ZfTransRecordDao;
import com.code.baseservice.dto.autoapi.TransParams;
import com.code.baseservice.entity.*;
import com.code.baseservice.service.*;
import com.code.baseservice.util.StringUtil;
import com.code.baseservice.util.StringUtils;
import com.code.baseservice.util.Telegram;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    ZfAgentService zfAgentService;

    @Autowired
    ZfWithdrawService zfWithdrawService;

    @Override
    public void upload(TransParams transParams) {
        //查询码
        ZfCode zfCode = null;
        if(!StringUtils.isEmpty(transParams.getAccount())){
            if(!transParams.getAccount().contains("*")){
                zfCode = zfCodeService.queryByAccount(transParams.getAccount());

            }else {
                zfCode = zfCodeService.queryByAccountByLike(transParams.getAccount());

            }
        }else {
             zfCode = zfCodeService.queryByName(transParams.getLoginName());
        }
        if(zfCode == null){
            log.error("非系统二维码 {}", transParams);
            return;
        }
        //更新码余额
        zfCode.setBalance(transParams.getBalance());
        zfCode.setUpdateTime(new Date());
        zfCode.setIp(transParams.getIp());
        zfCodeService.update(zfCode);
        try{
            int  expire =  24 * 3600;
            //排重
            String md5 = (DigestUtils.md5DigestAsHex(
                    zfCode.getAccount()
                            .concat(transParams.getName().toString())
                            .concat(transParams.getAmout().toString())
                            .concat(transParams.getTransTime().toString())
                            .getBytes()));
            if(redisUtilService.hasKey(md5)){
                log.info("流水已存在");
                return;
            }
            redisUtilService.set(md5, 1, expire);
            ZfTransRecord transRecord = new ZfTransRecord(transParams);
            transRecord.setCodeId(zfCode.getCodeId());
            transRecord.setAgentId(zfCode.getAgentId());
            //匹配订单
            if(transParams.getTransType().equals(TransTypeEnum.RRCHARGE.getValue())) {
                ZfRecharge zfRecharge = zfRechargeService.tryFindOrderByTrans(transRecord, zfCode);
                if (zfRecharge != null) {
                    transRecord.setStatus(1);
                    transRecord.setMerchantOrderNo(zfRecharge.getMerchantOrderNo());
                }
            }
            //写入流水
            zfTransRecordDao.insert(transRecord);
        }catch (Exception e){
            log.info("执行异常 {}", e);
        }

    }

    @Override
    public List<ZfTransRecord> queryTransByWithdraw(ZfWithdraw zfWithdraw) {
        return  zfTransRecordDao.queryTransByWithdraw(zfWithdraw);
    }

    @Override
    public void checkTxt(TransParams transParams, String ip) {
        ZfCode zfCode = zfCodeService.queryByAccount(transParams.getAccount());
        zfCode.setIp(ip);
        ZfAgent  zfAgent = zfAgentService.queryById(zfCode.getAgentId());
        Telegram telegram = new Telegram();
        if(transParams.getBankCode().equals("Alipay")){
            if(!transParams.getTxt().contains("商家订单号")){
                zfCode.setIsOpen(0);
                zfCodeService.update(zfCode);
                telegram.sendWarrnCodeClient("个人码登录需要验证", zfCode, zfAgent.getConfig());
            }
        }
        if(transParams.getTxt().equals("AlipayComany")){
            if(!transParams.getTxt().contains("支付宝交易号")){
                zfCode.setIsOpen(0);
                zfCodeService.update(zfCode);
                telegram.sendWarrnCodeClient("企业码登录需要验证", zfCode, zfAgent.getConfig());
            }
        }
    }

    @Override
    public int update(ZfTransRecord zfTransRecord){
        return zfTransRecordDao.update(zfTransRecord);
    }

    @Override
    public ZfTransRecord queryId(int trandId) {
        return zfTransRecordDao.queryById(trandId);
    }
}
