package com.code.payapi.controller;

import com.alibaba.fastjson.JSONObject;
import com.code.baseservice.base.enums.ResultEnum;
import com.code.baseservice.base.exception.BaseException;
import com.code.baseservice.dto.payapi.RechareParams;
import com.code.baseservice.entity.ZfCode;
import com.code.baseservice.service.ZfCodeService;
import com.code.baseservice.service.ZfMerchantTransService;
import com.code.baseservice.service.ZfRechargeService;
import com.code.baseservice.util.Telegram;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
@RequestMapping("/hooks")
public class TelegramWebhooksController {

    @Autowired
    ZfRechargeService zfRechargeService;

    @Autowired
    ZfCodeService zfCodeService;

    @Autowired
    ZfMerchantTransService zfMerchantTransService;


    @ApiOperation("创建消息")
    @PostMapping("/create")
    @ResponseBody
    public String create(@RequestBody  Map<String, Object> map) {
        try {
            log.info("接受消息 纸飞机消息  {}", map.toString());
            JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(map));
            if (null == jsonObject.getJSONObject("message").getString("text")) {
                return "True";
            }
            if (jsonObject.getJSONObject("message").getString("text").contains("您有一笔新的充值订单，请及时处理")) {
                //创建订单
                String text = jsonObject.getJSONObject("message").getString("text");
                log.info("接受消息  {}", text);

                List<String> infos = Arrays.asList(text.split("\\n"));
                RechareParams rechareParams = new RechareParams();
                rechareParams.setMerchant_id(895480);
                String merchantOrderId = Arrays.asList(infos.get(1).split("：")).get(1);
                rechareParams.setMerchant_order_no(merchantOrderId);
                String name = Arrays.asList(infos.get(6).split("：")).get(1);
                rechareParams.setName(name);
                String amount = Arrays.asList(infos.get(7).split("：")).get(1);
                rechareParams.setPay_amount(new BigDecimal(amount));
                String account = Arrays.asList(infos.get(4).split("：")).get(1);
                rechareParams.setNotify_url("http://127.0.0.1");
                zfRechargeService.createOrderByTelegram(rechareParams, account);
            }
            if (jsonObject.getJSONObject("message").getString("text").contains("获取在线码")) {
                String text = jsonObject.getJSONObject("message").getString("text");
                log.info("接受消息  {}", text);
                //创建订单
                List<ZfCode> zfCodes = zfCodeService.queryGroupList(895480);
                if(zfCodes.size() == 0){
                    throw new BaseException(ResultEnum.NO_CODE);
                }
                String s = "";
                for (ZfCode zfCode : zfCodes) {
                    s = s.concat(zfCode.getAccount() + " " + zfCode.getName() + "\n");
                }
                Telegram telegram = new Telegram();
                telegram.sendCode(s);
            }
        }catch (BaseException b){
            Telegram telegram = new Telegram();
            telegram.sendError(b.getMessage());
        }catch (Exception e){
            log.error("系统异常 ",e);
            return "True";
        }

      return "True";
    }

    public static void main(String[] args) {
        String text = "*您有一笔新的充值订单，请及时处理\n" +
                "订单号：04221957400712647\n" +
                "账户类型：支付宝\n" +
                "收款人：王婷婷\n" +
                "收款账号：15250742177\n" +
                "------------------------------------\n" +
                "用户姓名：蒋峰\n" +
                "充值金额：10000";
        List<String> infos =  Arrays.asList(text.split("\\n"));
        RechareParams  rechareParams = new RechareParams();
        rechareParams.setMerchant_id(895480);
        String merchantOrderId = Arrays.asList(infos.get(1).split("：")).get(1);
        rechareParams.setMerchant_order_no(merchantOrderId);
        String name = Arrays.asList(infos.get(6).split("：")).get(1);
        rechareParams.setName(name);
        String amount = Arrays.asList(infos.get(7).split("：")).get(1);
        rechareParams.setPay_amount(new BigDecimal(amount));
        String account = Arrays.asList(infos.get(4).split("：")).get(1);

    }

}
