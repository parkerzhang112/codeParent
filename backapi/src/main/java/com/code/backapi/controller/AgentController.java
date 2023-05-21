package com.code.backapi.controller;

import com.code.baseservice.base.enums.ResultEnum;
import com.code.baseservice.base.exception.BaseException;
import com.code.baseservice.dto.ResponseResult;
import com.code.baseservice.dto.backapi.OperaAgentParams;
import com.code.baseservice.dto.backapi.OperaBalanceParams;
import com.code.baseservice.service.ZfAgentService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@Api(tags = "代理", description = "订单操作 API")
@RequestMapping("/agent")
@Slf4j
public class AgentController {

    @Autowired
    private ZfAgentService zfAgentService;

    /***
     * 商户补分上分操作
     * @param operaBalanceParams
     * @return
     */
    @PostMapping("/operatBalance")
    @ResponseBody
    public String operatBalance(@RequestBody OperaAgentParams operaAgentParams) {
        ResponseResult responseResult = new ResponseResult();
        try {
            zfAgentService .operatBalance(operaAgentParams);
            return responseResult.toJsonString();
        } catch (BaseException e) {
            responseResult.setCode(e.getCode()).setMsg(e.getMessage());
        } catch (Exception e) {
            log.error("系统异常", e);
            responseResult.setCode(ResultEnum.ERROR.getCode()).setMsg("系统异常");
        }
        return responseResult.toJsonString();
    }
}
