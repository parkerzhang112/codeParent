package com.code.baseservice.util;

import com.alibaba.fastjson.JSONObject;
import com.xiagao.baseservice.dto.MemberConfig;
import com.xiagao.baseservice.dto.api.ClientstatusParams;
import com.xiagao.baseservice.dto.api.RechareParams;
import com.xiagao.baseservice.entity.*;

import java.util.HashMap;
import java.util.Map;

public class Telegram {
    public void  sendMesaage(Map<String, Object> map, String url){
        HttpClientUtil.doPostJson(url, JSONObject.toJSONString(map));
    }

    /**
     * 入款调单通知
     */
    public void sendWarrnRechargeMessage(RechareParams rechareParams, String notice){

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("【存款消息预警】\n");
        stringBuilder.append("预警原因："+notice + " \n");
        stringBuilder.append("金额：" + rechareParams.getPay_amount()+"\n");
        stringBuilder.append("存款姓名：" + rechareParams.getName()+"\n");
        stringBuilder.append("商户订单号：" + rechareParams.getMerchant_order_no());
        String url = "https://api.telegram.org/bot1911676399:AAGapaytGEhECheDvWJjpnLOVw4AZPtaSaU/sendMessage";
        Map<String, Object> map = new HashMap<>();
        map.put("chat_id", -538961255);
        map.put("text", stringBuilder.toString());
        sendMesaage(map, url);
    }

    /**
     * 入款调单通知
     */
    public void sendWarrnSmsMessage(XRecharge xRecharge, String notice){

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("【短信消息预警】\n");
        stringBuilder.append("预警原因："+notice + " \n");
        stringBuilder.append("金额：" + xRecharge.getPayAmount()+"\n");
        stringBuilder.append("存款姓名：" + xRecharge.getName()+"\n");
        stringBuilder.append("商户订单号：" + xRecharge.getMerchantOrderNo());
        String url = "https://api.telegram.org/bot5569136092:AAGhxaVuxYlKmy2uqZP9RBbCh0PlBCmDJsI/sendMessage";
        Map<String, Object> map = new HashMap<>();
        map.put("chat_id", -629074970);
        map.put("text", stringBuilder.toString());
        sendMesaage(map, url);
    }


