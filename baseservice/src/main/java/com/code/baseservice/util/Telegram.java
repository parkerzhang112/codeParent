package com.code.baseservice.util;

import com.alibaba.fastjson.JSONObject;
import com.code.baseservice.dto.AgentConfig;
import com.code.baseservice.dto.payapi.RechareParams;
import com.code.baseservice.entity.ZfCode;
import com.code.baseservice.entity.ZfMerchant;
import com.code.baseservice.entity.ZfRecharge;
import com.code.baseservice.entity.ZfWithdraw;
import lombok.extern.slf4j.Slf4j;
import sun.management.Agent;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class Telegram {
    public void  sendMesaage(Map<String, Object> map, String url){
        HttpClientUtil.doPostJson(url, JSONObject.toJSONString(map));
    }

    public void sendWarrnSmsMessage(ZfRecharge zfRecharge, String notice) {
        try {
            String chatId= "-629074970";
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("【短信消息预警】\n");
            stringBuilder.append("预警原因："+notice + " \n");
            stringBuilder.append("商户：" + zfRecharge.getRemark()+"\n");
            stringBuilder.append("金额：" + zfRecharge.getPayAmount()+"\n");
            String url = "https://api.telegram.org/bot5569136092:AAGhxaVuxYlKmy2uqZP9RBbCh0PlBCmDJsI/sendMessage";
            Map<String, Object> map = new HashMap<>();
            map.put("chat_id", chatId);
            map.put("text", stringBuilder.toString());
            sendMesaage(map, url);
        }catch (Exception e){
            log.error("纸飞机发送消息异常 {}", e.getStackTrace());
        }

    }

    public void sendMerchantBalanceMessage(ZfMerchant xMerchant, ZfRecharge zfRecharge, String config) {
        String chatId= "-810520302";
        if(StringUtils.isNotEmpty(config)){
            AgentConfig agentConfig = JSONObject.parseObject(config,AgentConfig.class);
            if(StringUtils.isNotEmpty(agentConfig.getExceptionTrans())){
                chatId = agentConfig.getExceptionTrans();
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("【短信消息预警】\n");
        stringBuilder.append( "商户余额预警 \n");
        stringBuilder.append("当前余额" + xMerchant.getBalance().add(zfRecharge.getPaidAmount())+"\n");
        String url = "https://api.telegram.org/bot5569136092:AAGhxaVuxYlKmy2uqZP9RBbCh0PlBCmDJsI/sendMessage";
        Map<String, Object> map = new HashMap<>();
        map.put("chat_id",chatId);
        map.put("text", stringBuilder.toString());
        sendMesaage(map, url);
    }

    /**
     * 入款调单通知
     */
    public void sendWarrnSmsMessage(ZfCode zfCode, String notice, String config){
        String chatId= "-629074970";
        if(StringUtils.isNotEmpty(config)){
            AgentConfig agentConfig = JSONObject.parseObject(config,AgentConfig.class);
            if(StringUtils.isNotEmpty(agentConfig.getCommonNotice())){
                chatId = agentConfig.getExceptionTrans();
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("【短信消息预警】\n");
        stringBuilder.append("预警原因："+notice + " \n");
        stringBuilder.append("二维码：" + zfCode.getAccount().substring(zfCode.getAccount().length()-4)+"\n");
        stringBuilder.append("余额：" + zfCode.getBalance()+"\n");
        String url = "https://api.telegram.org/bot5569136092:AAGhxaVuxYlKmy2uqZP9RBbCh0PlBCmDJsI/sendMessage";
        Map<String, Object> map = new HashMap<>();
        map.put("chat_id", chatId);
        map.put("text", stringBuilder.toString());
        sendMesaage(map, url);
    }
    /**
     * 入款调单通知
     */
    public void sendWarrnWithdraw( ZfWithdraw zfWithdraw, String notice, String config){
        String chatId= "-1001858921015";
        if(StringUtils.isNotEmpty(config)){
            AgentConfig memberConfig = JSONObject.parseObject(config,AgentConfig.class);
            if(StringUtils.isNotEmpty(memberConfig.getDispacthOrderGroup())){
                chatId = memberConfig.getDispacthOrderGroup();
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("" + zfWithdraw.getPayAmount().toBigInteger()+"  ");
        stringBuilder.append("" + zfWithdraw.getCardName()+"  ");
        stringBuilder.append("" + zfWithdraw.getCardType()+"  ");
        stringBuilder.append("" + zfWithdraw.getCardAccount()+"  ");
        stringBuilder.append("" + zfWithdraw.getCardAddress()+"  ");
        stringBuilder.append("" + notice);
        String url = "https://api.telegram.org/bot5569136092:AAGhxaVuxYlKmy2uqZP9RBbCh0PlBCmDJsI/sendMessage";
        Map<String, Object> map = new HashMap<>();
        map.put("chat_id",chatId);
        map.put("text", stringBuilder.toString());
        sendMesaage(map, url);
    }
}
