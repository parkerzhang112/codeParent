package com.code.baseservice.util;

import com.alibaba.fastjson.JSONObject;
import com.code.baseservice.base.constant.TransTypeEnum;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *  内容提取
 * @author shadow
 */
public class BankCaseUtil {

    public static final Logger logger = LoggerFactory.getLogger(BankCaseUtil.class);

    public static SimpleDateFormat bankformat = new SimpleDateFormat("yyyy年MM月DD日HH:mm");
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter FORMAT_HM = DateTimeFormatter.ofPattern("dd:HH");


    // private static  SimpleDateFormat sf = new SimpleDateFormat("HH:mm");

    private static  SimpleDateFormat monthsf = new SimpleDateFormat("MM");
    private static final ZoneId ZONE_ID = ZoneId.of("Asia/Shanghai");

    //平安银行
    public static final Pattern PAB_Card4End = Pattern.compile("[您尾号,您存款账户](.*?)[的账户,于,业务转入]");
    public static final Pattern PAB_Amount = Pattern.compile("转入人民币(.*?)元");
    public static final Pattern PAB_Time = Pattern.compile("于(.*?)[付款业务,网银转入]");

    //邮储银行
    public static final Pattern PSBC_Card4End = Pattern.compile("您尾号(.*?)账户");
    public static final Pattern PSBC_Amount = Pattern.compile("金额(.*?)元");

    public static final Pattern PSBC_Time = Pattern.compile("年(.*?)[账户,您尾号]");
    public static final Pattern PSCB_Balance = Pattern.compile("余额(.*?)元");
    public static final Pattern PSBC_Name = Pattern.compile("向(.*?)尾号");


    //工商银行
    public static final Pattern ICBC_Card4End = Pattern.compile("您尾号(.*?)卡");
    public static final Pattern ICBC_Amount = Pattern.compile("\\)(.*?)元，");
    public static final Pattern ICBC_Time = Pattern.compile("卡(.*?):");
    public static final Pattern ICBC_Name = Pattern.compile("收入\\)(.*?)转账");
    public static final Pattern ICBC_Name1 = Pattern.compile("户名：(.*?)，");
    public static final Pattern ICBC_Balance = Pattern.compile("余额(.*?)元");
    public static final Pattern ICBC_TransAccount = Pattern.compile("对方账户尾号：(.*?)。");


    //农业银行
    public static final Pattern ABC_Card4End = Pattern.compile("您尾号(.*?)账户");
    public static final Pattern ABC_Amount = Pattern.compile("交易人民币(.*?)，");
    public static final Pattern ABC_Time = Pattern.compile("于(.*?)向您尾号");
    public static final Pattern ABC_Time2 = Pattern.compile("账户(.*?)完成");
    public static final Pattern ABC_Name = Pattern.compile("银行】(.*?)于");
    public static final Pattern ABC_Name1 = Pattern.compile("向(.*?)完成");
    public static final Pattern ABC_Balacne = Pattern.compile("余额(.*?)。");



    //民生银行
    public static final Pattern CMBC_Card4End = Pattern.compile("账户\\*(.*?)于");
    public static final Pattern CMBC_Amount = Pattern.compile("存入￥(.*?)元，");
    public static final Pattern CMBC_Time = Pattern.compile("于(.*?)存入");

    //中国银行
    public static final Pattern BOC_Card4End = Pattern.compile("账户(.*?)[，于]");
    public static final Pattern BOC_Amount = Pattern.compile("人民币(.*?)元");
    // public static final Pattern BOC_Time = Pattern.compile("于(.*?)[网上支付,银联入账,收入,POS]");
    public static final Pattern BOC_Time = Pattern.compile("于(.*?)[网上支付,银联入账,收入,POS,手机银行]");

    //兴业银行
    public static final Pattern CIB_Card4End = Pattern.compile("账户\\*(.*?)\\*");
    public static final Pattern CIB_Amount = Pattern.compile("收入(.*?)元，");
    public static final Pattern CIB_Time = Pattern.compile("(.*?)账户");
    public static final Pattern CIB_Balance = Pattern.compile("余额(.*?)元");
    public static final Pattern CIB_Name = Pattern.compile("对方户名:(.*?)\\[");


    //建设银行
    public static final Pattern CCB_Card4End = Pattern.compile("尾号(.*?)的");
    public static final Pattern CCB_Amount = Pattern.compile("人民币(.*?)元");
    public static final Pattern CCB_Time = Pattern.compile("账户(.*?)[支付机构,银联入账]");
    public static final Pattern CCB_Time2 = Pattern.compile("月(.*?)分");
    public static final Pattern CCB_Balance = Pattern.compile("余额(.*?)元");

    public static final Pattern CCB_Name = Pattern.compile("^(.*?)月");


    //福建农信
    public static final Pattern FJNX_Card4End = Pattern.compile("您的(.*?)账户");
    public static final Pattern FJNX_Amount = Pattern.compile("收入(.*?)人民币");
    public static final Pattern FJNX_Time = Pattern.compile("账户(.*?)分");

    // 交通银行
    public static final Pattern JTONG_Card4End = Pattern.compile("\\*(.*?)[的卡,于]");
    public static final Pattern JTONG_Amount = Pattern.compile("转入[\\u4e00-\\u9fa5]*(.*?)元");
    public static final Pattern JTONG_Time = Pattern.compile("于(.*?)[手机,网络,支付宝,财付通,在]");

    // 中信银行
    public static final Pattern ZXING_Card4End = Pattern.compile("尾号(.*?)的");
    public static final Pattern ZXING_Amount = Pattern.compile("入人民币(.*?)元");
    public static final Pattern ZXING_Time = Pattern.compile("于(.*?)，");

    // 招商银行
    public static final Pattern CMB_CARD4END = Pattern.compile("您账户(.*?)于");
    public static final Pattern CMB_AMOUNT = Pattern.compile("人民币(.*?)，");
    // public static final Pattern CMB_TIME = Pattern.compile("于(.*?)[银联,收款,手机,网络,支付宝]");
    public static final Pattern CMB_TIME = Pattern.compile("于(.*?)[银联,收款,手机,网络,支付宝,他行]");
    public static final Pattern CMB_NAME = Pattern.compile("付方(.*?)$");
    public static final Pattern CMB_NAME_TRANS = Pattern.compile("收款人(.*?)$");

    public static final Pattern CMB_BALANCE = Pattern.compile("余额(.*?)，");



    // 浦发银行 95528
    // 您尾号9645卡人民币活期14:10存入1,000.00[支付宝-张灿彬支付宝转]，可用余额1018.54。【浦发银行】
    public static final Pattern SPDB_CARD4END = Pattern.compile("尾号(.*?)卡");
    public static final Pattern SPDB_AMOUNT = Pattern.compile("存入(.*?)元");
    public static final Pattern SPDB_TIME = Pattern.compile("活期(.*?)[存入]");
    public static final Pattern SPDB_NAME = Pattern.compile("](.*?)，可用");
    public static final Pattern SPDB_ZFB_NAME = Pattern.compile("支付宝-(.*?)支付");
    public static final Pattern SPDB_BALANCE = Pattern.compile("可用余额(.*?)。");


