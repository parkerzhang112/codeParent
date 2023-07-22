package com.code.baseservice.base.enums;

public enum TransTypeEnum {

    RRCHARGE(1,"转入"),
    TRANSFER(0,"转出"),
    INTERNAL (2,"内转"),

    ;

    private Integer value;
    private String name;

    TransTypeEnum(Integer value, String name) {
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
