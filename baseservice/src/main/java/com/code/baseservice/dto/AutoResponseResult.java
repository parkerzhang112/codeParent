package com.code.baseservice.dto;


import com.alibaba.fastjson.JSONObject;
import lombok.Getter;

@Getter
public class AutoResponseResult {

    //响应消息内容
    private  String  msg  = "";

    private int status = 200;


    private String retCode = "SUCCESS";

    private int code = 1;

    private Object data = "";

    private String token = "";

    public String toJsonString(){
        return JSONObject.toJSONString(this);
    }

    public AutoResponseResult setStatus(int status){
        this.status = status;
        return this;
    }

    public AutoResponseResult setToken(String  token){
        this.token = token;
        return this;
    }

    public AutoResponseResult setCode(Integer  code){
        this.code = code;
        return this;
    }

    public AutoResponseResult setMsg(String msg){
        this.msg = msg;
        return  this;
    }

    public AutoResponseResult setData(Object data){
        this.data = data;
        return this;
    }



}
