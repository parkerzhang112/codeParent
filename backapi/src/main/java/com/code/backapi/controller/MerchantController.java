package com.code.backapi.controller;

import com.alibaba.fastjson.JSONObject;
import com.code.baseservice.base.enums.ResultEnum;
import com.code.baseservice.base.enums.TransTypeEnum;
import com.code.baseservice.base.exception.BaseException;
import com.code.baseservice.dto.ResponseResult;
import com.code.baseservice.dto.backapi.OperaBalanceParams;
import com.code.baseservice.dto.backapi.OperaOrderParams;
import com.code.baseservice.dto.payapi.TransferParams;
import com.code.baseservice.entity.ZfWithdraw;
import com.code.baseservice.service.ZfMerchantService;
import com.code.baseservice.service.ZfRechargeService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(tags = "商户操作", description = "商户操作 API")
@RequestMapping("/merchant")
@Slf4j
public class MerchantController {

    @Autowired
    ZfMerchantService zfMerchantService;

    @Autowired
    ZfRechargeService zfRechargeService;


    /***
     * 商户补分上分操作
     * @param operaBalanceParams
     * @return
     */
    @PostMapping("/operatBalance")
    @ResponseBody
    public String operatBalance(@RequestBody OperaBalanceParams operaBalanceParams) {
        ResponseResult responseResult = new ResponseResult();
        try {
            zfMerchantService .operatBalance(operaBalanceParams);
//            zfRechargeService.operatOrder(operaBalanceParams);
            return responseResult.toJsonString();
        } catch (BaseException e) {
            responseResult.setCode(e.getCode()).setMsg(e.getMessage());
        } catch (Exception e) {
            log.error("系统异常", e);
            responseResult.setCode(ResultEnum.ERROR.getCode()).setMsg("系统异常");
        }
        return responseResult.toJsonString();
    }




    /**
     * 确认订单
     *
     * @return
     */
    @PostMapping("/issue")
    @ResponseBody
    public String issue(@RequestBody TransferParams transParams) {

        log.info("下发参数参数 信息 {}", transParams.toString());
        ResponseResult responseResult = new ResponseResult();
        try {
             zfMerchantService.issue(transParams);
             responseResult.setCode(ResultEnum.SUCCESS.getCode()).setMsg("操作成功");
        }catch (BaseException e){
            responseResult.setCode(e.getCode()).setMsg(e.getMessage());
        }catch (Exception e){
            log.error("系统异常", e);
            responseResult.setCode(ResultEnum.ERROR.getCode()).setMsg("系统异常");
        }
        return responseResult.toJsonString();

    }


}
