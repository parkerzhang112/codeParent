package com.code.baseservice.service.impl;

import com.code.baseservice.dao.ZfCodeDao;
import com.code.baseservice.dto.autoapi.TransParams;
import com.code.baseservice.dto.payapi.RechareParams;
import com.code.baseservice.entity.ZfChannel;
import com.code.baseservice.entity.ZfCode;
import com.code.baseservice.entity.ZfRecharge;
import com.code.baseservice.entity.ZfWithdraw;
import com.code.baseservice.service.RedisUtilService;
import com.code.baseservice.service.ZfCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * (ZfCode)表服务实现类
 *
 * @author makejava
 * @since 2023-03-19 23:07:16
 */
@Service("zfCodeService")
public class ZfCodeServiceImpl implements ZfCodeService {
    @Resource
    private ZfCodeDao zfCodeDao;

    @Autowired
    private RedisUtilService redisUtilService;

    /**
     * 通过ID查询单条数据
     *
     * @param codeId 主键
     * @return 实例对象
     */
    @Override
    public ZfCode queryById(Integer codeId) {
        return this.zfCodeDao.queryById(codeId);
    }

    @Override
    public int update(ZfCode code) {
        return zfCodeDao.update(code);
    }

    @Override
    public List<ZfCode> queryCodeByParamAndChannel(ZfRecharge zfRecharge) {

        List<Integer> ids = new ArrayList<Integer>();
        ids.add(zfRecharge.getChannelId());
       Integer payType =  zfRecharge.getPayType();
       Integer  codeType = 0;
        //数字
        if(payType == 3){
            codeType = 1;
        }
        //云闪付
        if(payType == 4 || payType == 5){
            codeType = 2;
        }
        //微信
        if(payType == 6 || payType == 7){
            codeType = 3;
        }
        //微信
        if(payType == 9){
            codeType = 4;
        }
        if(payType == 1){
            codeType = 6;
        }
        List<ZfCode> zfCodes = zfCodeDao.selectCodeByChannelAndParams(ids, zfRecharge.getPayAmount(), codeType);

        List<ZfCode> filterCard = new ArrayList<>();
        for (int i =0 ; i < zfCodes.size(); i ++){
//            String codeAmount = "onlyAmount"+zfRecharge.getPayAmount().toBigInteger()+zfCodes.get(i).getName();
//            if(redisUtilService.hasKey(codeAmount)){
//                continue;
//            }
            filterCard.add(zfCodes.get(i));
        }
        if(filterCard.size() == 0){

//            throw  new BaseException(ResultEnum.NO_CODE);
            return  filterCard;
        }
        return filterCard;
    }

    @Override
    public List<ZfCode> queryCodeByParamAndChannel(RechareParams rechareParams, ZfChannel zfChannel) {
        List<Integer> ids = new ArrayList<Integer>();
        ids.add(zfChannel.getChannelId());
        Integer payType =  zfChannel.getPayType();
        Integer  codeType = 0;
        //数字
        if(payType == 3){
            codeType = 1;
        }
        //云闪付
        if(payType == 4 || payType == 5){
            codeType = 2;
        }
        //微信
        if(payType == 6 || payType == 7){
            codeType = 3;
        }
        //微信
        if(payType == 9){
            codeType = 4;
        }
        List<ZfCode> zfCodes = zfCodeDao.selectCodeByChannelAndParams(ids, rechareParams.getPay_amount(), codeType);
        List<ZfCode> filterCard = new ArrayList<>();
        for (int i =0 ; i < zfCodes.size(); i ++){
            filterCard.add(zfCodes.get(i));
        }
        if(filterCard.size() == 0){
            return  filterCard;
        }
        return filterCard;
    }

    @Override
    public ZfCode selectCardByTrans(ZfWithdraw zfWithdraw) {
        return  zfCodeDao.selectCardByTrans(zfWithdraw);
    }

    @Override
    public ZfCode queryByAccount(String account) {
        return  zfCodeDao.queryByAccount(account);
    }

    @Override
    public void heart(TransParams transParams) {
        zfCodeDao.updateByHeart(transParams.getAccount());
    }

    @Override
    public ZfCode queryByProudctId(String productId) {
        return  zfCodeDao.queryByProudctId(productId);

    }

    @Override
    public ZfCode queryByName(String name) {
        return  zfCodeDao.queryByName(name);
    }

    @Override
    public ZfCode queryByAccountByLike(String account) {
        account = account.replaceAll("\\*+", "%");
        return  zfCodeDao.queryByAccountByLike(account);

    }

    @Override
    public List<ZfCode> queryGroupList(Integer  merchantId) {
        List<ZfCode> zfCodes = zfCodeDao.queryCodeByMerchant(merchantId);
        return zfCodes;
    }

}
