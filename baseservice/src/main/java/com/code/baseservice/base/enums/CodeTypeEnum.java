package com.code.baseservice.base.enums;

public enum CodeTypeEnum {
    二维码(0, "二维码"),
    数字人名币(1, "数字人民币"),
    微信二维码(3, "微信二维码"),
    云闪付(2, "云闪付"),
    银行卡(4, "银行卡"),
    微信商户(5, "微信商户");


    private final Integer code;

    private final String value;

    CodeTypeEnum(Integer code , String value){
        this.code = code;
        this.value = value;
    }

    public Integer getCode(){
        return code;
    }
}
