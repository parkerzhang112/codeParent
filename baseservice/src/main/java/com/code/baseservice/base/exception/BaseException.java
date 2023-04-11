package com.code.baseservice.base.exception;

import com.code.baseservice.base.enums.ResultEnum;
import lombok.Data;

@Data
public class BaseException extends  RuntimeException{

    private static final long serialVersionUID = 1L;

    private Integer code;  //错误码

    public BaseException() {}

    public BaseException(ResultEnum resultEnum) {
        super(resultEnum.getMsg());
        this.code = resultEnum.getCode();
    }
}
