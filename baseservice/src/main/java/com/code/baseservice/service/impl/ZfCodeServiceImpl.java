package com.code.baseservice.service.impl;

import com.code.baseservice.base.enums.ResultEnum;
import com.code.baseservice.base.exception.BaseException;
import com.code.baseservice.dao.ZfCodeDao;
import com.code.baseservice.dto.payapi.RechareParams;
import com.code.baseservice.entity.ZfChannel;
import com.code.baseservice.entity.ZfCode;
import com.code.baseservice.entity.ZfWithdraw;
import com.code.baseservice.service.RedisUtilService;
import com.code.baseservice.service.ZfCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    public List<ZfCode> queryCodeByParamAndChannel(List<ZfChannel> zfChannels, RechareParams rechareParams) {

        List<Integer> ids = zfChannels.stream().map(ZfChannel::getChannelId).collect(Collectors.toList());
        List<ZfCode> zfCodes = zfCodeDao.selectCodeByChannelAndParams(ids, rechareParams.getPay_amount());
        List<ZfCode> filterCard = new ArrayList<>();
        for (int i =0 ; i < zfCodes.size(); i ++){
            String codeAmount = "onlyAmount"+rechareParams.getPay_amount().toBigInteger()+zfCodes.get(i).getName();
            if(redisUtilService.hasKey(codeAmount)){
                continue;
            }
            filterCard.add(zfCodes.get(i));
        }
        if(filterCard.size() == 0){
            throw  new BaseException(ResultEnum.NO_CODE);
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

}
