package com.code.baseservice.base.enums;

public enum TransOrderTypeEnum {

    TRANS_ORDER(1,"代付订单"),
    ISSUE_ORDER(2,"下发订单"),
    INI_ORDER(3,"内转订单"),
    OUT_ORDER(5,"内转订单")

            ;

    private Integer value;
    private String name;

    TransOrderTypeEnum(Integer value, String name) {
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
