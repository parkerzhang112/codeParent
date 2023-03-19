package com.code.baseservice.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * (ZfTransRecord)实体类
 *
 * @author makejava
 * @since 2023-03-19 23:08:34
 */
@Data
public class ZfTransRecord implements Serializable {
    private static final long serialVersionUID = 874048920690000119L;
    
    private Integer transId;
    
    private Integer codeId;
    
    private BigDecimal amount;
    
    private BigDecimal preBalance;
    
    private BigDecimal balance;
    
    private Date transTime;
    
    private Date createTime;
    
    private String merchantOrderNo;
    
    private String md5;

}

