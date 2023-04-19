package com.code.autoapi.controller;

import com.alibaba.fastjson.JSONObject;

import com.code.baseservice.base.exception.BaseException;
import com.code.baseservice.dto.AutoResponseResult;
import com.code.baseservice.dto.autoapi.TransParams;
import com.code.baseservice.service.ZfTransRecordService;
import com.code.baseservice.util.IpUtil;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Api(tags = "流水", description = "流水api")
@Slf4j
@RestController
public class TransController {

    @Autowired
    ZfTransRecordService zfTransRecordService;

    private String token  = "cbphaCnDjWG2rfqfYDHscQxSaHfjsZS6";

    @PostMapping(value ={"/zdjrecords"})
    @ResponseBody
    public String txtrecords(@RequestBody TransParams transParams, HttpServletRequest request){
        AutoResponseResult responseResult = new AutoResponseResult();
        try {
            log.info("流水上报 {}", transParams);
            String ip  = IpUtil.getIpAddr(request);
            transParams.setIp(ip);
            transParams.parseTxt();
            zfTransRecordService.upload(transParams);
            responseResult.setStatus(1);
        }catch (BaseException e){
            responseResult.setStatus(1);
            //其他非法异常，重新上传;
        }catch (Exception e){
            log.error("异常流水流水内容 {}", e.getStackTrace());
            responseResult.setStatus(1);
        }
        responseResult.setMsg("操作成功");
        return responseResult.toJsonString();
    }


    @PostMapping(value ={"/api/index/sms"})
    @ResponseBody
    public String sms(@RequestBody TransParams transParams){
        AutoResponseResult responseResult = new AutoResponseResult();
        try {
            transParams.parseTxt();
            zfTransRecordService.upload(transParams);
            responseResult.setStatus(1);
        }catch (BaseException e){
            responseResult.setStatus(1);
            //其他非法异常，重新上传;
        }catch (Exception e){
            log.error("异常流水流水内容", e.getStackTrace());
            responseResult.setStatus(1);
        }
        responseResult.setMsg("操作成功");
        return responseResult.toJsonString();
    }

    /**
     * 固定成功
     * @return
     */
    @PostMapping(value = {"/api/user/login"})
    @ResponseBody
    public String login(){
        AutoResponseResult responseResult = new AutoResponseResult();
        log.info("用户登出  {}");
        try {
            JSONObject user = new JSONObject();
            user.put("username", "defalut");
            responseResult.setToken(DigestUtils.md5Hex(token));
            responseResult.setData(user);
            responseResult.setStatus(1);
        }catch (Exception e){
            responseResult.setStatus(0).setMsg("系统异常");
        }
        return responseResult.toJsonString();
    }

    /**
     * 固定成功
     * @return
     */
    @PostMapping(value = {"/o/login"})
    @ResponseBody
    public String ologin(){
        AutoResponseResult responseResult = new AutoResponseResult();
        log.info("用户登录  {}");
        try {
            JSONObject user = new JSONObject();
            user.put("username", "defalut");
            List<JSONObject> users = new ArrayList<>();
            users.add(user);
            responseResult.setToken(DigestUtils.md5Hex(token));
            responseResult.setData(users);
            responseResult.setStatus(1);
        }catch (Exception e){
            responseResult.setStatus(0).setMsg("系统异常");
            log.error("系统登录异常", e);
        }
        return responseResult.toJsonString();
    }




    @PostMapping(value = {"/api/user/logout", "/o/logout"})
    @ResponseBody
    public String logout(){
        AutoResponseResult responseResult = new AutoResponseResult();
        log.info("用户登除  {}");
        try {
            responseResult.setStatus(1);
            responseResult.setMsg("退出成功");
        }catch (Exception e){
            responseResult.setStatus(0);
        }
        return responseResult.toJsonString();
    }


}
