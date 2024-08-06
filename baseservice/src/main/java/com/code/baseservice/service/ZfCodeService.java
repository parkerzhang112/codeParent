package com.code.baseservice.service;

import com.code.baseservice.dto.autoapi.TransParams;
import com.code.baseservice.dto.frontapi.code.AddCodeDto;
import com.code.baseservice.dto.frontapi.code.QueryCodeDto;
import com.code.baseservice.dto.payapi.RechareParams;
import com.code.baseservice.entity.*;
import com.code.baseservice.vo.ZfCodeVo;

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


    List<ZfCode> queryCodeByParamAndChannel(ZfRecharge zfRecharge);

    List<ZfCode> queryCodeByParamAndChannel(RechareParams rechareParams, ZfChannel zfChannel);


    ZfCode selectCardByTrans(ZfWithdraw zfWithdraw);

    ZfCode queryByAccount(String account);

    void heart(TransParams transParams);

    ZfCode queryByProudctId(String productId);

    ZfCode queryByName(String name);

    ZfCode queryByAccountByLike(String account);

    List<ZfCode> queryGroupList(Integer i);

    List<ZfCodeVo> queryListByAgentId(QueryCodeDto agentId);

    void addCode(AddCodeDto addCodeDto, ZfAgent zfAgent);
}
