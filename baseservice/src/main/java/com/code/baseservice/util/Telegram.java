package com.code.baseservice.util;

import com.alibaba.fastjson.JSONObject;
import com.code.baseservice.dto.AgentConfig;
import com.code.baseservice.dto.payapi.RechareParams;
import com.code.baseservice.entity.*;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class Telegram {
    public void  sendMesaage(Map<String, Object> map, String url){
        try {
            HttpClientUtil.doPostJson(url, JSONObject.toJSONString(map));
        }catch (Exception e){
            log.error("发送纸飞机消息失败 {}", e);
        }
    }

    public void sendWarrnSmsMessage(ZfRecharge zfRecharge, String notice) {
        try {
            String chatId= "-921812639";
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("【短信消息预警】\n");
            stringBuilder.append("预警原因："+notice + " \n");
            stringBuilder.append("商户：" + zfRecharge.getRemark()+"\n");
            stringBuilder.append("金额：" + zfRecharge.getPayAmount()+"\n");
            String url = "https://api.telegram.org/bot6431542163:AAGa41xGC44flApg5K_oV8todOOEscK1uFc/sendMessage";
            Map<String, Object> map = new HashMap<>();
            map.put("chat_id", chatId);
            map.put("c", stringBuilder.toString());
            sendMesaage(map, url);
        }catch (Exception e){
            log.error("纸飞机发送消息异常 {}", e.getStackTrace());
        }
    }


    public void sendWarrnThirdMessage(RechareParams rechareParams, ZfChannel zfChannel, String notice) {
        try {
            String chatId= "-4128575462";
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("【短信消息预警】\n");
            stringBuilder.append("预警原因："+notice + " \n");
            stringBuilder.append("商户：" + zfChannel.getChannelName()+"\n");
            stringBuilder.append("金额：" + rechareParams.getPay_amount()+"\n");
            stringBuilder.append("单号：" + rechareParams.getMerchant_order_no()+"\n");
            String url = "https://api.telegram.org/bot6431542163:AAGa41xGC44flApg5K_oV8todOOEscK1uFc/sendMessage";
            Map<String, Object> map = new HashMap<>();
            map.put("chat_id", chatId);
            map.put("text", stringBuilder.toString());
            sendMesaage(map, url);
        }catch (Exception e){
            log.error("纸飞机发送消息异常 {}", e.getStackTrace());
        }
    }

    public void sendMerchantBalanceMessage(ZfMerchant xMerchant, ZfRecharge zfRecharge, String config) {
        try {
            String chatId= "-921812639";
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
        }catch (Exception e){
            log.error("消息发送异常");
        }

    }

    /**
     * 入款调单通知
     */
    public void sendWarrnSmsMessage(ZfRecharge zfRecharge, String notice, String config){

        String chatId= "-982175337";
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("【短信消息预警】\n");
        stringBuilder.append("预警原因："+notice + " \n");
        stringBuilder.append("商户id：" + zfRecharge.getMerchantId() + "\n");
        stringBuilder.append("订单号:" + zfRecharge.getMerchantOrderNo() + "\n");
        stringBuilder.append("订单金额：" + zfRecharge.getPayAmount() + "\n");
        stringBuilder.append("查询码："+config );
        String url = "https://api.telegram.org/bot5569136092:AAGhxaVuxYlKmy2uqZP9RBbCh0PlBCmDJsI/sendMessage";
        Map<String, Object> map = new HashMap<>();
        map.put("chat_id", chatId);
        map.put("text", stringBuilder.toString());
        sendMesaage(map, url);
    }

    /**
     * 系统异常
     */
    public void sendWarrnException(ZfRecharge zfRecharge, String notice){
        String chatId= "-1001859874119";
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("【短信消息预警】\n");
        stringBuilder.append("预警原因："+notice + " \n");
        stringBuilder.append("预警单号：" + zfRecharge.getMerchantOrderNo() + "\n");
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
        String chatId= "-921812639";
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

    public void sendWarrnCodeClient(String notice, ZfCode zfCode, String config) {
        String chatId= "-921812639";
        if(StringUtils.isNotEmpty(config)){
            AgentConfig memberConfig = JSONObject.parseObject(config,AgentConfig.class);
            if(StringUtils.isNotEmpty(memberConfig.getDispacthOrderGroup())){
                chatId = memberConfig.getDispacthOrderGroup();
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("预警原因：" + notice);
        stringBuilder.append("支付宝账号：" + zfCode.getAccount()+"  ");
        stringBuilder.append("ip:" + zfCode.getIp());
        String url = "https://api.telegram.org/bot5569136092:AAGhxaVuxYlKmy2uqZP9RBbCh0PlBCmDJsI/sendMessage";
        Map<String, Object> map = new HashMap<>();
        map.put("chat_id",chatId);
        map.put("text", stringBuilder.toString());
        sendMesaage(map, url);
    }

    public void sendWarrnMessageByNoMatch(ZfTransRecord zfTransRecord, ZfCode zfCode) {
        String chatId= "-921812639";
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("预警原因：未上分流水" );
        stringBuilder.append("支付宝账号：" + zfCode.getName()+" \n");
        stringBuilder.append("金额:" + zfTransRecord.getAmount());
        String url = "https://api.telegram.org/bot5569136092:AAGhxaVuxYlKmy2uqZP9RBbCh0PlBCmDJsI/sendMessage";
        Map<String, Object> map = new HashMap<>();
        map.put("chat_id",chatId);
        map.put("text", stringBuilder.toString());
        sendMesaage(map, url);
    }
}