    /**
     * 卡状态截图通知
     */
    public void sendCardStatus(String config ,String notice){
        String chatId = "-1001858921015";
        if(StringUtils.isNotEmpty(config)){
            MemberConfig memberConfig = JSONObject.parseObject(config,MemberConfig.class);
            if(StringUtils.isNotEmpty(memberConfig.getDispacthOrderGroup())){
                chatId = memberConfig.getDispacthOrderGroup();
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("【短信消息预警】\n");
        stringBuilder.append("预警原因："+notice + " \n");
        String url = "https://api.telegram.org/bot5569136092:AAGhxaVuxYlKmy2uqZP9RBbCh0PlBCmDJsI/sendMessage";
        Map<String, Object> map = new HashMap<>();
        map.put("chat_id",chatId);
        map.put("text", stringBuilder.toString());
        sendMesaage(map, url);
    }

    /**
     * 入款调单通知
     */
    public void sendWarrnBug(XCard xCard, String notice){

        StringBuilder stringBuilder = new StringBuilder();        stringBuilder.append("异常名称：" + xCard.getCardName()+"\n");
        stringBuilder.append("异常收款卡请注意\n");
        stringBuilder.append("异常名称：" + xCard.getCardName()+"\n");
        stringBuilder.append("异常卡号：" + xCard.getCardNum());
        String url = "https://api.telegram.org/bot5569136092:AAGhxaVuxYlKmy2uqZP9RBbCh0PlBCmDJsI/sendMessage";
        Map<String, Object> map = new HashMap<>();
        map.put("chat_id", -815813448);
        map.put("text", stringBuilder.toString());
        sendMesaage(map, url);
    }

    /**
     * 入款调单通知
     */
    public void sendWarrnSmsMessage( XTransfer xTransfer, String notice, String config){
        String chatId= "-1001858921015";
        if(StringUtils.isNotEmpty(config)){
            MemberConfig memberConfig = JSONObject.parseObject(config,MemberConfig.class);
            if(StringUtils.isNotEmpty(memberConfig.getDispacthOrderGroup())){
                chatId = memberConfig.getDispacthOrderGroup();
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("" + xTransfer.getPayAmount().toBigInteger()+"  ");
        stringBuilder.append("" + xTransfer.getCardName()+"  ");
        stringBuilder.append("" + xTransfer.getCardType()+"  ");
        stringBuilder.append("" + xTransfer.getCardAccount()+"  ");
        stringBuilder.append("" + xTransfer.getCardAddress()+"  ");
        stringBuilder.append("" + notice);
        String url = "https://api.telegram.org/bot5569136092:AAGhxaVuxYlKmy2uqZP9RBbCh0PlBCmDJsI/sendMessage";
        Map<String, Object> map = new HashMap<>();
        map.put("chat_id",chatId);
        map.put("text", stringBuilder.toString());
        sendMesaage(map, url);
    }



    /**
     * 入款调单通知
     */
    public void sendWarrnSmsMessage(XCard xCard, String notice,String config){
        String chatId= "-629074970";
        if(StringUtils.isNotEmpty(config)){
            MemberConfig memberConfig = JSONObject.parseObject(config,MemberConfig.class);
            if(StringUtils.isNotEmpty(memberConfig.getCommonNotice())){
                chatId = memberConfig.getExceptionTrans();
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("【短信消息预警】\n");
        stringBuilder.append("预警原因："+notice + " \n");
        stringBuilder.append("卡号：" + xCard.getCardNum()+"\n");
        stringBuilder.append("卡主：" + xCard.getCardName());
        stringBuilder.append("余额：" + xCard.getBalance()+"\n");
        String url = "https://api.telegram.org/bot5569136092:AAGhxaVuxYlKmy2uqZP9RBbCh0PlBCmDJsI/sendMessage";
        Map<String, Object> map = new HashMap<>();
        map.put("chat_id", chatId);
        map.put("text", stringBuilder.toString());
        sendMesaage(map, url);
    }


    /**
     * 入款调单通知
     */
    public void sendWarrnSmsMessage(XTrans xTrans, String notice, String config){
        String chatId= "-810520302";
        if(StringUtils.isNotEmpty(config)){
            MemberConfig memberConfig = JSONObject.parseObject(config,MemberConfig.class);
            if(StringUtils.isNotEmpty(memberConfig.getCommonNotice())){
                chatId = memberConfig.getCommonNotice();
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("【短信消息预警】\n");
        stringBuilder.append("预警原因："+notice + " \n");
        stringBuilder.append("金额：" + xTrans.getAmout()+"\n");
        stringBuilder.append("存款姓名：" + xTrans.getName()+"\n");
        String url = "https://api.telegram.org/bot5569136092:AAGhxaVuxYlKmy2uqZP9RBbCh0PlBCmDJsI/sendMessage";
        Map<String, Object> map = new HashMap<>();
        map.put("chat_id", chatId);
        map.put("text", stringBuilder.toString());
        sendMesaage(map, url);
    }

    /**
     * 入款调单通知
     */
    public void sendWarrnTransMessage(XTrans xTrans, String notice, String config){
        String chatId= "-810520302";
        if(StringUtils.isNotEmpty(config)){
            MemberConfig memberConfig = JSONObject.parseObject(config,MemberConfig.class);
            if(StringUtils.isNotEmpty(memberConfig.getExceptionTrans())){
                chatId = memberConfig.getExceptionTrans();
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("【短信消息预警】\n");
        stringBuilder.append(notice + " \n");
        stringBuilder.append("金额：" + xTrans.getAmout()+"\n");
        stringBuilder.append("存款姓名：" + xTrans.getName()+"\n");
        String url = "https://api.telegram.org/bot5569136092:AAGhxaVuxYlKmy2uqZP9RBbCh0PlBCmDJsI/sendMessage";
        Map<String, Object> map = new HashMap<>();
        map.put("chat_id",chatId);
        map.put("text", stringBuilder.toString());
        sendMesaage(map, url);
    }

    public static void main(String[] args) {
        String url = "https://api.telegram.org/bot5569136092:AAGhxaVuxYlKmy2uqZP9RBbCh0PlBCmDJsI/sendMessage";
        Map<String, Object> map = new HashMap<>();
        map.put("chat_id","-1001858921015");
        map.put("text", "11");
        HttpClientUtil.doPostJson(url, JSONObject.toJSONString(map));
    }

    public void sendMerchantBalanceMessage(XMerchant xMerchant, XRecharge xRecharge, String config) {
        String chatId= "-810520302";
        if(StringUtils.isNotEmpty(config)){
            MemberConfig memberConfig = JSONObject.parseObject(config,MemberConfig.class);
            if(StringUtils.isNotEmpty(memberConfig.getExceptionTrans())){
                chatId = memberConfig.getExceptionTrans();
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("【短信消息预警】\n");
        stringBuilder.append( "商户余额预警 \n");
        stringBuilder.append("当前余额" + xMerchant.getBalance().add(xRecharge.getPaidAmount())+"\n");
        String url = "https://api.telegram.org/bot5569136092:AAGhxaVuxYlKmy2uqZP9RBbCh0PlBCmDJsI/sendMessage";
        Map<String, Object> map = new HashMap<>();
        map.put("chat_id",chatId);
        map.put("text", stringBuilder.toString());
        sendMesaage(map, url);
    }

    public void sendMesaageByClientStatus(ClientstatusParams clientstatusParams, String config) {
        String chatId= "-815813448";
        if(StringUtils.isNotEmpty(config)){
            MemberConfig memberConfig = JSONObject.parseObject(config,MemberConfig.class);
            if(StringUtils.isNotEmpty(memberConfig.getExceptionTrans())){
                chatId = memberConfig.getExceptionTrans();
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("【客户端微信监控消息】\n");
        stringBuilder.append( "微信状态预警 \n");

        stringBuilder.append("微信号："+clientstatusParams.getWx_account()+"" +
                "\n");
        stringBuilder.append("卡："+clientstatusParams.getBankaccount()+"" +
                "\n");
        String status = clientstatusParams.getWechat_status() == 2 ?"登录":"退出";
        stringBuilder.append("状态："+status+"" +
                "\n");
        String url = "https://api.telegram.org/bot5569136092:AAGhxaVuxYlKmy2uqZP9RBbCh0PlBCmDJsI/sendMessage";
        Map<String, Object> map = new HashMap<>();
        map.put("chat_id",chatId);
        map.put("text", stringBuilder.toString());
        sendMesaage(map, url);
    }
}
