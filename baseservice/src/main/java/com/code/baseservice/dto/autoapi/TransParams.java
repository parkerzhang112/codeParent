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


    private String account;


     /**
     * 卡描述 当前余额和银行卡尾号之类的
     */
    @JsonProperty("bankCode")
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

    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {
        Pattern transAccountPattern = Pattern.compile("[^0-9]");
        String tr = getContent("2323张三", transAccountPattern);
        System.out.printf(tr);
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
        String balanceStr = getContent(txt, Pattern.compile("余额(.*?)元"));
        balance = new BigDecimal(balanceStr.replaceAll(" ", ""));
        if("alipay".equals(bankCode) ){
            List<String> infos  =  Arrays.asList(txt.split("\n"+calendar.get(Calendar.YEAR)));
            for (String info: infos) {
                if(info.contains("你好")){
                    continue;
                }
                TransParams transParams1 = new TransParams();
                info = info.replace("\\n", "n");
                List<String> transinfo =  Arrays.asList(info.split("nn"));
                String time = calendar.get(Calendar.YEAR)+transinfo.get(0).concat(" ").concat(transinfo.get(1));
                time = time.replaceAll("\\.", "-");
                transParams1.setTransTime(time);
                transParams1.setName(transinfo.get(4));
                if(transinfo.get(5).contains("-")){
                    transType = TransTypeEnum.TRANSFER.getValue();
                }else {
                    transType = TransTypeEnum.RRCHARGE.getValue();
                }
                BigDecimal amout = new BigDecimal(getContent(transinfo.get(5), Pattern.compile("\\d+(\\.\\d+)?")));
                transParams1.setAmout(amout);
                transParams1.setRemark(transinfo.get(6));
                transParams.add(transParams1);
            }
        }
    }

}
