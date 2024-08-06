package com.code.baseservice.dao;

import com.code.baseservice.dto.frontapi.code.QueryCodeDto;
import com.code.baseservice.entity.ZfCode;
import com.code.baseservice.entity.ZfWithdraw;
import com.code.baseservice.vo.ZfCodeVo;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * (ZfCode)表数据库访问层
 *
 * @author makejava
 * @since 2023-03-19 23:07:15
 */
public interface ZfCodeDao {

    /**
     * 通过ID查询单条数据
     *
     * @param codeId 主键
     * @return 实例对象
     */
    ZfCode queryById(Integer codeId);

    List<ZfCode> selectCodeByChannelAndParams(@Param("channel_ids") List<Integer> ids, @Param("pay_amount")BigDecimal payAmount, @Param("code_type") Integer codeType);

    ZfCode selectCardByTrans(ZfWithdraw zfWithdraw);

    int update(ZfCode code);

    int insert(ZfCode code);


    ZfCode queryByAccount(@Param("account") String account);

    void updateByHeart(@Param("account") String account);

    ZfCode queryByProudctId(@Param("productId") String productId);

    ZfCode queryByName(@Param("name") String name);

    ZfCode queryByAccountByLike(@Param("account") String account);

    List<ZfCode> queryCodeByMerchant(@Param("merchantId") Integer merchantId);

    List<ZfCodeVo> queryListByAgentId(QueryCodeDto agentId);
}

