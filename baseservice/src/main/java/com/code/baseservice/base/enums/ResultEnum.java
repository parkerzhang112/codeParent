package com.code.baseservice.base.enums;

public enum ResultEnum {
    UNKNOWN_ERROR(-1,"系统错误"),
    SUCCESS(0,"成功"),
    ERROR(1,"失败"),
    NO_CHANNEL(700, "无可用渠道"),
    NO_CODE(701,"无码可用"),
    SIGN_ERROR(702,"签名异常"),

    ORDER_REPEAT(703, "订单号重复"),
    NO_VAILD_MERCHANT(704, "无可用商户"),

    ORDER_NO_EXIST(705, "订单不存在"),

    USER_NO_LOGIN(706, "用户未登录"),
    USER_ACCOUNT_PAAWROD_ERROR(707, "用户账户或密码错误"),
    USER_GOOGLE_CODE_ERROR(708, "验证码错误"),

    MERCHANT_BALANCE_NO_ENOUGH(709, "商户余额不足"),
    ORDER_REPEAT_NOTIFY(710, "订单重复通知"),

    OPRDER_CANT_OPERA(711, "订单未操作，不能补分操作"),

    OPRDER_REPEAT(713, "订单补分操作重复"),

    TRANS_CANT_OPERA(712, "代付订单不能失败补分"),
    REFUSE_ORRDER(713, "交易失败"),
    CARD_BALANCE_NO_ENOUGH(714, "卡余额不足"),
    FORBBIDEN_SPILIT_ORDER(715, "该订单禁止拆单"),
    ORDER_STATUS_ERROR(716, "订单状态错误"),
    SUM_ORDER_NO_FINISH(717, "子订单未完成"),
    SUM_ORDER_ALL_ERROR(718, "子订单都失败，禁止确认"),
    TRANS_EXIST_TRANSFER(719, "存在该订单流水，禁止取消订单，请核实"),
    ILLEGAL_ORDER(720, "非法订单"),

    CREATE_ERROR(801, "创建订单异常"),

    NO_SPPORT_BANK(802, "暂不支持银行"),

    REPEAT_SMS(803, "重复短信"),
    NO_BANK_SMS(804, "非银行短信"),
    NO_EXIST_CARD(805, "不存在卡"),
    FIND_ORDER(806, "匹配失败"),
    RATE_ERROR(807, "获取汇率失败"),
    REPEAT_TRANS(808, "重复流水"),
    NO_WITHDRAW_ORDER(809, "暂无合适出款订单"),
    REPEAT_CATCH_ORDER(809, "重复抓单"),
    TRANS_STATUS_EXCEPTION(809, "流水状态异常"),
    ORDER_LOCKED(810, "订单已锁定"),
    NO_ENOUGH_AMOUNT(811, "积分不够"),
    NO_COMMISSION_AMOUNT(812, "佣金不够"),
    CHANNEL_BALANCE_NO_ENOUGH(813, "渠道余额不足"),


    ;


    private Integer code;
    private String msg;

    ResultEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
