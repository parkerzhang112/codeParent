package com.code.baseservice.base.constant;

public enum SpiltEnum {

    NO_SPILIT(0,"未分单"),
    IS_SPILIT(1,"已分单"),
            ;

    private Integer value;
    private String name;

    SpiltEnum(Integer value, String name) {
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
