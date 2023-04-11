package com.code.baseservice.util;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *  内容提取
 * @author shadow
 */
public class TransBaseUtil {

    public static final Logger logger = LoggerFactory.getLogger(BankCaseUtil.class);


    //工商银行
    public static final Pattern ICBC_Card4End = Pattern.compile("您尾号(.*?)卡");
    public static final Pattern ICBC_TransAccount = Pattern.compile("\\)(.*?)元，");
    public static final Pattern ICBC_TransTime = Pattern.compile("卡(.*?):");
    public static final Pattern ICBC_Balance = Pattern.compile("卡(.*?):");
    public static final Pattern ICBC_TransType = Pattern.compile("卡(.*?):");
    public static final Pattern ICBC_Remark = Pattern.compile("卡(.*?):");

    /**
     * @描述:获取正则内容
     * @作者:nada
     * @时间:2019/4/17
     **/
    public static String getContent(String content, Pattern pattern){
        StringBuffer sb = new StringBuffer();
        Matcher m = pattern.matcher(content);
        while (m.find()) {
            int i = 1;
            sb.append(m.group(i));
            i++;
        }
        return sb.toString();
    }

    public static final Pattern TAOBAO_NAME = Pattern.compile("您已成功订购(.*?)提供");
    public static final Pattern TAOBAO_MARK = Pattern.compile("验证码(.*?).您");
    /**
     * @desc 获取淘宝内容
     * @author nada
     * @create 2019/9/6 11:24
     */
    public static JSONObject getTaobaoInfo(String smsContent){
        try {
            JSONObject taobaoInfo = new JSONObject();
            String name = BankCaseUtil.getContent(smsContent, BankCaseUtil.TAOBAO_NAME);
            String mark = BankCaseUtil.getContent(smsContent, BankCaseUtil.TAOBAO_MARK);
            taobaoInfo.put("name",name);
            taobaoInfo.put("mark",mark);
            return taobaoInfo;
        } catch (Exception e) {
            logger.error("解析失败",e);
            return null;
        }
    }
}
