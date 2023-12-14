package com.code.baseservice.base.enums;

public enum PaytypeEnum {

    CODE(0,"支付宝二维码"),
    TRANS(1,"支付宝转账"),
    SMALL_CODE(2,"小额支付宝扫码"),
    SHUZI(3,"数字人民币"),
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

    public static String getPayView(Integer paytype){
        String view  = "index";
        switch (paytype){
            case 1:
                view = "trans";
                break;
            case 3:
                view = "shuzi";
                break;
            case 4:
                view = "yunshanfu";
            default:
                break;
        }
        return  view;
    }
}
