package com.code.baseservice.base.enums;

public enum PaytypeEnum {

    CODE(0,"支付宝二维码"),
    TRANS(1,"支付宝转账"),
    SMALL_CODE(2,"小额支付宝扫码"),
    SHUZI(3,"数字人民币"),
    S_YUNSHANFU(4,"小额云闪付"),
    YUNSHANFU(5,"大额云闪付"),
    S_WEIXIN_CODE(6,"小额微信扫码支付"),
    WEIXIN_CODE(7,"微信扫码支付"),
    微信原生(10,"微信原生扫码"),
    微信小程序(9,"微信小程序"),
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
            default:
                break;
        }
        return  view;
    }
}
