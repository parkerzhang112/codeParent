package com.code.autoapi;

import com.xiagao.baseservice.dto.api.SmsParms;
import com.xiagao.baseservice.service.SmsService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SmsControllerTest extends  AutoapiApplicationTests{

    private String match= "(([1-9]{1}\\d*))(\\.\\d{1,2})";
    private String abcmatch= "[】]{1}[\\s\\S]{0,10}[于]";


    @Autowired
    private SmsService smsService;

    @Test
    public void testICBCParse(){
        String content = "您的借记卡账户长城电子借记卡，于09月17日收入(网银跨行)人民币100.00元,交易后余额1250.10【中国银行】";
        if(content.contains("收入")){
            Pattern pattern= Pattern.compile("[于]"); // 判断小数点后2位的数字的正则表达式
            Matcher match=pattern.matcher(content);
            String  a = "";
            List<String> list = new ArrayList<>();
            while (match.find()){
                System.out.print(match.group());
            }
        }

    }

    @Test
    public void testABCparse(){
        String content = "【中国农业银行】尊敬的客户：您尾号5670的农行账户的消息服务注册信息已于2021年05月04日14时33分成功变更！\n" +
                "【中国农业银行】王乃钊于05月04日14:34向您尾号5670账户完成转存交易人民币5000.00，余额5000.33。";
        Pattern pattern= Pattern.compile(match); // 判断小数点后2位的数字的正则表达式
        Matcher match=pattern.matcher(content);
        String  a = "";
        List<String> list = new ArrayList<>();
        while (match.find()){
            list.add(match.group());
        }
        Pattern pattern1= Pattern.compile(abcmatch); // 判断小数点后2位的数字的正则表达式
        Matcher match1 = pattern1.matcher(content);
        String  a1 = "";
        List<String> list1 = new ArrayList<>();
        while (match1.find()){
            String name =  match1.group();
            name = name.replace("】", "");
            name = name.replace("于", "");
            System.out.print("匹配人人名"+name);
        }
    }

    @Test
    public void testIcbcparse(){
        String content = "您尾号4699卡5月6日13:27手机银行支出(跨行汇款)1,500元，余额6113.20元，对方户名：窦武，对方账户尾号 5476。【工商银行】";
        Pattern pattern= Pattern.compile(match); // 判断小数点后2位的数字的正则表达式
        Matcher match=pattern.matcher(content);
        String  a = "";
        List<String> list = new ArrayList<>();
        while (match.find()){
            list.add(match.group());
        }
        Pattern pattern1= Pattern.compile(abcmatch); // 判断小数点后2位的数字的正则表达式
        Matcher match1 = pattern1.matcher(content);
        String  a1 = "";
        List<String> list1 = new ArrayList<>();
        while (match1.find()){
            String name =  match1.group();
            name = name.replace("】", "");
            name = name.replace("于", "");
            System.out.print("匹配人人名"+name);
        }
    }

    @Test
    public void  testUpload(){
        SmsParms smsParms = new SmsParms();
        String content = "您的借记卡账22户长22城电子借记卡，于09月17日收入(网银跨行)人民币1016.00元,交易后余额1058.10【中国银行】";
        System.out.print(content.substring(0,30));
        smsParms.setContent(content);
        smsParms.setSendTime(new Date());
        smsParms.setBankType("BOC");
        smsParms.setBankid(48);
        smsParms.setBankNum("6217857500045507925");
        smsService.upload(smsParms);
    }
}