    //华夏银行 95577
    //您的账户4654于07月26日16:53收入人民币610.00元，余额710.00元。支付宝收入。【华夏银行】
    public static final Pattern HUAXIA_CARD4END = Pattern.compile("账户(.*?)于");
    public static final Pattern HUAXIA_AMOUNT = Pattern.compile("人民币(.*?)元");
    public static final Pattern HUAXIA_TIME = Pattern.compile("于(.*?)收入");

    //农信 9552
    //【河南农信】您尾号5165于2022年06月08日17时00分通过本行跨行支付系统账号1374（户名：余佳菲）转入5000.00元，余额9227.18元。存款有保险，安全有保障。
    public static final Pattern HENAN_CARD4END = Pattern.compile("您尾号(.*?)于");
    public static final Pattern ENAN_AMOUNT = Pattern.compile("转入(.*?)元");
    public static final Pattern ENAN_TIME = Pattern.compile("于(.*?)通过本行跨行支付系统账号");
    public static final Pattern ENAN_NAME = Pattern.compile("：(.*?)）转入");

    //农信 9552
    //【湖北农信】您尾号3869银行卡08月30日18:07从邮储银行矫成海转入107.00元(个人超网跨行收款)，余额107.38元，以实际资金到账为准。
    public static final Pattern HBNCYH_CARD4END = Pattern.compile("您尾号(.*?)银行");
    public static final Pattern HBNCYH_AMOUNT = Pattern.compile("转入(.*?)元");
    public static final Pattern HBNCYH_TIME = Pattern.compile("于(.*?)通过本行跨行支付系统账号");
    public static final Pattern HBNCYH__NAME = Pattern.compile("从邮储银行(.*?)转入");

    //浙信 9552
    //【浙江农信】北仑联社提醒：张三于06月24日12:31通过丰收e网向您的尾号为7410的账号汇入1000.00元，请关注到账情况
    public static final Pattern ZJNX_CARD4END = Pattern.compile("尾号为(.*?)的账号");
    public static final Pattern ZJNX_AMOUNT = Pattern.compile("汇入(.*?)元");
    public static final Pattern ZJNX_TIME = Pattern.compile("于(.*?)通过");
    public static final Pattern ZJNX__NAME = Pattern.compile("：(.*?)于");



    //浙信 9552
    //【浙江农信】北仑联社提醒：张三于06月24日12:31通过丰收e网向您的尾号为7410的账号汇入1000.00元，请关注到账情况
    public static final Pattern BAOANBANK_Amount = Pattern.compile("转账）(.*?)元");
    public static final Pattern BAOANBANK_Balance = Pattern.compile("现余额(.*?)元");
    public static final Pattern BAOANBANK_Card4End = Pattern.compile("尾号(.*?)账户");
    public static final Pattern BAOANBANK_Time = Pattern.compile("于(.*?)收");
    public static final Pattern BAOANBANK_Name = Pattern.compile("对方户名(.*?)");


    //东海村镇银行
    //【东海村镇银行】您尾号8073卡02月20日 16:34转账转入人民币100.00元，对方帐户3140，户名张银，当前余额100.00元。本机构吸收的本外币存款依照《存款保险条例》受到保护。
    //【东海村镇银行】您尾号8073卡02月20日 16:40汇款转出人民币100.00元，对方帐户5386，户名何海龙，当前余额0元。
    //【东海村镇银行】您尾号8073卡02月20日16:45支付宝转入人民币10.00元，余额10.00元。本机构吸收的本外币存款依照《存款保险条例》受到保护。
    public static final Pattern DHCZ_Amount = Pattern.compile("人民币(.*?)元");
    public static final Pattern DHCZ_Balance = Pattern.compile("当前余额(.*?)元");
    public static final Pattern DHCZ_Card4End = Pattern.compile("尾号(.*?)卡");
    public static final Pattern DHCZ_Time = Pattern.compile("卡(.*?)转");
    public static final Pattern DHCZ_Name = Pattern.compile("户名(.*?)，");

    //江苏银行
    //【江苏银行】您尾号14083账户于2月23日转入人民币1,000.00元，余额1,003.01元，对方户名为何兵飞，摘要：转账。
    //【江苏银行】您尾号14083账户于2月23日转出人民币302.00元，余额611.01元，对方户名为何海龙，摘要：转账。
    public static final Pattern JSBANK_Amount = Pattern.compile("人民币(.*?)元");
    public static final Pattern JSBANK_Balance = Pattern.compile("余额(.*?)元");
    public static final Pattern JSBANK_Card4End = Pattern.compile("尾号(.*?)账");
    public static final Pattern JSBANK_Time = Pattern.compile("于(.*?)转");
    public static final Pattern JSBANK_Name = Pattern.compile("户名为(.*?)，");


    //宁夏
    //【宁夏银行】朱丛于2023年03月03日13:33向您尾号3158的卡号跨行转入人民币253.00元，余额257.76元。本机构吸收的本外币存款依照《存款保险条例》受到保护
    //【宁夏银行】您尾号3158的卡号于2023年03月03日个人手机银行向刘静静转账支出人民币257.00元，活期余额0.76元。本机构吸收的本外币存款依照《存款保险条例》受到保护。
    public static final Pattern NXBANK_Amount = Pattern.compile("人民币(.*?)元");
    public static final Pattern NXBANK_Balance = Pattern.compile("余额(.*?)元");
    public static final Pattern NXBANK_Card4End = Pattern.compile("尾号(.*?)的卡");
    public static final Pattern NXBANK_Time = Pattern.compile("于(.*?)向");
    public static final Pattern NXBANK_Name = Pattern.compile("】(.*?)于");
    public static final Pattern NXBANK_Trans_Name = Pattern.compile("向(.*?)转账");


    //石山嘴
    //【石嘴山银行】您的账号4068于02月21日15时33分，收到由（朱丛）转入1001.00元，余额3000.42元，详情请致电96789。本机构吸收的本外币存款依照《存款保险条例》受到保护。
    //【石嘴山银行】您的账号4068于02月21日15时46分，向（吕晶）支出3000.00元，余额0.42元，详情请致电96789。本机构吸收的本外币存款依照《存款保险条例》受到保护。
    public static final Pattern SSZBANK_Amount = Pattern.compile("入(.*?)元");
    public static final Pattern SSZBANK_Trans_Amount = Pattern.compile("支出(.*?)元");

