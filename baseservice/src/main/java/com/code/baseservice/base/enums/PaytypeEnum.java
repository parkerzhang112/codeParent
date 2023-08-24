package com.code.baseservice.base.enums;

public enum PaytypeEnum {

    CODE(0,"支付宝二维码"),
    TRANS(1,"支付宝转账"),
            ;

    private Integer value;
    private String name;

    PaytypeEnum(Integer value, String name) {
        this.value = value;
        this.name = name;
    }

    public Integer getValue() {
        return value;
    }

    public String getName() {
        return name;
    }
}
