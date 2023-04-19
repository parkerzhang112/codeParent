package com.code.baseservice.dto.autoapi;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.code.baseservice.base.enums.TransTypeEnum;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Data
@Slf4j
public class TransParams {


    /**
     * 姓名
     */
    @JsonProperty("name")
    private String name= "";

    /**
     * 金额
     */
    @JsonProperty("amout")
    private BigDecimal amout;

    /**
     * 交易类型
     */
    @JsonProperty("transType")
    private Integer transType;

    /**
     * 余额
     */
    @JsonProperty("balance")
    private BigDecimal balance = BigDecimal.ZERO;

    /**
     * 交易账户
     */
    @JsonProperty("transAccout")
    private String transAccout;

    /*
     * 交易时间
     */
    @JsonProperty("transTime")
    private String transTime;

    @JsonProperty("account")
    private String account;


     /**
     * 卡描述 当前余额和银行卡尾号之类的
     */
    @JsonProperty("BankCode")
    private String bankCode;


    /**
     * 卡描述 当前余额和银行卡尾号之类的
     */
    @JsonProperty("banktitle")
    private String bankTitle;

    private String remark = "";

    private JSONArray cardInfos;

    private List<TransParams> transParams = new ArrayList<>();

    /**
     * 卡描述 当前余额和银行卡尾号之类的
     */
    @JsonProperty("xml")
    private String xml;

    /**
     * 卡描述 当前余额和银行卡尾号之类的
     */
    @JsonProperty("txt")
    private String txt;

    /**
     * 机器名称
     */
    @JsonProperty("client_cc")
    private String client_cc;

    private String ip;

    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {
        String txt = "+ 123.12\\t\\tn交易成功";
        StringBuffer sb = new StringBuffer();
        Matcher m = Pattern.compile("([1-9]\\d*\\.?\\d+)|(0\\.\\d*[1-9])|(\\d+)").matcher(txt);
        while (m.find()) {
            int i = 1;
            sb.append(m.group(i));
            i++;
        }
        System.out.printf(sb.toString());
    }

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



    public void parseTxt() {
        Calendar calendar = Calendar.getInstance();
        if("Alipay".equals(bankCode) ){
            String balanceStr = getContent(txt, Pattern.compile("余额(.*?)元"));
            balance = new BigDecimal(balanceStr.replaceAll(" ", ""));
            List<String> infos  =  Arrays.asList(txt.split("\n2023"));
            for (String info: infos) {
                try{
                    if(info.contains("我的支付宝")){
                        continue;
                    }
                    TransParams transParams1 = new TransParams();
                    info = info.replace("\n", "n");
                    List<String> transinfo =  Arrays.asList(info.split("nn"));
                    String time = calendar.get(Calendar.YEAR)+transinfo.get(0).concat(" ").concat(transinfo.get(1));
                    time = time.replaceAll("\\.", "-");
                    transParams1.setTransTime(time);
                    transParams1.setName(transinfo.get(4));
                    if(transinfo.get(5).contains("-")){
                        transParams1.setTransType(TransTypeEnum.TRANSFER.getValue());
                    }else {
                        transParams1.setTransType(TransTypeEnum.RRCHARGE.getValue());
                    }
                    BigDecimal amout = new BigDecimal(getContent(transinfo.get(5), Pattern.compile("([1-9]\\d*\\.?\\d+)|(0\\.\\d*[1-9])|(\\d+)")));
                    transParams1.setAmout(amout);
                    transParams1.setRemark(transinfo.get(6));
                    transParams.add(transParams1);
                }catch (Exception e){
                    log.error("解析异常失败 内容{} 异常原因 {}", info, e);
                }

            }
        }

        if("appalipay".equals(bankCode)){
            //余额宝-收益发放|0.01|投资理财|今天|05:29; 余额宝-收益发放|0.01|投资理财|昨天|04:01; 余额宝-自动转入|0.01|投资理财|04-12|11:27; 淘宝签到提现-淘宝（中国）软件有限公司|+0.01|转账红包|04-12|11:27; 余额宝-自动转入|0.08|投资理财|04-12|11:26
            List<String> infos  =  Arrays.asList(txt.split("; "));
            for (String info: infos) {

                TransParams transParams1 = new TransParams();
                List<String> transinfo =  Arrays.asList(info.split("\\|"));
                name = transinfo.get(2);
                amout = new BigDecimal(transinfo.get(1).replace(",", ""));
                if(transinfo.get(3).equals("今天")){
                   transTime =  new SimpleDateFormat("yyyy-MM-dd ").format(new Date()).concat(transinfo.get(4));
                }else  if(transinfo.get(4).equals("明天")){
                    transTime = new SimpleDateFormat("yyyy-MM-dd ").format(new Date(System.currentTimeMillis() + 24*3600)).concat(transinfo.get(4));
                }else {
                    transTime = new Integer(calendar.get(Calendar.YEAR)).toString().concat("-").concat(transinfo.get(4)).concat(" ").concat(transinfo.get(4));
                }
                if(transinfo.get(0).contains("入")){
                    transType = TransTypeEnum.RRCHARGE.getValue();
                }else {
                    transType = TransTypeEnum.TRANSFER.getValue();
                }
                transParams1.setName(name);
                transParams1.setAmout(amout);
                transParams1.setTransType(transType);
                transParams1.setTransTime(transTime);
                transParams.add(transParams1);
            }
        }

    }

}