    public static final Pattern SSZBANK_Balance = Pattern.compile("余额(.*?)元，");
    public static final Pattern SSZBANK_Card4End = Pattern.compile("账号(.*?)于");
    public static final Pattern SSZBANK_Time = Pattern.compile("于(.*?)分，");
    public static final Pattern SSZBANK_Name = Pattern.compile("由（(.*?)）");
    public static final Pattern SSZBANK_Trans_Name = Pattern.compile("向（(.*?)）");


    //华夏银行
    //您的账户6073于03月03日14:14收入人民币252.00元，余额252.36元。跨行收款，付款方郭瑞。【华夏银行】
    //您的账户6073于03月03日14:15支出人民币252.00元，余额0.36元。手机银行跨行付款，收款方郭瑞。【华夏银行】
    public static final Pattern HXB_Amount = Pattern.compile("人民币(.*?)元");
    public static final Pattern HXB_Balance = Pattern.compile("余额(.*?)元");
    public static final Pattern HXB_Card4End = Pattern.compile("账户(.*?)于");
    public static final Pattern HXB_Time = Pattern.compile("于(.*?)人民");
    public static final Pattern HXB_Name = Pattern.compile("款方(.*?)。");

    //南京银行
    //【南京银行】您尾号7226的账号于03月03日01时07分收到由朱雷汇入的100000.00元，余额100030.20，摘要：城商实时汇兑
    //【南京银行】您尾号7226的账号于03月03日15时00分支出100000.00元，余额30.20元，对方户名：朱雷，摘要：网银跨行转账
    public static final Pattern NJB_Amount = Pattern.compile("汇入的(.*?)元");
    public static final Pattern NJB_Trans_Amount = Pattern.compile("支出(.*?)元");
    public static final Pattern NJB_Balance = Pattern.compile("余额(.*?)，");
    public static final Pattern NJB_Trans_Balance = Pattern.compile("余额(.*?)元");
    public static final Pattern NJB_Card4End = Pattern.compile("尾号(.*?)的账");
    public static final Pattern NJB_Time = Pattern.compile("于(.*?)分");
    public static final Pattern NJB_Name = Pattern.compile("由(.*?)汇入");
    public static final Pattern NJB__Trans_Name = Pattern.compile("户名：(.*?)，");

    //交通银行
    public static final Pattern BCOM_Amount = Pattern.compile("转入(.*?)元");
    public static final Pattern BCOM_Balance = Pattern.compile("余额(.*?)元");
    public static final Pattern BCOM_TRANS_Amount = Pattern.compile("转出(.*?)元");
    public static final Pattern BCOM_TRANS_Balance = Pattern.compile("余额为(.*?)元");
    public static final Pattern BCOM_Card4End = Pattern.compile("[账户|尾号](.*?)于");
    public static final Pattern BCOM__TRANS_Card4End = Pattern.compile("尾号(.*?)的卡");

    public static final Pattern BCOM_Time = Pattern.compile("于(.*?)入");
    public static final Pattern BCOM_Trans_Time = Pattern.compile("于(.*?)转");

    public static final Pattern BCOM_Name = Pattern.compile("户名：(.*?)，");

    public static void main (String[] args) {
//        String card4EndNo = "95511";
//        String smsContent = "【平安银行】您尾号8843的账户于5月15日1:04付款业务转入人民币1000.03元,存款账户余额人民币89067.80元。详询95511-3【平安银行】";
        String card4EndNo = "BOC";
//        String smsContent = "【河南农信】您尾号4340于2022年06月13日11时53分通过本行跨行支付系统账号1177（户名：黄丽萍）转入10055.00元，余额10778.84元。存款有保险，安全有保障。";
//        String smsContent  = "【湖北农信】您尾号3869银行卡08月30日18:07从邮储银行矫成海转入107.00元(个人超网跨行收款)，余额107.38元，以实际资金到账为准。";
//        String smsContent = "    //【浙江农信】北仑联社提醒：张三于06月24日12:31通过丰收e网向您的尾号为7410的账号汇入1000.00元，请关注到账情况";
//        String smsContent  = "您尾号0220卡人民币活期12:33存入200.00元[互联汇入]名字3302，可用余额200.00元。【浦发银行】";
//        String smsContent  = "【招商银行】您账户3302于09月01日他行实时转入人民币200.00，余额300.00付方王天明";
        //        String card4EndNo = "95580";
//        Stringing smsContent = "【邮储银行】19年04月23日19:58您尾号959账户提现金额2000.01元，余额5970.08元。";
//        String card4EndNo = "95588";
//        String smsContent = "【工商银行】您尾号5196卡1月22日23:58快捷支付收入(张海龙支付宝转账支付宝)5元，余额9.60元。";
//        String card4EndNo = "95599";
//        String smsContent = "【中国农业银行】您尾号6970账户01月26日14:08完成代付交易人民币20.00，余额47638.71";
//        String card4EndNo = "95533";
//        String smsContent = "您尾号6744的储蓄卡账户4月20日19时6分银联入账收入人民币3000.01元,活期余额16047.00元。[建设银行]";
        String smsContent  = "您的借记卡账户长城电子借记卡，于09月16日网上支付收入人民币100.00元,交易后余额100.10【中国银行】";
        JSONObject bankInfo  = getBankInfo (card4EndNo,smsContent);
        System.out.println (bankInfo);
    }

