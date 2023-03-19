package com.code.baseservice.base.constant;

public enum WxStatusEnum {


    LOGIN(1,"登录"),
    LOGOUT(0,"退出"),
    OTHER(2,"其他");

    private Integer value;
    private String name;

    WxStatusEnum(Integer value, String name) {
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
