package com.code.baseservice.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * (ZfChannelCode)实体类
 *
 * @author makejava
 * @since 2023-03-19 23:07:05
 */
@Data
public class ZfChannelCode implements Serializable {
    private static final long serialVersionUID = -60182995217094865L;
    /**
     * 渠道id,
     */
    private Integer channeId;
    /**
     * 二维码Id
     */
    private Integer codeId;

}

