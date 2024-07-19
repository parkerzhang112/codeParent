package com.code.frontapi.controller;

import com.code.baseservice.base.exception.BaseException;
import com.code.baseservice.dto.ResponseResult;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Api(tags = "用户报表", description = "流水api")
@Slf4j
@RestController
@RequestMapping(value = "/user/report")
public class ReportController {

    @PostMapping(value ={"/index"})
    @ResponseBody
    public String index(HttpServletRequest request){
        ResponseResult responseResult = new ResponseResult();
        try {
        }catch (BaseException e){
            //其他非法异常，重新上传;
        }catch (Exception e){
            log.error("获取用户二维码异常 {}", e.getStackTrace());
        }
        responseResult.setMsg("操作成功");
        return responseResult.toJsonString();
    }

}
