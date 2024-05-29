package com.code.baseservice.service.impl;

import com.code.baseservice.base.constant.RedisConstant;
import com.code.baseservice.base.enums.CodeTypeEnum;
import com.code.baseservice.base.enums.PaytypeEnum;
import com.code.baseservice.base.enums.ResultEnum;
import com.code.baseservice.base.exception.BaseException;
import com.code.baseservice.dao.ZfCodeDao;
import com.code.baseservice.dto.autoapi.TransParams;
import com.code.baseservice.entity.ZfCode;
import com.code.baseservice.entity.ZfRecharge;
import com.code.baseservice.entity.ZfWithdraw;
import com.code.baseservice.service.RedisUtilService;
import com.code.baseservice.service.ZfCodeService;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
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
        if(payType == 3){
            codeType = 1;
       }
        if(payType == PaytypeEnum.微信小程序.getValue() || payType == PaytypeEnum.微信原生.getValue()){
            codeType = CodeTypeEnum.微信商户.getCode();
        }
        List<ZfCode> zfCodes = zfCodeDao.selectCodeByChannelAndParams(ids, zfRecharge.getPayAmount(), codeType);
        if(zfCodes.size() == 0){
            log.error("sql查询出来无可用码 {}", zfRecharge);
            throw new BaseException(ResultEnum.NO_CODE);
        }
        List<ZfCode> filterCard = new ArrayList<>();
        for (int i =0 ; i < zfCodes.size(); i ++){
            String key  = RedisConstant.LIMIT + zfCodes.get(i).getCodeId();
            if(redisUtilService.hasKey(key)){
                continue;
            }
            filterCard.add(zfCodes.get(i));
        }
        if(filterCard.size() == 0){
            log.error("过滤后出来无可用码 {}", zfRecharge);
            throw new BaseException(ResultEnum.NO_CODE);
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

}
