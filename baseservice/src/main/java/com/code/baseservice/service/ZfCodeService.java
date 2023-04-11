package com.code.baseservice.service;

import com.code.baseservice.dto.payapi.RechareParams;
import com.code.baseservice.entity.ZfChannel;
import com.code.baseservice.entity.ZfCode;
import com.code.baseservice.entity.ZfWithdraw;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

/**
 * (ZfCode)表服务接口
 *
 * @author makejava
 * @since 2023-03-19 23:07:16
 */
public interface ZfCodeService {

    /**
     * 通过ID查询单条数据
     *
     * @param codeId 主键
     * @return 实例对象
     */
    ZfCode queryById(Integer codeId);

    int update(ZfCode code);


    List<ZfCode> queryCodeByParamAndChannel(List<ZfChannel> zfChannels, RechareParams rechareParams);

    ZfCode selectCardByTrans(ZfWithdraw zfWithdraw);

    ZfCode queryByAccount(String account);
}
