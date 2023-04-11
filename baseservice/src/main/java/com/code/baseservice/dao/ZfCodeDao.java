package com.code.baseservice.dao;

import com.code.baseservice.dto.payapi.RechareParams;
import com.code.baseservice.entity.ZfCode;
import com.code.baseservice.entity.ZfWithdraw;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;

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

    List<ZfCode> selectCodeByChannelAndParams(@Param("channel_ids") List<Integer> ids, @Param("pay_amount")BigDecimal payAmount);

    ZfCode selectCardByTrans(ZfWithdraw zfWithdraw);

    int update(ZfCode code);

    ZfCode queryByAccount(@Param("account") String account);
}

