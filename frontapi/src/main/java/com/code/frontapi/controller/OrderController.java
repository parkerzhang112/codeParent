package com.code.frontapi.controller;

import com.code.baseservice.base.enums.ResultEnum;
import com.code.baseservice.base.exception.BaseException;
import com.code.baseservice.dto.ResponseResult;
import com.code.baseservice.dto.backapi.OperaOrderParams;
import com.code.baseservice.dto.frontapi.order.QueryOrderDto;
import com.code.baseservice.entity.ZfAgent;
import com.code.baseservice.entity.ZfRecharge;
import com.code.baseservice.service.ZfAgentService;
import com.code.baseservice.service.ZfRechargeService;
import com.code.frontapi.util.TokenUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Api(tags = "订单操作", description = "流水api")
@Slf4j
@RestController
@RequestMapping(value = "/user/order")
public class OrderController {

    @Autowired
    ZfRechargeService zfRechargeService;

    @Autowired
    ZfAgentService zfAgentService;

    @Autowired
    TokenUtil tokenUtil;

    @PostMapping(value ={"/list"})
    @ResponseBody
    public PageInfo<ZfRecharge> list(@RequestBody QueryOrderDto queryOrderDto, HttpServletRequest request){
        try {
            String token  =   request.getHeader("token");
            String account  = tokenUtil.parseToken(token).get("loginName");
            ZfAgent zfAgent = zfAgentService.queryByAcount(account);
            queryOrderDto.setAgentId(zfAgent.getAgentId());
            PageHelper.startPage(queryOrderDto.getPageNum(), queryOrderDto.getPageSize());
            List<ZfRecharge> zfRecharges = zfRechargeService.selectListByApp(queryOrderDto);
            log.info("获取用户二维码 {}", queryOrderDto);
            return new PageInfo<>(zfRecharges);
        }catch (BaseException e){
            //其他非法异常，重新上传;
        }catch (Exception e){
            log.error("获取用户二维码异常 ", e);
        }
        return new PageInfo<>();
    }

    @PostMapping(value ={"/confirm"})
    @ResponseBody
    public String confirm(@RequestBody OperaOrderParams operaOrderParams, HttpServletRequest request){
        ResponseResult responseResult = new ResponseResult();
        try {
            operaOrderParams.setOrderType(1);
            if (new Integer(1).equals(operaOrderParams.getOrderType())) {
                zfRechargeService.confirmOrder(operaOrderParams);
            }
            responseResult.setCode(ResultEnum.SUCCESS.getCode());
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
