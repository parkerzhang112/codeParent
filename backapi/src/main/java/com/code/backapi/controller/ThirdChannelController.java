package com.code.backapi.controller;

import com.code.baseservice.base.enums.ResultEnum;
import com.code.baseservice.base.exception.BaseException;
import com.code.baseservice.dto.ResponseResult;
import com.code.baseservice.dto.backapi.OperaChannelParams;
import com.code.baseservice.service.ZfChannelService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@Api(tags = "三方渠道", description = "三方渠道 API")
@RequestMapping("/channel")
@Slf4j
public class ThirdChannelController {

    @Autowired
    private ZfChannelService zfChannelService;

    /***
     * 代理补分上分操作
     * @param operaBalanceParams
     * @return
     */
    @PostMapping("/operatBalance")
    @ResponseBody
    public String operatBalance(@RequestBody OperaChannelParams operaChannelParams) {
        ResponseResult responseResult = new ResponseResult();
        try {
            log.info("开始操作渠道余额，{}", operaChannelParams);
            zfChannelService .operatBalance(operaChannelParams);
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