    /**
     * @描述:解析银行卡信息 返回数据: {"amount":"3000.01","time":"2019年4月20日19:6分","gmtCreate":1547982360000,"card4EndNo":"6744"}
     *  暂不可用银行： 兴业银行、邮储银行
     * @作者:nada
     * @时间:2019/4/17
     **/
    public static JSONObject getBankInfo(String card4EndNo, String smsContent){
        if(null == smsContent ||
                smsContent.contains("信用卡") ||
                smsContent.contains("手机交易码") ||
                smsContent.contains("验证码")||
                smsContent.contains("动态码")){
            return null;
        }
        JSONObject bankInfo = new JSONObject(8);
        try {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH)  == 12 ? 01 :  calendar.get(Calendar.MONTH)  + 1;
            // int month = (calendar.get(Calendar.MONTH) + 1)%12;
            String amount = "";
            String time = "";
            String parseTime = "";
            Integer transType = 1;

            String balance = "";
            String parseName = "";
            String parseTransAccount;
            SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            switch(card4EndNo){
                case "HNNCYH":
                    //"河南农信】您尾号5165于2022年06月08日17时00分通过本行跨行支付系统账号1374（户名：余佳菲）转入5000.00元，余额9227.18元。存款有保险，安全有保障。";
                    //【河南农信】您尾号4340于2022年06月13日11时53分通过本行跨行支付系统账号1177（户名：黄丽萍）转入10055.00元，余额10778.84元。存款有保险，安全有保障。
                    amount = BankCaseUtil.getContent(smsContent, BankCaseUtil.ENAN_AMOUNT);
                    card4EndNo = BankCaseUtil.getContent(smsContent, BankCaseUtil.HENAN_CARD4END);
                    parseTime = BankCaseUtil.getContent(smsContent, BankCaseUtil.ENAN_TIME);
                    parseName =  BankCaseUtil.getContent(smsContent, BankCaseUtil.ENAN_NAME);
                    time = year + "年" + monthsf.format(new Date ())+"月"+parseTime;
                    bankInfo.put ("amount",amount);
                    bankInfo.put ("card4EndNo",card4EndNo);
                    bankInfo.put ("time",time);
                    bankInfo.put("name", parseName );
                    return  bankInfo;
                case "CCB":
                    //您尾号6744的储蓄卡账户4月20日19时6分银联入账收入人民币3000.01元,活期余额16047.00元。[建设银行]
                    //您尾号5047的储蓄卡账户10月7日7时1分支付机构提现收入人民币300.01元,活期余额3098.31元。[建设银行]
                    //许云龙12月9日14时46分向您尾号8872的储蓄卡账户电子汇入收入人民币153.04元,活期余额13956.29元。[建设银行]
                    //您尾号2639的储蓄卡6月28日0时6分支付宝提现收入人民币500.00元,活期余额5057.47元。[建设银行]
                    amount = BankCaseUtil.getContent(smsContent, BankCaseUtil.CCB_Amount);
                    card4EndNo = BankCaseUtil.getContent(smsContent, BankCaseUtil.CCB_Card4End);
                    balance = BankCaseUtil.getContent(smsContent,  BankCaseUtil.CCB_Balance);
                    if(smsContent.contains("收入")){
                        transType = TransTypeEnum.RRCHARGE.getValue();
                    }else {
                        transType = TransTypeEnum.TRANSFER.getValue();
                    }
                    parseName = BankCaseUtil.getContent(smsContent, BankCaseUtil.CCB_Name);
                    parseName = parseName.replaceAll("\\d", "");
                    parseTime = BankCaseUtil.getContent(smsContent,CCB_Time2);
                    String dateTime =year+"-"+ month+"-"+parseTime.replace("日", " ").replace("时",":").replace("分", ":00").concat(":00");
                    bankInfo.put ("amount",amount);
                    bankInfo.put ("card4EndNo",card4EndNo);
                    bankInfo.put ("time",dateTime);
                    bankInfo.put("name", parseName);
                    bankInfo.put("transType", transType);
                    bankInfo.put("balance", balance);
                    return  bankInfo;
                case "CIB":
                    //18日15:53账户*8731*汇款汇入收入250.00元，余额250.00元。对方户名:张荣达[兴业银行]
                    //23日00:09账户*8221*网联付款收入105.00元，余额19098.75元[兴业银行]
                    //18日15:53账户*8731*汇款汇入收入250.00元，余额250.00元。对方户名:张荣达[兴业银行]
                    //  06日17:38账户*8731*跨行代付收入10000.00元，余额26917.42元[兴业银行]
                    amount = BankCaseUtil.getContent(smsContent, BankCaseUtil.CIB_Amount);
                    card4EndNo = BankCaseUtil.getContent(smsContent, BankCaseUtil.CIB_Card4End);
                    parseTime = BankCaseUtil.getContent(smsContent, BankCaseUtil.CIB_Time);
                    parseName = BankCaseUtil.getContent(smsContent, BankCaseUtil.CIB_Name).replaceAll("\\[]", "");
                    if(smsContent.contains("收入")){
                        transType = TransTypeEnum.RRCHARGE.getValue();
                    }else {
                        transType = TransTypeEnum.TRANSFER.getValue();
                    }
                    balance = BankCaseUtil.getContent(smsContent, BankCaseUtil.CIB_Balance);
                    dateTime =year+"-"+ month+"-"+parseTime.replace("日", " ").replace("时",":");
                    bankInfo.put ("amount",amount);
                    bankInfo.put ("card4EndNo",card4EndNo);
                    bankInfo.put ("time",dateTime);
                    bankInfo.put("balance", balance);
                    bankInfo.put("transType", transType);
                    bankInfo.put("name", parseName);
                    return  bankInfo;
                case "BOC":
                    // 您的借记卡账户陈清强，于10月09日网上支付收入人民币700.11元，交易后余额763.50【中国银行】
                    // 您的借记卡/账户4717于12月09日银联入账人民币500.04元（财付通支付科技有限公司）,交易后余额15259.20【中国银行】";
                    // 您的借记卡账户8852， 于12月09日收入(网银跨行)人民币1000.01元，交易后余额3621.97【中国银行】";
                    // 您的借记卡账户4717，于12月09日POS收入人民币123.04元，交易后余额5189.05【中国银行】";
                    // 您的借记卡账户8317， 于12月09日收入(网银跨行)人民币200.01元，交易后余额6508.52【中国银行】";
                    // 您的借记卡账户8852，于12月30日手机银行收入人民币3000.04元，交易后余额3169.63【中国银行】
                    // 您的借记卡账户2391，于02月28日网上支付收入人民币100.01元，交易后余额8005.15【中国银行】
                    amount = BankCaseUtil.getContent(smsContent, BankCaseUtil.BOC_Amount);
                    card4EndNo = BankCaseUtil.getContent(smsContent, BankCaseUtil.BOC_Card4End);
                    parseTime = BankCaseUtil.getContent(smsContent, BankCaseUtil.BOC_Time);
                    LocalDateTime now = LocalDateTime.now(ZONE_ID);
                    parseTime = (null == parseTime)?"":parseTime+now.getHour()+":"+now.getMinute();
                    time = year + "年".concat(parseTime);
                    bankInfo.put ("amount",amount);
                    bankInfo.put ("card4EndNo",card4EndNo);
                    bankInfo.put ("time",time);
                    return  bankInfo;
                case "CMBC":
                    //【民生银行】账户*1546于04月18日16:16存入￥3000.00元，可用余额3000.97元。存款。【民生银行】
                    //账户*6804于01月22日10:43存入￥500.00元，付方支付宝（中国）网络技术有限公司，可用余额40852.20元。1-1-1-1-支付宝（中国）网络技术有限公司。【民生银行】
                    //账户*2140于01月24日12:19存入￥500.00元，可用余额3109.08元。奚晓亚支付宝转账-奚晓亚支付宝转账-支付宝（中国）网络技术有限公司。【民生银行】
                    amount = BankCaseUtil.getContent(smsContent, BankCaseUtil.CMBC_Amount);
                    card4EndNo = BankCaseUtil.getContent(smsContent, BankCaseUtil.CMBC_Card4End);
                    parseTime = BankCaseUtil.getContent(smsContent, BankCaseUtil.CMBC_Time);
                    time = year + "年" + parseTime;
                    bankInfo.put ("amount",amount);
                    bankInfo.put ("card4EndNo",card4EndNo);
                    bankInfo.put ("time",time);
                    return  bankInfo;
                case "ABC":
                    //【中国农业银行】张荣达于04月18日16:08向您尾号3912账户完成转存交易人民币328.00，余额328.00。
                    //【中国农业银行】支付宝（中国）网络技术有限公司于01月22日23:49向您尾号9769账户完成代付交易人民币10.00，余额10.07
                    //【中国农业银行】支付宝（中国）网络技术有限公司于01月25日20:42向您尾号4272账户完成代付交易人民币100.01，余额296.04。
                    //【中国农业银行】您尾号6970账户01月26日14:08完成代付交易人民币20.00，余额47638.71。
                    //【中国农业银行】徐洪涛于06月09日12:16向您尾号0774账户完成转存交易人民币8088.00，余额14013.00。。
                    amount = BankCaseUtil.getContent(smsContent, BankCaseUtil.ABC_Amount).replace("-", "");
                    card4EndNo = BankCaseUtil.getContent(smsContent, BankCaseUtil.ABC_Card4End);
                    parseTime = BankCaseUtil.getContent(smsContent, BankCaseUtil.ABC_Time);
                    if("".equals(parseTime)){
                        parseTime = BankCaseUtil.getContent(smsContent, BankCaseUtil.ABC_Time2);
                    }
                    if(smsContent.contains("转存") || smsContent.contains("转账") || smsContent.contains("入账")|| smsContent.contains("完成代付")){
                        transType = TransTypeEnum.RRCHARGE.getValue();
                        parseName =  BankCaseUtil.getContent(smsContent, BankCaseUtil.ABC_Name);
                    }else {
                        transType = TransTypeEnum.TRANSFER.getValue();
                        parseName =  BankCaseUtil.getContent(smsContent, BankCaseUtil.ABC_Name1);
                    }
                    balance = BankCaseUtil.getContent(smsContent, BankCaseUtil.ABC_Balacne).replace(",", "");
                    dateTime =year+"-"+ parseTime.replace("月", "-").replace("日", " ").replace("时",":").replace("分", ":00");
                    bankInfo.put ("amount",amount);
                    bankInfo.put("balance", balance);
                    bankInfo.put("transType", transType);
                    bankInfo.put("name", parseName);
                    bankInfo.put ("card4EndNo",card4EndNo);
                    bankInfo.put ("time",dateTime);
                    return  bankInfo;
                case "ICBC":
                    //【工商银行】您尾号5196卡1月22日23:58快捷支付收入(张海龙支付宝转账支付宝)5元，余额9.60元。
                    //【工商银行】您尾号6073卡4月18日15:39工商银行收入(他行汇入)300元，余额300元。
                    // 您尾号3789卡1月15日17:03快捷支付收入(转账到银行卡财付通)1,000.02元，余额1,776.21元。【工商银行】
                    amount = BankCaseUtil.getContent(smsContent, BankCaseUtil.ICBC_Amount);
                    balance = BankCaseUtil.getContent(smsContent, BankCaseUtil.ICBC_Balance).replace(",", "").replace("元", "");
                    card4EndNo = BankCaseUtil.getContent(smsContent, BankCaseUtil.ICBC_Card4End);
                    parseTime = BankCaseUtil.getContent(smsContent, BankCaseUtil.ICBC_Time);
                    if(isThird(smsContent)){
                        parseName = BankCaseUtil.getContent(smsContent,BankCaseUtil.ICBC_Name);
                        parseName = parseTime.replace(getThirdName(smsContent), "");
                    }else{
                        parseName = BankCaseUtil.getContent(smsContent,BankCaseUtil.ICBC_Name1);
                    }
                    if(StringUtils.isNotEmpty (parseTime) && smsContent.indexOf (parseTime) >0){
                        int timerIndex = smsContent.indexOf (parseTime)+parseTime.length ();
                        parseTime =  parseTime + smsContent.substring (timerIndex,timerIndex+3);
                    }
                    if(smsContent.contains("入")){
                        transType = TransTypeEnum.RRCHARGE.getValue();
                    }else {
                        transType = TransTypeEnum.TRANSFER.getValue();
                    }
                    parseTransAccount  = BankCaseUtil.getContent(smsContent,ICBC_TransAccount);
                    dateTime =year+"-"+ parseTime.replace("月", "-").replace("日", " ").replace("时",":").replace("分", ":00");
                    bankInfo.put ("amount",amount);
                    bankInfo.put ("card4EndNo",card4EndNo);
                    bankInfo.put ("time",dateTime);
                    bankInfo.put ("balance",balance);
                    bankInfo.put ("name",parseName);
                    bankInfo.put ("transType",transType);
                    bankInfo.put("transAccount", parseTransAccount);
                    return  bankInfo;
                case "HBNCYH":
                    //【湖北农信】您尾号3869银行卡08月30日18:07从邮储银行矫成海转入107.00元(个人超网跨行收款)，余额107.38元，以实际资金到账为准。
                    amount = BankCaseUtil.getContent(smsContent, BankCaseUtil.HBNCYH_AMOUNT);
                    card4EndNo = BankCaseUtil.getContent(smsContent, BankCaseUtil.HBNCYH_CARD4END);
                    parseName = BankCaseUtil.getContent(smsContent, BankCaseUtil.HBNCYH__NAME);
                    time = year + "年" + BankCaseUtil.getContent(smsContent, BankCaseUtil.HBNCYH_TIME);
                    bankInfo.put ("amount",amount);
                    bankInfo.put("name", parseName);
                    bankInfo.put ("card4EndNo",card4EndNo);
                    bankInfo.put ("time",time);
                    return  bankInfo;
                case "ZJNX":
                    //【湖北农信】您尾号3869银行卡08月30日18:07从邮储银行矫成海转入107.00元(个人超网跨行收款)，余额107.38元，以实际资金到账为准。
                    amount = BankCaseUtil.getContent(smsContent, BankCaseUtil.ZJNX_AMOUNT);
                    card4EndNo = BankCaseUtil.getContent(smsContent, BankCaseUtil.ZJNX_CARD4END);
                    parseName = BankCaseUtil.getContent(smsContent, BankCaseUtil.ZJNX__NAME);
                    time = year + "年" + BankCaseUtil.getContent(smsContent, BankCaseUtil.ZJNX_TIME);
                    bankInfo.put ("amount",amount);
                    bankInfo.put("name", parseName);
                    bankInfo.put ("card4EndNo",card4EndNo);
                    bankInfo.put ("time",time);
                    return  bankInfo;
                case "PSBC":
                    //【邮储银行】19年04月19日17:32您尾号959账户提现金额2000.03元，余额4000.07元。
                    //【邮储银行】19年04月17日12:56您尾号188账户提现金额10.30元，余额10.81元。
                    //【邮储银行】19年05月06日01:35您尾号297账户银联入账金额1001.04元，余额62991.89元。
                    //【邮储银行】19年12月10日07:15您尾号462账户提现金额400.04元，余额565.16元。
                    amount = BankCaseUtil.getContent(smsContent, BankCaseUtil.PSBC_Amount).replace(",", "");
                    balance = BankCaseUtil.getContent(smsContent, BankCaseUtil.PSCB_Balance).replace(",", "");

                    card4EndNo = BankCaseUtil.getContent(smsContent, BankCaseUtil.PSBC_Card4End);
                    time = BankCaseUtil.getContent(smsContent, BankCaseUtil.PSBC_Time);
                     dateTime = time.replace("月", "-").replace("日", " ").replace("时",":").replace("分", ":00");
                    parseName = getContent1(dateTime,  Pattern.compile("[\u4e00-\u9fa5]"));
                    if(parseName != null){
                        parseName = parseName.replace("账户", "");
                    }
                    if(smsContent.contains("入")){
                        transType = TransTypeEnum.RRCHARGE.getValue();
                    }else {
                        parseName = BankCaseUtil.getContent(smsContent,  BankCaseUtil.PSBC_Name);
                        transType = TransTypeEnum.TRANSFER.getValue();
                    }

                    dateTime =  CommonUtil.replaceChinese(dateTime, "");
                    String transTime = new Integer(calendar.get(Calendar.YEAR)).toString().concat("-"+dateTime);
                    bankInfo.put ("amount",amount);
                    bankInfo.put("balance", balance);
                    bankInfo.put ("card4EndNo",card4EndNo);
                    bankInfo.put ("time",transTime);
                    bankInfo.put ("transType",transType);
                    bankInfo.put ("name",parseName);
                    return  bankInfo;
                case "CMB":
                    // 您账户9132于09月29日22:47银联入账人民币10.00元（支付宝（中国）网络技术有限公司/张海龙支付宝转账）[招商银行]
                    // 您账户2045于12月30日他行实时转入人民币1000.01，付方刘新。查社保 cmbt.cn/sbkk 。[招商银行]
                    // 您账户5935于10月22日15:15收款人民币600.01，备注：财付通-财付通支付科技有限公司-，更多详情请查看招商银行APP动账通知。[招商银行]
                    // 您账户3107于01月15日00:11收款人民币100.03，备注：支付宝-支付宝（中国）网络技术有限公，更多详情请查看招商银行APP动账通知。[招商银行]
                    amount = BankCaseUtil.getContent(smsContent, CMB_AMOUNT);
                    card4EndNo = BankCaseUtil.getContent(smsContent, CMB_CARD4END);
                    balance = BankCaseUtil.getContent(smsContent,CMB_BALANCE);
                    if(smsContent.contains("入")){
                        parseName = BankCaseUtil.getContent(smsContent,CMB_NAME);
                        transType  = TransTypeEnum.RRCHARGE.getValue();
                    }else {
                        parseName = BankCaseUtil.getContent(smsContent,CMB_NAME_TRANS);
                        transType  = TransTypeEnum.TRANSFER.getValue();
                    }
                    SimpleDateFormat sdf1= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    dateTime =  sdf1.format(new Date());
                    bankInfo.put ("amount",amount);
                    bankInfo.put ("card4EndNo",card4EndNo);
                    bankInfo.put ("time",dateTime);
                    bankInfo.put("transType", transType);
                    bankInfo.put("balance", balance);
                    bankInfo.put("name", parseName);
                    return  bankInfo;
                case "SPDB":
                    // 浦发银行 95528
                    // 您尾号9645卡人民币活期14:10存入1,000.00[支付宝-张灿彬支付宝转]，可用余额1018.54。【浦发银行】
                    // 您尾号9645卡人民币活期12:17存入50.00[财付通-转账到银行卡]，可用余额3112.26。【浦发银行】
                    // 您尾号9645卡人民币活期12:19存入100.00[互联汇入]张海龙8476，可用余额3212.26。【浦发银行】
                    amount = BankCaseUtil.getContent(smsContent, SPDB_AMOUNT).replace(",","");
                    card4EndNo = BankCaseUtil.getContent(smsContent, SPDB_CARD4END);
                    parseTime = BankCaseUtil.getContent(smsContent,SPDB_TIME);
                    if(isThird(smsContent)){
                        if(smsContent.contains("支付宝")){
                            parseName = BankCaseUtil.getContent(smsContent, SPDB_ZFB_NAME);
                        }
                    }else {
                        parseName = BankCaseUtil.getContent(smsContent, SPDB_NAME);
                        parseName = parseName.replaceAll("\\d+", "");
                    }
                    if(smsContent.contains("存入")){
                        transType  = TransTypeEnum.RRCHARGE.getValue();
                    }else {
                        transType  = TransTypeEnum.TRANSFER.getValue();
                    }
                    balance = BankCaseUtil.getContent(smsContent, SPDB_BALANCE);
                    LocalDate ld = LocalDate.now(ZONE_ID);
                    time = ld.format(DATE_FORMAT).concat(" "+parseTime);
                    // time = year + "年".concat(parseTime);
                    bankInfo.put ("amount",amount);
                    bankInfo.put ("card4EndNo",card4EndNo);
                    bankInfo.put ("time",time);
                    bankInfo.put("transType", transType);
                    bankInfo.put("balance", balance);
                    bankInfo.put("name", parseName);
                    return  bankInfo;
                case "95577":
                    //95577，短信内容，您的账户4654于07月26日16:53收入人民币610.00元，余额710.00元。支付宝收入。【华夏银行】
                    amount = BankCaseUtil.getContent(smsContent, HUAXIA_AMOUNT).replace(",","");
                    card4EndNo = BankCaseUtil.getContent(smsContent, HUAXIA_CARD4END);
                    parseTime = BankCaseUtil.getContent(smsContent,HUAXIA_TIME);
                    time = year + "年".concat(parseTime);
                    if(!time.contains(":")){
                        time = time + LocalDateTime.now(ZONE_ID).format(FORMAT_HM);
                    }
                    // time = year + "年".concat(parseTime);
                    bankInfo.put ("amount",amount);
                    bankInfo.put ("card4EndNo",card4EndNo);
                    bankInfo.put ("time",time);
                    return  bankInfo;
                case "BAOANBANK":
                    amount = BankCaseUtil.getContent(smsContent, BankCaseUtil.BAOANBANK_Amount).replace(",", "");
                    balance = BankCaseUtil.getContent(smsContent, BankCaseUtil.BAOANBANK_Balance).replace(",", "");
                    if(smsContent.contains("入")){
                        transType = TransTypeEnum.RRCHARGE.getValue();
                    }else {
                        transType = TransTypeEnum.TRANSFER.getValue();
                    }

                    card4EndNo = BankCaseUtil.getContent(smsContent, BankCaseUtil.BAOANBANK_Card4End);
                    time = BankCaseUtil.getContent(smsContent, BankCaseUtil.BAOANBANK_Time);
                    dateTime = time.replace("月", "-").replace("日", " ").replace("时",":").replace("分", ":00");
                    parseName = getContent1(dateTime,  Pattern.compile("[\u4e00-\u9fa5]"));
                    if(parseName != null){
                        parseName = parseName.replace("账户", "");
                    }
                    parseName  = BankCaseUtil.getContent(smsContent, BankCaseUtil.BAOANBANK_Name);
                    dateTime =  CommonUtil.replaceChinese(dateTime, "");
                    transTime = new Integer(calendar.get(Calendar.YEAR)).toString().concat("-"+dateTime);
                    bankInfo.put ("amount",amount);
                    bankInfo.put("balance", balance);
                    bankInfo.put ("card4EndNo",card4EndNo);
                    bankInfo.put ("time",transTime);
                    bankInfo.put ("transType",transType);
                    bankInfo.put ("name",parseName);
                    return  bankInfo;
                case "DHCZ":
                    amount = BankCaseUtil.getContent(smsContent, BankCaseUtil.DHCZ_Amount).replace(",", "");
                    balance = BankCaseUtil.getContent(smsContent, BankCaseUtil.DHCZ_Balance).replace(",", "");
                    if(smsContent.contains("入")){
                        transType = TransTypeEnum.RRCHARGE.getValue();
                    }else {
                        transType = TransTypeEnum.TRANSFER.getValue();
                    }

                    card4EndNo = BankCaseUtil.getContent(smsContent, BankCaseUtil.DHCZ_Card4End);
                    time = BankCaseUtil.getContent(smsContent, BankCaseUtil.DHCZ_Time);
                    dateTime = time.replace("月", "-").replace("日", " ").replace("时",":").replace("分", ":00");
                    parseName  = BankCaseUtil.getContent(smsContent, BankCaseUtil.DHCZ_Name);
                    dateTime =  CommonUtil.replaceChinese(dateTime, "");
                    transTime = new Integer(calendar.get(Calendar.YEAR)).toString().concat("-"+dateTime);
                    bankInfo.put ("amount",amount);
                    bankInfo.put("balance", balance);
                    bankInfo.put ("card4EndNo",card4EndNo);
                    bankInfo.put ("time",transTime);
                    bankInfo.put ("transType",transType);
                    bankInfo.put ("name",parseName);
                    return  bankInfo;
                case "JSBANK":
                    amount = BankCaseUtil.getContent(smsContent, BankCaseUtil.JSBANK_Amount).replace(",", "");
                    balance = BankCaseUtil.getContent(smsContent, BankCaseUtil.JSBANK_Balance).replace(",", "");
                    if(smsContent.contains("入")){
                        transType = TransTypeEnum.RRCHARGE.getValue();
                    }else {
                        transType = TransTypeEnum.TRANSFER.getValue();
                    }
                    parseName  = BankCaseUtil.getContent(smsContent, BankCaseUtil.JSBANK_Name);
                    card4EndNo = BankCaseUtil.getContent(smsContent, BankCaseUtil.JSBANK_Card4End);
                    sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    dateTime =  sdf.format(new Date());
                    bankInfo.put ("amount",amount);
                    bankInfo.put("balance", balance);
                    bankInfo.put ("card4EndNo",card4EndNo);
                    bankInfo.put ("time",dateTime);
                    bankInfo.put ("transType",transType);
                    bankInfo.put ("name",parseName);
                    return  bankInfo;
                case "HXB":
                    amount = BankCaseUtil.getContent(smsContent, BankCaseUtil.HXB_Amount).replace(",", "");
                    balance = BankCaseUtil.getContent(smsContent, BankCaseUtil.HXB_Balance).replace(",", "");
                    if(smsContent.contains("入")){
                        transType = TransTypeEnum.RRCHARGE.getValue();
                    }else {
                        transType = TransTypeEnum.TRANSFER.getValue();
                    }
                    parseName  = BankCaseUtil.getContent(smsContent, BankCaseUtil.HXB_Name);
                    card4EndNo = BankCaseUtil.getContent(smsContent, BankCaseUtil.HXB_Card4End);
                    time = BankCaseUtil.getContent(smsContent, BankCaseUtil.HXB_Time);
                    dateTime = time.replace("月", "-").replace("日", " ").replace("时",":").replace("分", ":00");
                    dateTime =  CommonUtil.replaceChinese(dateTime, "");
                    transTime = new Integer(calendar.get(Calendar.YEAR)).toString().concat("-"+dateTime);
                    bankInfo.put ("amount",amount);
                    bankInfo.put("balance", balance);
                    bankInfo.put ("card4EndNo",card4EndNo);
                    bankInfo.put ("time",transTime);
                    bankInfo.put ("transType",transType);
                    bankInfo.put ("name",parseName);
                    return  bankInfo;
                case "SSZBANK":
                    balance = BankCaseUtil.getContent(smsContent, BankCaseUtil.SSZBANK_Balance).replace(",", "");
                    if(smsContent.contains("入")){
                        amount = BankCaseUtil.getContent(smsContent, BankCaseUtil.SSZBANK_Amount).replace(",", "");
                        transType = TransTypeEnum.RRCHARGE.getValue();
                        parseName  = BankCaseUtil.getContent(smsContent, BankCaseUtil.SSZBANK_Name);
                    }else {
                        amount = BankCaseUtil.getContent(smsContent, BankCaseUtil.SSZBANK_Trans_Amount).replace(",", "");
                        parseName  = BankCaseUtil.getContent(smsContent, BankCaseUtil.SSZBANK_Trans_Name);
                        transType = TransTypeEnum.TRANSFER.getValue();
                    }
                    card4EndNo = BankCaseUtil.getContent(smsContent, BankCaseUtil.SSZBANK_Card4End);
                    time = BankCaseUtil.getContent(smsContent, BankCaseUtil.SSZBANK_Time);
                    dateTime = time.replace("月", "-").replace("日", " ").replace("时",":").replace("分", ":00");
                    dateTime =  CommonUtil.replaceChinese(dateTime, "");
                    transTime = new Integer(calendar.get(Calendar.YEAR)).toString().concat("-"+dateTime);
                    bankInfo.put ("amount",amount);
                    bankInfo.put("balance", balance);
                    bankInfo.put ("card4EndNo",card4EndNo);
                    bankInfo.put ("time",transTime);
                    bankInfo.put ("transType",transType);
                    bankInfo.put ("name",parseName);
                    return  bankInfo;
                case "NXBANK":
                    amount = BankCaseUtil.getContent(smsContent, BankCaseUtil.NXBANK_Amount).replace(",", "");
                    balance = BankCaseUtil.getContent(smsContent, BankCaseUtil.NXBANK_Balance).replace(",", "");
                    if(smsContent.contains("入")){
                        transType = TransTypeEnum.RRCHARGE.getValue();
                        parseName  = BankCaseUtil.getContent(smsContent, BankCaseUtil.NXBANK_Name);
                    }else {
                        transType = TransTypeEnum.TRANSFER.getValue();
                        parseName  = BankCaseUtil.getContent(smsContent, BankCaseUtil.NXBANK_Trans_Name);
                    }
                    card4EndNo = BankCaseUtil.getContent(smsContent, BankCaseUtil.NXBANK_Card4End);

                    time = BankCaseUtil.getContent(smsContent, BankCaseUtil.NXBANK_Time);
                    dateTime = time.replace("月", "-").replace("日", " ").replace("时",":").replace("分", ":00");
                    dateTime =  CommonUtil.replaceChinese(dateTime, "");
                    bankInfo.put ("amount",amount);
                    bankInfo.put("balance", balance);
                    bankInfo.put ("card4EndNo",card4EndNo);
                    bankInfo.put ("time",dateTime);
                    bankInfo.put ("transType",transType);
                    bankInfo.put ("name",parseName);
                    return  bankInfo;
                case "NJB":
                    if(smsContent.contains("入") || smsContent.contains("收")){
                        balance = BankCaseUtil.getContent(smsContent, BankCaseUtil.NJB_Balance).replace(",", "");
                        amount = BankCaseUtil.getContent(smsContent, BankCaseUtil.NJB_Amount).replace(",", "");
                        transType = TransTypeEnum.RRCHARGE.getValue();
                        parseName  = BankCaseUtil.getContent(smsContent, BankCaseUtil.NJB_Name);
                    }else {
                        balance = BankCaseUtil.getContent(smsContent, BankCaseUtil.NJB_Trans_Balance).replace(",", "");
                        amount = BankCaseUtil.getContent(smsContent, BankCaseUtil.NJB_Trans_Amount).replace(",", "");
                        transType = TransTypeEnum.TRANSFER.getValue();
                        parseName  = BankCaseUtil.getContent(smsContent, BankCaseUtil.NJB__Trans_Name);
                    }
                    card4EndNo = BankCaseUtil.getContent(smsContent, BankCaseUtil.NJB_Card4End);
                    time = BankCaseUtil.getContent(smsContent, BankCaseUtil.NJB_Time);
                    dateTime = time.replace("月", "-").replace("日", " ").replace("时",":").replace("分", ":00");
                    dateTime =  CommonUtil.replaceChinese(dateTime, "");
                    transTime = new Integer(calendar.get(Calendar.YEAR)).toString().concat("-"+dateTime);
                    bankInfo.put ("amount",amount);
                    bankInfo.put("balance", balance);
                    bankInfo.put ("card4EndNo",card4EndNo);
                    bankInfo.put ("time",transTime);
                    bankInfo.put ("transType",transType);
                    bankInfo.put ("name",parseName);
                    return  bankInfo;
                case "BCOM":

                    parseName  = BankCaseUtil.getContent(smsContent, BankCaseUtil.BCOM_Name);
                    if(smsContent.contains("入") || smsContent.contains("收")){
                        transType = TransTypeEnum.RRCHARGE.getValue();
                        balance = BankCaseUtil.getContent(smsContent, BankCaseUtil.BCOM_Balance).replace(",", "");
                        amount = BankCaseUtil.getContent(smsContent, BankCaseUtil.BCOM_Amount).replace(",", "");
                        time = BankCaseUtil.getContent(smsContent, BankCaseUtil.BCOM_Time).replace(new Integer(calendar.get(Calendar.YEAR)).toString().concat("年"), "");
                        card4EndNo = BankCaseUtil.getContent(smsContent, BankCaseUtil.BCOM_Card4End).replace("*", "");

                    }else {
                        balance = BankCaseUtil.getContent(smsContent, BankCaseUtil.BCOM_TRANS_Balance).replace(",", "");
                        amount = BankCaseUtil.getContent(smsContent, BankCaseUtil.BCOM_TRANS_Amount).replace(",", "");
                        transType = TransTypeEnum.TRANSFER.getValue();
                        time = BankCaseUtil.getContent(smsContent, BankCaseUtil.BCOM_Trans_Time);
                        card4EndNo = BankCaseUtil.getContent(smsContent, BankCaseUtil.BCOM__TRANS_Card4End).replace("*", "");

                    }
                    card4EndNo = CommonUtil.replaceChinese(card4EndNo, "");
                    balance = CommonUtil.replaceChinese(balance, "");
                    amount = CommonUtil.replaceChinese(amount, "");
                    dateTime = time.replace("月", "-").replace("日", " ").replace("时",":").replace("分", ":00");
                    dateTime =  CommonUtil.replaceChinese(dateTime, "");
                    transTime = new Integer(calendar.get(Calendar.YEAR)).toString().concat("-"+dateTime);
                    bankInfo.put ("amount",amount);
                    bankInfo.put("balance", balance);
                    bankInfo.put ("card4EndNo",card4EndNo);
                    bankInfo.put ("time",transTime);
                    bankInfo.put ("transType",transType);
                    bankInfo.put ("name",parseName);
                    return  bankInfo;
                default:
                    return bankInfo;
            }

        } catch (Exception e) {
            logger.error ("解析银行卡信息异常: {}",smsContent,e);
        }finally {
            try {
                if(bankInfo !=null && !bankInfo.isEmpty () && bankInfo.containsKey ("time")){
                    if(StringUtils.isNotEmpty(bankInfo.getString("time"))){
                        long gmtCreate = bankformat.parse(bankInfo.getString ("time")).getTime();
                        bankInfo.put ("gmtCreate",gmtCreate);
                    }
                }
                if(null != bankInfo.get("amount")){
                    bankInfo.put ("amount",bankInfo.getString("amount").replace(",",""));
                }
            } catch (ParseException e) {
                logger.error ("再次解析银行卡日期异常: {}|{}",card4EndNo,smsContent);
            }
        }
        return bankInfo;
    }

    private static boolean isThird(String smsContent) {
        if(smsContent.contains("支付宝")
            || smsContent.contains("微信")
                || smsContent.contains("抖音")
                || smsContent.contains("京东")
        ){
            return  true;
        }
        return  false;
    }

    private static String getThirdName(String smsContent) {
        if(smsContent.contains("支付宝")
        ){
            return  "支付宝";
        }
        if(smsContent.contains("微信")
        ){
            return  "支付宝";
        }
        return  "";
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

    /**
     * @描述:获取正则内容
     * @作者:nada
     * @时间:2019/4/17
     **/
    public static String getContent1(String content, Pattern pattern){
        StringBuffer sb = new StringBuffer();
        Matcher m = pattern.matcher(content);
        while (m.find()) {
            int i = 0;
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
