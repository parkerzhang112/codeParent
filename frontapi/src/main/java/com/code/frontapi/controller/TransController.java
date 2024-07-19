package com.code.frontapi.controller;

import com.code.baseservice.base.exception.BaseException;
import com.code.baseservice.dto.ResponseResult;
import com.code.baseservice.dto.frontapi.trans.QueryTransDto;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Api(tags = "用户报表", description = "流水api")
@Slf4j
@RestController
@RequestMapping(value = "/user/trans")
public class TransController {

    @PostMapping(value ={"/index"})
    @ResponseBody
    public String index(@RequestBody QueryTransDto queryTransDto, HttpServletRequest request){
        ResponseResult responseResult = new ResponseResult();
        try {
            log.info("获取用户流水 {}", queryTransDto);
        }catch (BaseException e){
            //其他非法异常，重新上传;
        }catch (Exception e){
            log.error("获取用户流水 ", e);
        }
        return responseResult.toJsonString();
    }
}
