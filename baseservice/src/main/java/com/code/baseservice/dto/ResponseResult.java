package com.code.baseservice.dto;


import com.alibaba.fastjson.JSONObject;
import lombok.Getter;

@Getter
public class ResponseResult {

    //响应消息内容
    private  String  msg  = "操作成功";

    private int code = 200;

    private Object data = "";

    private String token = "";

    public String toJsonString(){
        return JSONObject.toJSONString(this);
    }

    public ResponseResult setCode(int code){
        this.code = code;
        return this;
    }

    public ResponseResult setToken(String  token){
        this.token = token;
        return this;
    }

    public ResponseResult setMsg(String msg){
        this.msg = msg;
        return  this;
    }

    public ResponseResult setData(Object data){
        this.data = data;
        return this;
    }



}
