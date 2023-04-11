package com.code.baseservice.base.enums;

public enum  CommonStatusEnum {

    STATR(1,"启用"),
    STOP(0,"停用");

    private Integer value;
    private String name;

    CommonStatusEnum(Integer value, String name) {
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
