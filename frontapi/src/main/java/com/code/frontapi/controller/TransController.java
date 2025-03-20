package com.code.frontapi.controller;

import com.code.baseservice.base.exception.BaseException;
import com.code.baseservice.dto.ResponseResult;
import com.code.baseservice.dto.frontapi.trans.QueryTransDto;
import com.code.baseservice.entity.ZfAgent;
import com.code.baseservice.entity.ZfAgentTrans;
import com.code.baseservice.service.ZfAgentService;
import com.code.baseservice.service.ZfAgentTransService;
import com.code.baseservice.util.DateUtil;
import com.code.frontapi.util.TokenUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Api(tags = "用户报表", description = "流水api")
@Slf4j
@RestController
@RequestMapping(value = "/user/trans")
public class TransController {
    @Autowired
    private  ZfAgentTransService zfAgentTransService;

    @Autowired
    ZfAgentService zfAgentService;

    @Autowired
    TokenUtil tokenUtil;

    @PostMapping(value ={"/index"})
    @ResponseBody
    public PageInfo<ZfAgentTrans> index(@RequestBody QueryTransDto queryTransDto, HttpServletRequest request){
        ResponseResult responseResult = new ResponseResult();
        try {
            // Set default start and end times if not provided
            log.info("获取用户流水 {}", queryTransDto);
            String token  =   request.getHeader("token");
            String account  = tokenUtil.parseToken(token).get("loginName");
            ZfAgent zfAgent = zfAgentService.queryByAcount(account);
            queryTransDto.setAgentId(zfAgent.getAgentId());
            PageHelper.startPage(queryTransDto.getPageNum(), queryTransDto.getPageSize());
            List<ZfAgentTrans> zfAgentTrans = zfAgentTransService.queryByQueryTrans(queryTransDto);
            log.info("获取用户流水 {}", queryTransDto);
            return new PageInfo<>(zfAgentTrans);
        }catch (BaseException e){
            //其他非法异常，重新上传;
        }catch (Exception e){
            log.error("获取用户流水异常 ", e);
        }
        return new PageInfo<>();
    }
}
