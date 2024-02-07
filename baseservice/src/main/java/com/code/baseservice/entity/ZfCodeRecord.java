package com.code.baseservice.entity;

import com.code.baseservice.util.DateUtil;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * (ZfCodeRecord)实体类
 *
 * @author makejava
 * @since 2023-03-19 23:07:26
 */
@Data
public class ZfCodeRecord implements Serializable {
    private static final long serialVersionUID = 561655651068564461L;
    
    private Integer codeId;
    
    private String recordDate;
    
    private BigDecimal rechargeAmount;
    
    private Integer rechargeTimes;
    
    private BigDecimal fee;

    private Integer agentId;



    public ZfCodeRecord(ZfRecharge zfRecharge) {
        codeId  = zfRecharge.getCodeId();
        recordDate = DateUtil.format(new Date(), DateUtil.YYYY_MM_DD);
        rechargeAmount = zfRecharge.getPaidAmount();
        rechargeTimes = 1;
        agentId = zfRecharge.getAgentId();
    }

    public  ZfCodeRecord(){

    }
}

