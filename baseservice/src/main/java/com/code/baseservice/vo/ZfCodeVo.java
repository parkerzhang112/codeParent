package com.code.baseservice.vo;

import com.code.baseservice.entity.ZfCode;
import lombok.Data;

import java.math.BigDecimal;

/**
 * (ZfCode)实体类
 *
 * @author makejava
 * @since 2023-03-19 23:07:15
 */
@Data
public class ZfCodeVo extends ZfCode {
    private static final long serialVersionUID = 449487199437871700L;

    //当日充值笔数
    private Integer rechargeTimes;

    //当日充值额度
    private BigDecimal rechargeAmount;

    //当日手续费
    private BigDecimal fee;

}

