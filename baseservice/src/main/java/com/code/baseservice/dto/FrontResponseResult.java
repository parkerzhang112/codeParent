package com.code.baseservice.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "通用 API 响应实体")
public class FrontResponseResult<T> {

    @ApiModelProperty(value = "响应消息", example = "操作成功")
    private String message;

    @ApiModelProperty(value = "响应代码", example = "操作成功")
    private int code;

    @ApiModelProperty(value  = "具体的返回数据", example = "操作成功")
    private T data;

    // 构造函数，getter 和 setter 方法
    public FrontResponseResult(String message, int code, T data) {
        this.message = message;
        this.code = code;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
