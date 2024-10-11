package com.code.payapi.controller;

import com.alibaba.fastjson.JSONObject;
import com.code.baseservice.base.enums.ResultEnum;
import com.code.baseservice.base.exception.BaseException;
import com.code.baseservice.dto.payapi.RechareParams;
import com.code.baseservice.entity.ZfAgent;
import com.code.baseservice.entity.ZfCode;
import com.code.baseservice.entity.ZfMerchant;
import com.code.baseservice.entity.ZfRecharge;
import com.code.baseservice.service.*;
import com.code.baseservice.util.Telegram;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    @Autowired
    ZfAgentService zfAgentService;


    @Autowired
    ZfMerchantService zfMerchantService;

    // 定义正则表达式
    String regex = "[a-zA-Z0-9]+";  // 匹配大小写字母和数字的子字符串


    // 编译正则表达式
    Pattern pattern = Pattern.compile(regex);



    @ApiOperation("创建消息")
    @PostMapping("/create")
    @ResponseBody
    public String create(@RequestBody  Map<String, Object> map) {
        try {
            log.info("接受消息 纸飞机消息  {}", map.toString());
            JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(map));
            String message =  jsonObject.getJSONObject("message").getString("text");
            Long chatid =  jsonObject.getJSONObject("message").getJSONObject("chat").getLong("id");
            Long messageId = jsonObject.getJSONObject("message").getLong("message_id");
            Telegram telegram = new Telegram();
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
                rechareParams.setPay_type(12);
                zfRechargeService.createOrderByTelegram(rechareParams, account);
            }
            if (jsonObject.getJSONObject("message").getString("text").contains("/help")) {
                log.info("开始执行 help 命令");
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("【机器人使用手册】\n");
                stringBuilder.append("代理方使用 \n");
                stringBuilder.append("代理绑定： 绑定代理 后台登录名 代理飞机用户名\n");
                stringBuilder.append("例如： 绑定代理-苹果-pingguo\n");

                stringBuilder.append("商户绑定: 绑定商户-商户名称\n");
                stringBuilder.append("通知内容：绑定商户-腾飞\n");
                stringBuilder.append("查单操作： 查单 单号+凭证\n");
                telegram.sendCommon(chatid.toString(), stringBuilder.toString(), "", messageId.toString());
            }
            if (jsonObject.getJSONObject("message").getString("text").contains("绑定代理")) {
                List<String> infos = Arrays.asList( message.split("-"));
                if(infos.size() != 3){
                    telegram.sendCommon(chatid.toString(),"绑定格式错误:  绑定代理 代理用户名 纸飞机用户名", "", messageId.toString());
                    return null;
                }
                String agentName = infos.get(1);
                String username = infos.get(2);
                ZfAgent zfAgent = zfAgentService.queryByAccount(agentName);
                if(zfAgent == null){
                    telegram.sendCommon(chatid.toString(),"不存在代理用户:"+ agentName, "", messageId.toString());
                }
                zfAgent.setTelegramUsername(username);
                zfAgent.setGroupId(chatid);
                zfAgentService.update(zfAgent);
                telegram.sendCommon(chatid.toString(),"绑定群代理成功", username, messageId.toString());
            }
            if (jsonObject.getJSONObject("message").getString("text").contains("绑定商户")) {
                List<String> infos = Arrays.asList( message.split("-"));
                String merchantName = infos.get(1);
                ZfMerchant zfMerchant = zfMerchantService.queryByName(merchantName);
                if(zfMerchant == null){
                    telegram.sendCommon(chatid.toString(),"不存在商户:"+ merchantName, "" , messageId.toString());
                    return null;
                }
                zfMerchant.setGroupId(chatid);
                zfMerchantService.update(zfMerchant);
                telegram.sendCommon(chatid.toString(),"绑定群商户成功", "", messageId.toString());
            }
            if (jsonObject.getJSONObject("message").getString("text").contains("查单")) {
                // 待匹配的字符串
                // 创建匹配器对象
                Matcher matcher = pattern.matcher(message);
                String merchantOrderNo =  matcher.group();
                if(!StringUtils.isNotBlank(merchantOrderNo)){
                    telegram.sendCommon(chatid.toString(),"订单不存在:"+ merchantOrderNo, "", messageId.toString());
                    return null;
                }

                ZfRecharge zfRecharge  = zfRechargeService.queryByMerchantOrderNo(merchantOrderNo);
                ZfAgent zfAgent =  zfAgentService.findGroupByAgent(zfRecharge.getAgentId());
                if(zfAgent == null){
                    telegram.sendCommon(chatid.toString(),"代理群未绑定:"+ zfAgent.getName(), "", messageId.toString());
                    return null;
                }
                telegram.sendCommon(zfAgent.getGroupId().toString(), message, zfAgent.getTelegramUsername(), messageId.toString());
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
