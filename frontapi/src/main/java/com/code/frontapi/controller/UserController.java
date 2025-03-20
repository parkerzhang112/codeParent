package com.code.frontapi.controller;

import com.alibaba.fastjson.JSONObject;
import com.code.baseservice.dto.ResponseResult;
import com.code.baseservice.entity.ZfAgent;
import com.code.baseservice.service.ZfAgentService;
import com.code.frontapi.util.TokenUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Api(tags = "用户信息", description = "流水api")
@Slf4j
@RestController
@RequestMapping(value = "/user/info")
public class UserController {

    @Autowired
    ZfAgentService zfAgentService;

    @Autowired
    TokenUtil tokenUtil;

    @ApiOperation(value = "查询代理信息")
    @PostMapping(value = "/agent")
    @ResponseBody
    public String getAgentInfo(HttpServletRequest request) {
        ResponseResult responseResult = new ResponseResult();
        try {
            String token  =   request.getHeader("token");
            String account  = tokenUtil.parseToken(token).get("loginName");
            ZfAgent zfAgent = zfAgentService.queryByAcount(account);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("currentIncome", zfAgent.getBalance());
            jsonObject.put("availableAmount", zfAgent.getAcceptAmount());
            jsonObject.put("status", zfAgent.getStatus());

            jsonObject.put("totalAmount", zfAgent.getBalance().add(zfAgent.getAcceptAmount()));
            responseResult.setData(jsonObject);
            responseResult.setMsg("操作成功");
        } catch (Exception e) {
            log.error("查询代理信息异常", e);
            responseResult.setMsg("操作失败");
        }
        return responseResult.toJsonString();
    }
}
