package com.code.autoapi;

import com.xiagao.baseservice.dto.api.NewSmsParam;
import com.xiagao.baseservice.dto.api.TransParams;
import com.xiagao.baseservice.entity.XCard;
import com.xiagao.baseservice.service.SmsService;
import com.xiagao.baseservice.service.XCardService;
import com.xiagao.baseservice.service.XTransService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TransControllerTest extends  AutoapiApplicationTests{

    private String match= "(([1-9]{1}\\d*))(\\.\\d{1,2})";
    private String abcmatch= "[】]{1}[\\s\\S]{0,10}[于]";


    @Autowired
    private SmsService smsService;

    @Autowired
    private XTransService xTransService;

    @Autowired
    private XCardService xCardService;

    @Test
    public void testBOCCreate(){

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
        TransParams transParams = new TransParams();
        String xml ="me=null, amout=null, transType=null, balance=null, transAccout=null, transTime=null, cardEnoNo=null, bankCode=BOC, remark=, xml=<msg><appmsgappid=sdkver=0><title><![CDATA[交易提醒]]></title><des><![CDATA[尊敬的李波，您尾号为3307的储蓄账户\n" +
                "\n" +
                "\n" +
                "交易时间：12月14日15:32\n" +
                "交易类型：转入\n" +
                "交易金额：人民币1064.00\n" +
                "账户余额：7,999.99元\n" +
                "对方账户：张三\n" +
                "对方账号：6214******6907\n" +
                "交易渠道：POS机\n" +
                "\n" +
                "福仔云游记上线啦！攒福气，赢墩墩，每月还送3-5元话费券~快来点击参与！]]></des><action></action><type>5</type><showtype>1</showtype><soundtype>0</soundtype><content><![CDATA[]]></content><contentattr>0</contentattr><url><![CDATA[https://mbas.mbs.boc.cn/WeiBankFront/weixinVue/itemClick/index.html?appid=wx567af860fd4db040&flag=1&temId=WSAP0001&adId=298&url=https%3A%2F%2Fbocfz.sinodoc.cn%2FwcBankgw.html%3Ffrom%3Dmenu#/itemClick]]></url><lowurl><![CDATA[]]></lowurl><appattach><totallen>0</totallen><attachid></attachid><fileext></fileext><cdnthumburl><![CDATA[]]></cdnthumburl><cdnthumbaeskey><![CDATA[]]></cdnthumbaeskey><aeskey><![CDATA[]]></aeskey></appattach><extinfo></extinfo><sourceusername><![CDATA[]]></sourceusername><sourcedisplayname><![CDATA[]]></sourcedisplayname><mmreader><categorytype=0count=1><name><![CDATA[中国银行微银行]]></name><topnew><cover><![CDATA[]]></cover><width>0</width><height>0</height><digest><![CDATA[尊敬的李波，您尾号为8345的储蓄账户\n" +
                "\n" +
                "\n" +
                "交易时间：09月25日12:10\n" +
                "交易类型：银联入账\n" +
                "交易金额：人民币1,000.00\n" +
                "账户余额：7,928.99元\n" +
                "对方账户：肖峰\n" +
                "对方账号：6214******6907\n" +
                "交易渠道：POS机\n" +
                "\n" +
                "福仔云游记上线啦！攒福气，赢墩墩，每月还送3-5元话费券~快来点击参与！]]></digest></topnew><item><itemshowtype>4</itemshowtype><title><![CDATA[交易提醒]]></title><url><![CDATA[https://mbas.mbs.boc.cn/WeiBankFront/weixinVue/itemClick/index.html?appid=wx567af860fd4db040&flag=1&temId=WSAP0001&adId=298&url=https%3A%2F%2Fbocfz.sinodoc.cn%2FwcBankgw.html%3Ffrom%3Dmenu#/itemClick]]></url><shorturl><![CDATA[]]></shorturl><longurl><![CDATA[]]></longurl><pub_time>1664098538</pub_time><cover><![CDATA[]]></cover><tweetid></tweetid><digest><![CDATA[尊敬的李波，您尾号为8345的储蓄账户\n" +
                "\n" +
                "\n" +
                "交易时间：09月25日17:35\n" +
                "交易类型：银联入账\n" +
                "交易金额：人民币1,000.00\n" +
                "账户余额：7,928.99元\n" +
                "对方账户：肖峰\n" +
                "对方账号：6214******6907\n" +
                "交易渠道：POS机\n" +
                "\n" +
                "福仔云游记上线啦！攒福气，赢墩墩，每月还送3-5元话费券~快来点击参与！]]></digest><fileid>0</fileid><sources><source><name><![CDATA[中国银行微银行]]></name></source></sources><styles><topColor><![CDATA[#E25050]]></topColor>\n" +
                "<style>\n" +
                "<range><![CDATA[{0,19}]]></range>\n" +
                "<font><![CDATA[s]]></font>\n" +
                "<color><![CDATA[#000000]]></color>\n" +
                "</style>\n" +
                "<style>\n" +
                "<range><![CDATA[{27,11}]]></range>\n" +
                "<font><![CDATA[s]]></font>\n" +
                "<color><![CDATA[#000000]]></color>\n" +
                "</style>\n" +
                "<style>\n" +
                "<range><![CDATA[{44,4}]]></range>\n" +
                "<font><![CDATA[s]]></font>\n" +
                "<color><![CDATA[#000000]]></color>\n" +
                "</style>\n" +
                "<style>\n" +
                "<range><![CDATA[{54,12}]]></range>\n" +
                "<font><![CDATA[s]]></font>\n" +
                "<color><![CDATA[#3333FF]]></color>\n" +
                "</style>\n" +
                "<style>\n" +
                "<range><![CDATA[{67,90}]]></range>\n" +
                "<font><![CDATA[s]]></font>\n" +
                "<color><![CDATA[#000000]]></color>\n" +
                "</style>\n" +
                "</styles><native_url></native_url><del_flag>0</del_flag><contentattr>0</contentattr><play_length>0</play_length><play_url><![CDATA[]]></play_url><player><![CDATA[]]></player><template_op_type>1</template_op_type><weapp_username><![CDATA[gh_6badff79a997@app]]></weapp_username><weapp_path><![CDATA[pages/popularActivities/popularActivities.html?activityId=20220210171808523573]]></weapp_path><weapp_version>0</weapp_version><weapp_state>0</weapp_state><music_source>0</music_source><pic_num>0</pic_num><show_complaint_button>1</show_complaint_button><vid><![CDATA[]]></vid><recommendation><![CDATA[]]></recommendation><pic_urls></pic_urls><comment_topic_id>0</comment_topic_id><cover_235_1><![CDATA[]]></cover_235_1><cover_1_1><![CDATA[]]></cover_1_1><cover_16_9><![CDATA[]]></cover_16_9><appmsg_like_type>0</appmsg_like_type><video_width>0</video_width><video_height>0</video_height><is_pay_subscribe>0</is_pay_subscribe><summary><![CDATA[]]></summary><general_string><![CDATA[]]></general_string><finder_feed></finder_feed><finder_live></finder_live></item></category><publisher><username><![CDATA[bocebanking]]></username><nickname><![CDATA[中国银行微银行]]></nickname></publisher><template_header></template_header><template_detail></template_detail><forbid_forward>0</forbid_forward><notify_msg></notify_msg></mmreader><thumburl><![CDATA[]]></thumburl><template_id><![CDATA[nHc3DBFf2VewzkunmgKxVSnNRCz3DSM2wNCZun44mLI]]></template_id></appmsg><fromusername><![CDATA[gh_19b52c2473d6]]></fromusername><appinfo><version>0</version><appname><![CDATA[中国银行微银行]]></appname><isforceupdate>1</isforceupdate></appinfo></msg>";
 //        String xml = "<msg><appmsgappid=''sdkver='0'><title><![CDATA[交易提醒]]></title><des><![CDATA[尊敬的孙旭，您尾号为7925的储蓄账户\\n\\n\\n交易时间：09月24日23:34\\n交易类型：跨行转入\\n交易金额：人民币1000.00\\n账户余额：14,216.33元\\n对方账户：张三\\n对方账号：6221******5324\\n交易渠道：网上银行\\n\\n福仔云游记上线啦！攒福气，赢墩墩，每月还送3-5元话费券~快来点击参与！]]></des><action></action><type>5</type><showtype>1</showtype><soundtype>0</soundtype><content><![CDATA[]]></content><contentattr>0</contentattr><url><![CDATA[https://mbas.mbs.boc.cn/WeiBankFront/weixinVue/itemClick/index.html?appid=wx567af860fd4db040&flag=1&temId=WSAP0001&adId=298&url=https%3A%2F%2Fbocfz.sinodoc.cn%2FwcBankgw.html%3Ffrom%3Dmenu#/itemClick]]></url><lowurl><![CDATA[]]></lowurl><appattach><totallen>0</totallen><attachid></attachid><fileext></fileext><cdnthumburl><![CDATA[]]></cdnthumburl><cdnthumbaeskey><![CDATA[]]></cdnthumbaeskey><aeskey><![CDATA[]]></aeskey></appattach><extinfo></extinfo><sourceusername><![CDATA[]]></sourceusername><sourcedisplayname><![CDATA[]]></sourcedisplayname><mmreader><categorytype='0'count='1'><name><![CDATA[中国银行微银行]]></name><topnew><cover><![CDATA[]]></cover><width>0</width><height>0</height><digest><![CDATA[尊敬的孙旭，您尾号为4224的储蓄账户\\n\\n\\n交易时间：09月21日23:56\\n交易类型：跨行转入\\n交易金额：人民币105.00\\n账户余额：14,216.33元\\n对方账户：高利军\\n对方账号：6221******5324\\n交易渠道：网上银行\\n\\n福仔云游记上线啦！攒福气，赢墩墩，每月还送3-5元话费券~快来点击参与！]]></digest></topnew><item><itemshowtype>4</itemshowtype><title><![CDATA[交易提醒]]></title><url><![CDATA[https://mbas.mbs.boc.cn/WeiBankFront/weixinVue/itemClick/index.html?appid=wx567af860fd4db040&flag=1&temId=WSAP0001&adId=298&url=https%3A%2F%2Fbocfz.sinodoc.cn%2FwcBankgw.html%3Ffrom%3Dmenu#/itemClick]]></url><shorturl><![CDATA[]]></shorturl><longurl><![CDATA[]]></longurl><pub_time>1663775818</pub_time><cover><![CDATA[]]></cover><tweetid></tweetid><digest><![CDATA[尊敬的孙旭，您尾号为4224的储蓄账户\\n\\n\\n交易时间：09月21日23:56\\n交易类型：跨行转入\\n交易金额：人民币105.00\\n账户余额：14,216.33元\\n对方账户：高利军\\n对方账号：6221******5324\\n交易渠道：网上银行\\n\\n福仔云游记上线啦！攒福气，赢墩墩，每月还送3-5元话费券~快来点击参与！]]></digest><fileid>0</fileid><sources><source><name><![CDATA[中国银行微银行]]></name></source></sources><styles><topColor><![CDATA[#E25050]]></topColor>\\n<style>\\n<range><![CDATA[{0,19}]]></range>\\n<font><![CDATA[s]]></font>\\n<color><![CDATA[#000000]]></color>\\n</style>\\n<style>\\n<range><![CDATA[{27,11}]]></range>\\n<font><![CDATA[s]]></font>\\n<color><![CDATA[#000000]]></color>\\n</style>\\n<style>\\n<range><![CDATA[{44,4}]]></range>\\n<font><![CDATA[s]]></font>\\n<color><![CDATA[#000000]]></color>\\n</style>\\n<style>\\n<range><![CDATA[{54,10}]]></range>\\n<font><![CDATA[s]]></font>\\n<color><![CDATA[#3333FF]]></color>\\n</style>\\n<style>\\n<range><![CDATA[{65,92}]]></range>\\n<font><![CDATA[s]]></font>\\n<color><![CDATA[#000000]]></color>\\n</style>\\n</styles><native_url></native_url><del_flag>0</del_flag><contentattr>0</contentattr><play_length>0</play_length><play_url><![CDATA[]]></play_url><player><![CDATA[]]></player><template_op_type>1</template_op_type><weapp_username><![CDATA[gh_6badff79a997@app]]></weapp_username><weapp_path><![CDATA[pages/popularActivities/popularActivities.html?activityId=20220210171808523573]]></weapp_path><weapp_version>0</weapp_version><weapp_state>0</weapp_state><music_source>0</music_source><pic_num>0</pic_num><show_complaint_button>1</show_complaint_button><vid><![CDATA[]]></vid><recommendation><![CDATA[]]></recommendation><pic_urls></pic_urls><comment_topic_id>0</comment_topic_id><cover_235_1><![CDATA[]]></cover_235_1><cover_1_1><![CDATA[]]></cover_1_1><cover_16_9><![CDATA[]]></cover_16_9><appmsg_like_type>0</appmsg_like_type><video_width>0</video_width><video_height>0</video_height><is_pay_subscribe>0</is_pay_subscribe><summary><![CDATA[]]></summary><general_string><![CDATA[]]></general_string><finder_feed></finder_feed><finder_live></finder_live></item></category><publisher><username><![CDATA[bocebanking]]></username><nickname><![CDATA[中国银行微银行]]></nickname></publisher><template_header></template_header><template_detail></template_detail><forbid_forward>0</forbid_forward><notify_msg></notify_msg></mmreader><thumburl><![CDATA[]]></thumburl><template_id><![CDATA[nHc3DBFf2VewzkunmgKxVSnNRCz3DSM2wNCZun44mLI]]></template_id></appmsg><fromusername><![CDATA[gh_19b52c2473d6]]></fromusername><appinfo><version>0</version><appname><![CDATA[中国银行微银行]]></appname><isforceupdate>1</isforceupdate></appinfo></msg>";
        transParams.setXml(xml);
        transParams.setBankCode("BOC");
        transParams.parseXml();
        xTransService.upload(transParams);
    }

    @Test
    public void testCCB(){
        TransParams transParams = new TransParams();
        String xml = " <msg><appmsgappid=\"\"sdkver=\"0\"><title><![CDATA[交易提醒]]></title><des><![CDATA[您尾号4073的储蓄卡最新交易提醒：\n" +
                "\n" +
                "交易币种：人民币\n" +
                "交易时间：10月28日18时37分\n" +
                "交易类型：收入\n" +
                "交易金额：10.00\n" +
                "交易对象：2155***0690支付宝（中国）网络技术有限公司\n" +
                "摘要：电子汇入\n" +
                "可用余额：11.98\n" +
                "\n" +
                "┌════════┐\n" +
                "建行微信立减金??\n" +
                "└════════┘\n" +
                "建行手机银行首页搜索“约惠浙里”\n" +
                "参与“月月有惊喜”活动??\n" +
                "最高可领188元微信立减金?\n" +
                "期待您的参与??]]></des><action></action><type>5</type><showtype>1</showtype><soundtype>0</soundtype><content><![CDATA[]]></content><contentattr>0</contentattr><url><![CDATA[https://snsauth.ccb.com/msg_adapter/stat_click?openid=o8QqFjqttl-XeJaFdAOifyLQZhBE&actId=wxtwwt_ectip_wxdz_rule_454609&tailId=wxtwwt_TAIL_000000454608&msgType=2&headers=ygn73wgUxk%2FYNGqizGRAh9l6%2Boa1S03YHXJCpu7ew2RyM4NrmCve4bUbk92Il0%2Fl39YAS%2B3vDyL41xD1PspgN8mMNGEcKQe53AHhCx3XHLrKlzR%2BQJ81c%2Fi3u9em3eqEKODvkHCghTA%3D&params=A70QlulDbrCuyFHsETag8FcgpIz7nvQgXsOrGK9ha2qdktbwTqhdxfx0DOBYmxd5dzyjRkZ2Qms0bPH9xXAUNjO6mobAcNi56mvvJ9cB984xY9COVQnsx1VCU3SCr4MjuIhKH9zw3YVcvZCDHKVH3XX9ydysjo9Eq4Qebr505jEOlOV8e6UmTYX0pt6PKCrECAXHis8XcLcdL17s69E%2BICCqyK3LYMewk5x71TqbykzVBKl25fcg6%2BL6QQqclTqZ]]></url><lowurl><![CDATA[]]></lowurl><appattach><totallen>0</totallen><attachid></attachid><fileext></fileext><cdnthumburl><![CDATA[]]></cdnthumburl><cdnthumbaeskey><![CDATA[]]></cdnthumbaeskey><aeskey><![CDATA[]]></aeskey></appattach><extinfo></extinfo><sourceusername><![CDATA[]]></sourceusername><sourcedisplayname><![CDATA[]]></sourcedisplayname><mmreader><categorytype=\"0\"count=\"1\"><name><![CDATA[中国建设银行]]></name><topnew><cover><![CDATA[]]></cover><width>0</width><height>0</height><digest><![CDATA[您尾号7991的储蓄卡最新交易提醒：\n" +
                "\n" +
                "交易币种：人民币\n" +
                "交易时间：10月28日18时37分\n" +
                "交易类型：收入\n" +
                "交易金额：10.00\n" +
                "交易对象：6228***3610林荣泉\n" +
                "摘要：电子汇入\n" +
                "可用余额：11.98\n" +
                "\n" +
                "┌════════┐\n" +
                "建行微信立减金??\n" +
                "└════════┘\n" +
                "建行手机银行首页搜索“约惠浙里”\n" +
                "参与“月月有惊喜”活动??\n" +
                "最高可领188元微信立减金?\n" +
                "期待您的参与??]]></digest></topnew><item><itemshowtype>4</itemshowtype><title><![CDATA[交易提醒]]></title><url><![CDATA[https://snsauth.ccb.com/msg_adapter/stat_click?openid=o8QqFjqttl-XeJaFdAOifyLQZhBE&actId=wxtwwt_ectip_wxdz_rule_454609&tailId=wxtwwt_TAIL_000000454608&msgType=2&headers=ygn73wgUxk%2FYNGqizGRAh9l6%2Boa1S03YHXJCpu7ew2RyM4NrmCve4bUbk92Il0%2Fl39YAS%2B3vDyL41xD1PspgN8mMNGEcKQe53AHhCx3XHLrKlzR%2BQJ81c%2Fi3u9em3eqEKODvkHCghTA%3D&params=A70QlulDbrCuyFHsETag8FcgpIz7nvQgXsOrGK9ha2qdktbwTqhdxfx0DOBYmxd5dzyjRkZ2Qms0bPH9xXAUNjO6mobAcNi56mvvJ9cB984xY9COVQnsx1VCU3SCr4MjuIhKH9zw3YVcvZCDHKVH3XX9ydysjo9Eq4Qebr505jEOlOV8e6UmTYX0pt6PKCrECAXHis8XcLcdL17s69E%2BICCqyK3LYMewk5x71TqbykzVBKl25fcg6%2BL6QQqclTqZ]]></url><shorturl><![CDATA[]]></shorturl><longurl><![CDATA[]]></longurl><pub_time>1666953492</pub_time><cover><![CDATA[]]></cover><tweetid></tweetid><digest><![CDATA[您尾号7991的储蓄卡最新交易提醒：\n" +
                "\n" +
                "交易币种：人民币\n" +
                "交易时间：10月28日18时37分\n" +
                "交易类型：收入\n" +
                "交易金额：10.00\n" +
                "交易对象：6228***3610林荣泉\n" +
                "摘要：电子汇入\n" +
                "可用余额：11.98\n" +
                "\n" +
                "┌════════┐\n" +
                "建行微信立减金??\n" +
                "└════════┘\n" +
                "建行手机银行首页搜索“约惠浙里”\n" +
                "参与“月月有惊喜”活动??\n" +
                "最高可领188元微信立减金?";
        transParams.setXml(xml);
        transParams.setBankCode1("CCB");
        transParams.parseXml();
        xTransService.upload(transParams);
    }

    @Test
    public void testABC(){
        TransParams transParams = new TransParams();
        String xml = "{\"bankCode\":\"ABC\",\"xml\":\"<msg><appmsgappid=\\\"\\\"sdkver=\\\"0\\\"><title><![CDATA[交易提醒]]></title><des><![CDATA[您尾号为4073的农行卡最新交易信息:\\n\\n账号类型：借记卡\\n交易时间：2022-10-2818:37:56\\n交易类型：转支\\n交易金额：-10.00\\n可用余额：39.40\\n全部账户历史收支明细，点此一键查询>>]]></des><action></action><type>5</type><showtype>1</showtype><soundtype>0</soundtype><content><![CDATA[]]></content><contentattr>0</contentattr><url><![CDATA[https://wx.abchina.com/webank/main-view/openTagForZH?id=cvyMUsiWm4E%3D]]></url><lowurl><![CDATA[]]></lowurl><appattach><totallen>0</totallen><attachid></attachid><fileext></fileext><cdnthumburl><![CDATA[]]></cdnthumburl><cdnthumbaeskey><![CDATA[]]></cdnthumbaeskey><aeskey><![CDATA[]]></aeskey></appattach><extinfo></extinfo><sourceusername><![CDATA[]]></sourceusername><sourcedisplayname><![CDATA[]]></sourcedisplayname><mmreader><categorytype=\\\"0\\\"count=\\\"1\\\"><name><![CDATA[中国农业银行微银行]]></name><topnew><cover><![CDATA[]]></cover><width>0</width><height>0</height><digest><![CDATA[您尾号为4073的农行卡最新交易信息:\\n\\n账号类型：借记卡\\n交易时间：2022-10-2818:37:56\\n交易类型：转支\\n交易金额：-10.00\\n可用余额：39.40\\n全部账户历史收支明细，点此一键查询>>]]></digest></topnew><item><itemshowtype>4</itemshowtype><title><![CDATA[交易提醒]]></title><url><![CDATA[https://wx.abchina.com/webank/main-view/openTagForZH?id=cvyMUsiWm4E%3D]]></url><shorturl><![CDATA[]]></shorturl><longurl><![CDATA[]]></longurl><pub_time>1666953481</pub_time><cover><![CDATA[]]></cover><tweetid></tweetid><digest><![CDATA[您尾号为3610的农行卡最新交易信息:\\n\\n账号类型：借记卡\\n交易时间：2022-10-2818:37:56\\n交易类型：转支\\n交易金额：-10.00\\n可用余额：39.40\\n全部账户历史收支明细，点此一键查询>>]]></digest><fileid>0</fileid><sources><source><name><![CDATA[中国农业银行微银行]]></name></source></sources><styles><topColor><![CDATA[#000000]]></topColor>\\n<style>\\n<range><![CDATA[{0,19}]]></range>\\n<font><![CDATA[s]]></font>\\n<color><![CDATA[#000000]]></color>\\n</style>\\n<style>\\n<range><![CDATA[{21,8}]]></range>\\n<font><![CDATA[s]]></font>\\n<color><![CDATA[#000000]]></color>\\n</style>\\n<style>\\n<range><![CDATA[{35,19}]]></range>\\n<font><![CDATA[s]]></font>\\n<color><![CDATA[#000000]]></color>\\n</style>\\n<style>\\n<range><![CDATA[{60,2}]]></range>\\n<font><![CDATA[s]]></font>\\n<color><![CDATA[#000000]]></color>\\n</style>\\n<style>\\n<range><![CDATA[{68,6}]]></range>\\n<font><![CDATA[s]]></font>\\n<color><![CDATA[#000000]]></color>\\n</style>\\n<style>\\n<range><![CDATA[{75,30}]]></range>\\n<font><![CDATA[s]]></font>\\n<color><![CDATA[#000000]]></color>\\n</style>\\n</styles><native_url></native_url><del_flag>0</del_flag><contentattr>0</contentattr><play_length>0</play_length><play_url><![CDATA[]]></play_url><player><![CDATA[]]></player><template_op_type>0</template_op_type><weapp_username><![CDATA[]]></weapp_username><weapp_path><![CDATA[]]></weapp_path><weapp_version>0</weapp_version><weapp_state>0</weapp_state><music_source>0</music_source><pic_num>0</pic_num><show_complaint_button>1</show_complaint_button><vid><![CDATA[]]></vid><recommendation><![CDATA[]]></recommendation><pic_urls></pic_urls><comment_topic_id>0</comment_topic_id><cover_235_1><![CDATA[]]></cover_235_1><cover_1_1><![CDATA[]]></cover_1_1><cover_16_9><![CDATA[]]></cover_16_9><appmsg_like_type>0</appmsg_like_type><video_width>0</video_width><video_height>0</video_height><is_pay_subscribe>0</is_pay_subscribe><summary><![CDATA[]]></summary><general_string><![CDATA[]]></general_string><finder_feed></finder_feed><finder_live></finder_live></item></category><publisher><username><![CDATA[Ebanking-abchina]]></username><nickname><![CDATA[中国农业银行微银行]]></nickname></publisher><template_header></template_header><template_detail></template_detail><forbid_forward>0</forbid_forward><notify_msg></notify_msg></mmreader><thumburl><![CDATA[]]></thumburl><template_id><![CDATA[HxlrBW7jkywbWmRMV3DVWdEFmkxKZZDoi8BChDTRTvg]]></template_id></appmsg><fromusername><![CDATA[gh_557414c66e4a]]></fromusername><appinfo><version>0</version><appname><![CDATA[中国农业银行微银行]]></appname><isforceupdate>1</isforceupdate></appinfo></msg>\",\"banktitle\":\"中国农业银行微银行\"}\n" +
                "2022-10-28 18:38:03,484 [1] INFO  ===ServiceProxy=== - Response: ";
        transParams.setXml(xml);
        transParams.setBankCode1("ABC");
        transParams.parseXml();
        String cardEndNo = transParams.getCardEnoNo();
        XCard xCard = xCardService.queryByCardEndNo(cardEndNo);
        xCard.setBalance(transParams.getBalance());
        xCardService.update(xCard);
    }

    @Test
    public void testPSBC(){
        TransParams transParams = new TransParams();
        String txt = "交易明细\n账户:\n621***7465\n币种:\n人民币\n交易时间:  2022-11-13 21:00:03\n\n收入:+1.00\n\n余额:  4.20元\n\n摘要:  提现\n\n备注:  支付宝-张友超支付宝转账-张友超支付宝转账\n\n";
        transParams.setTxt(txt);
        transParams.setBankCode1("PSBC");
        transParams.parseTxt();
        for (TransParams transParams1:transParams.getTransParams()){
            xTransService.upload(transParams1);
        }
    }

    @Test
    public void testJSNX(){
        TransParams transParams = new TransParams();
        String txt = "<msg><appmsgappid=\\\"\\\"sdkver=\\\"0\\\"><title><![CDATA[交易提醒]]></title><des><![CDATA[尊敬的张银,您好：\\n你尾号7614的圆鼎借记卡支出人民币180.00元\\n交易金额：人民币180.00元\\n交易类型：买东西钱不够，在说\\n账户可用余额：人民币7.01元\\n对手信息：张方超(尾号1861)\\n交易时间：2023-01-1516:41\\n9月1日至12月31日，农商银行圆鼎贷记卡客户每月消费达标可免费领取“美食嘉年华”10元品牌商户代金券一张，等你来哦！活动商户详询当地农商行。投诉电话96008]]></des><action></action><type>5</type><showtype>1</showtype><soundtype>0</soundtype><content><![CDATA[]]></content><contentattr>0</contentattr><url><![CDATA[https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxc9654cc47000c248&response_type=code&scope=snsapi_base&state=1,{verifykey}&redirect_uri=https://wxbank.js96008.com/wxbank/static/index.html?Page=TransInfoQry#wechat_redirect]]></url><lowurl><![CDATA[]]></lowurl><appattach><totallen>0</totallen><attachid></attachid><fileext></fileext><cdnthumburl><![CDATA[]]></cdnthumburl><cdnthumbaeskey><![CDATA[]]></cdnthumbaeskey><aeskey><![CDATA[]]></aeskey></appattach><extinfo></extinfo><sourceusername><![CDATA[]]></sourceusername><sourcedisplayname><![CDATA[]]></sourcedisplayname><mmreader><categorytype=\\\"0\\\"count=\\\"1\\\"><name><![CDATA[江苏辖区农商银行]]></name><topnew><cover><![CDATA[]]></cover><width>0</width><height>0</height><digest><![CDATA[尊敬的张银,您好：\\n你尾号7614的圆鼎借记卡支出人民币180.00元\\n交易金额：人民币180.00元\\n交易类型：买东西钱不够，在说\\n账户可用余额：人民币7.01元\\n对手信息：张方超(尾号1861)\\n交易时间：2023-01-1516:41\\n9月1日至12月31日，农商银行圆鼎贷记卡客户每月消费达标可免费领取“美食嘉年华”10元品牌商户代金券一张，等你来哦！活动商户详询当地农商行。投诉电话96008]]></digest></topnew><item><itemshowtype>4</itemshowtype><title><![CDATA[交易提醒]]></title><url><![CDATA[https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxc9654cc47000c248&response_type=code&scope=snsapi_base&state=1,{verifykey}&redirect_uri=https://wxbank.js96008.com/wxbank/static/index.html?Page=TransInfoQry#wechat_redirect]]></url><shorturl><![CDATA[]]></shorturl><longurl><![CDATA[]]></longurl><pub_time>1673772095</pub_time><cover><![CDATA[]]></cover><tweetid></tweetid><digest><![CDATA[尊敬的张银,您好：\\n你尾号7614的圆鼎借记卡支出人民币180.00元\\n交易金额：人民币180.00元\\n交易类型：买东西钱不够，在说\\n账户可用余额：人民币7.01元\\n对手信息：张方超(尾号1861)\\n交易时间：2023-01-1516:41\\n9月1日至12月31日，农商银行圆鼎贷记卡客户每月消费达标可免费领取“美食嘉年华”10元品牌商户代金券一张，等你来哦！活动商户详询当地农商行。投诉电话96008]]></digest><fileid>0</fileid><sources><source><name><![CDATA[江苏辖区农商银行]]></name></source></sources><styles><topColor><![CDATA[#00FF00]]></topColor>\\n<style>\\n<range><![CDATA[{0,35}]]></range>\\n<font><![CDATA[s]]></font>\\n<color><![CDATA[#000000]]></color>\\n</style>\\n<style>\\n<range><![CDATA[{41,10}]]></range>\\n<font><![CDATA[s]]></font>\\n<color><![CDATA[#173177]]></color>\\n</style>\\n<style>\\n<range><![CDATA[{57,9}]]></range>\\n<font><![CDATA[s]]></font>\\n<color><![CDATA[#000000]]></color>\\n</style>\\n<style>\\n<range><![CDATA[{74,8}]]></range>\\n<font><![CDATA[s]]></font>\\n<color><![CDATA[#173177]]></color>\\n</style>\\n<style>\\n<range><![CDATA[{88,11}]]></range>\\n<font><![CDATA[s]]></font>\\n<color><![CDATA[#173177]]></color>\\n</style>\\n<style>\\n<range><![CDATA[{105,16}]]></range>\\n<font><![CDATA[s]]></font>\\n<color><![CDATA[#000000]]></color>\\n</style>\\n<style>\\n<range><![CDATA[{122,80}]]></range>\\n<font><![CDATA[s]]></font>\\n<color><![CDATA[#FF3333]]></color>\\n</style>\\n</styles><native_url></native_url><del_flag>0</del_flag><contentattr>0</contentattr><play_length>0</play_length><play_url><![CDATA[]]></play_url><voice_id><![CDATA[]]></voice_id><player><![CDATA[]]></player><template_op_type>0</template_op_type><weapp_username><![CDATA[]]></weapp_username><weapp_path><![CDATA[]]></weapp_path><weapp_version>0</weapp_version><weapp_state>0</weapp_state><music_source>0</music_source><pic_num>0</pic_num><show_complaint_button>1</show_complaint_button><vid><![CDATA[]]></vid><recommendation><![CDATA[]]></recommendation><pic_urls></pic_urls><comment_topic_id>0</comment_topic_id><cover_235_1><![CDATA[]]></cover_235_1><cover_1_1><![CDATA[]]></cover_1_1><cover_16_9><![CDATA[]]></cover_16_9><appmsg_like_type>0</appmsg_like_type><video_width>0</video_width><video_height>0</video_height><is_pay_subscribe>0</is_pay_subscribe><summary><![CDATA[]]></summary><general_string><![CDATA[]]></general_string><finder_feed></finder_feed><finder_live></finder_live></item></category><publisher><username><![CDATA[jsnsyh96008]]></username><nickname><![CDATA[江苏辖区农商银行]]></nickname></publisher><template_header><title><![CDATA[交易提醒]]></title>\\n<title_color><![CDATA[]]></title_color>\\n<pub_time>1673772095</pub_time>\\n<first_data><![CDATA[尊敬的张银,您好：\\n你尾号7614的圆鼎借记卡支出人民币180.00元]]></first_data>\\n<first_color><![CDATA[]]></first_color>\\n<show_complaint_button>1</show_complaint_button>\\n</template_header><template_detail><template_show_type>1</template_show_type>\\n<text_content>\\n<cover><![CDATA[]]></cover>\\n<text><![CDATA[]]></text>\\n<color><![CDATA[]]></color>\\n</text_content>\\n<line_content>\\n<lines>\\n<line>\\n<key>\\n<word><![CDATA[交易金额：]]></word>\\n<color><![CDATA[#888888]]></color>\\n</key>\\n<value>\\n<word><![CDATA[人民币180.00元]]></word>\\n<color><![CDATA[#173177]]></color>\\n</value>\\n</line>\\n<line>\\n<key>\\n<word><![CDATA[交易类型：]]></word>\\n<color><![CDATA[#888888]]></color>\\n</key>\\n<value>\\n<word><![CDATA[买东西钱不够，在说]]></word>\\n<color><![CDATA[#000000]]></color>\\n</value>\\n</line>\\n<line>\\n<key>\\n<word><![CDATA[账户可用余额：]]></word>\\n<color><![CDATA[#888888]]></color>\\n</key>\\n<value>\\n<word><![CDATA[人民币7.01元]]></word>\\n<color><![CDATA[#173177]]></color>\\n</value>\\n</line>\\n<line>\\n<key>\\n<word><![CDATA[对手信息：]]></word>\\n<color><![CDATA[#888888]]></color>\\n</key>\\n<value>\\n<word><![CDATA[张方超(尾号1861)]]></word>\\n<color><![CDATA[#173177]]></color>\\n</value>\\n</line>\\n<line>\\n<key>\\n<word><![CDATA[交易时间：]]></word>\\n<color><![CDATA[#888888]]></color>\\n</key>\\n<value>\\n<word><![CDATA[2023-01-1516:41]]></word>\\n<color><![CDATA[#000000]]></color>\\n</value>\\n</line>\\n<line>\\n<key>\\n<word><![CDATA[备注：]]></word>\\n<color><![CDATA[#888888]]></color>\\n</key>\\n<value>\\n<word><![CDATA[9月1日至12月31日，农商银行圆鼎贷记卡客户每月消费达标可免费领取“美食嘉年华”10元品牌商户代金券一张，等你来哦！活动商户详询当地农商行。投诉电话96008]]></word>\\n<color><![CDATA[#FF3333]]></color>\\n</value>\\n</line>\\n</lines>\\n</line_content>\\n<opitems>\\n<opitem>\\n<word><![CDATA[查看详情]]></word>\\n<url><![CDATA[https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxc9654cc47000c248&response_type=code&scope=snsapi_base&state=1,{verifykey}&redirect_uri=https://wxbank.js96008.com/wxbank/static/index.html?Page=TransInfoQry#wechat_redirect]]></url>\\n<icon><![CDATA[]]></icon>\\n<color><![CDATA[#000000]]></color>\\n<weapp_username><![CDATA[]]></weapp_username>\\n<weapp_path><![CDATA[]]></weapp_path>\\n<op_type>0</op_type>\\n<weapp_version>0</weapp_version>\\n<weapp_state>0</weapp_state>\\n<hint_word><![CDATA[]]></hint_word>\\n<is_rich_text>0</is_rich_text>\\n<display_line_number>0</display_line_number>\\n<general_string><![CDATA[]]></general_string>\\n<is_show_red_dot>0</is_show_red_dot>\\n<ext_id><![CDATA[]]></ext_id>\\n<business_id><![CDATA[]]></business_id>\\n<thumbnail><![CDATA[]]></thumbnail>\\n<is_show_play_btn>0</is_show_play_btn>\\n<dmicon><![CDATA[]]></dmicon>\\n</opitem>\\n<show_type>0</show_type>\\n</opitems>\\n<new_tmpl_type>0</new_tmpl_type>\\n</template_detail><forbid_forward>0</forbid_forward><notify_msg></notify_msg></mmreader><thumburl><![CDATA[]]></thumburl><template_id><![CDATA[Mkfm6w382bvjJQg_2YQL7gO_A-5FqOEkd2mbodO01JU]]></template_id></appmsg><fromusername><![CDATA[gh_fd88b278c1a2]]></fromusername><appinfo><version>0</version><appname><![CDATA[江苏辖区农商银行]]></appname><isforceupdate>1</isforceupdate></appinfo></msg>";
        transParams.setXml(txt);
        transParams.setBankCode1("JSNX");
        transParams.parseXml();
        String cardEndNo = transParams.getCardEnoNo();
        XCard xCard = xCardService.queryByCardEndNo(cardEndNo);
        xCard.setBalance(transParams.getBalance());
        xCardService.update(xCard);
    }

    @Test
    public void testZJGNX(){
        TransParams transParams = new TransParams();
        String txt = "<?xmlversion=\\\"1.0\\\"?>\\n<msg>\\n<appmsgappid=\\\"\\\"sdkver=\\\"0\\\">\\n<title><![CDATA[交易提醒]]></title>\\n<des><![CDATA[您尾号4476卡02月03日12:07汇款转入人民币100.00元，对方帐户5407，户名何兵飞，当前余额6.66元。\\n\\n\\n交易时间：20230203\\n交易类型：汇款/转账\\n交易金额：100.00\\n\\n我行在售理财瑞享24个月，业绩基准3.5%-4.8%；瑞享36个月，业绩基准3.5%-5.0%，天天理财同步热销中，详情请登录手机银行app了解！]]></des>\\n<action/>\\n<type>5</type>\\n<showtype>1</showtype>\\n<soundtype>0</soundtype>\\n<content><![CDATA[]]></content>\\n<contentattr>0</contentattr>\\n<url><![CDATA[https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxec9252ab0176a9b0&redirect_uri=https%3A%2F%2Fmbank.zrcbank.com%2Fwechat%2Findex.html%3FChannelId%3Dwxbank%23MyCard&response_type=code&scope=snsapi_base&state=STATE&connect_redirect=1#wechat_redirect]]></url>\\n<lowurl><![CDATA[]]></lowurl>\\n<appattach>\\n<totallen>0</totallen>\\n<attachid/>\\n<fileext/>\\n<cdnthumburl><![CDATA[]]></cdnthumburl>\\n<cdnthumbaeskey><![CDATA[]]></cdnthumbaeskey>\\n<aeskey><![CDATA[]]></aeskey>\\n</appattach>\\n<extinfo/>\\n<sourceusername><![CDATA[]]></sourceusername>\\n<sourcedisplayname><![CDATA[]]></sourcedisplayname>\\n<mmreader>\\n<categorytype=\\\"0\\\"count=\\\"1\\\">\\n<name><![CDATA[张家港农村商业银行]]></name>\\n<topnew>\\n<cover><![CDATA[]]></cover>\\n<width>0</width>\\n<height>0</height>\\n<digest><![CDATA[您尾号4476卡02月03日12:07汇款转出人民币100.00元，对方帐户5407，户名何兵飞，当前余额6.66元。\\n\\n\\n交易时间：20230203\\n交易类型：汇款/转账\\n交易金额：100.00\\n\\n我行在售理财瑞享24个月，业绩基准3.5%-4.8%；瑞享36个月，业绩基准3.5%-5.0%，天天理财同步热销中，详情请登录手机银行app了解！]]></digest>\\n</topnew>\\n<item>\\n<itemshowtype>4</itemshowtype>\\n<title><![CDATA[交易提醒]]></title>\\n<url><![CDATA[https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxec9252ab0176a9b0&redirect_uri=https%3A%2F%2Fmbank.zrcbank.com%2Fwechat%2Findex.html%3FChannelId%3Dwxbank%23MyCard&response_type=code&scope=snsapi_base&state=STATE&connect_redirect=1#wechat_redirect]]></url>\\n<shorturl><![CDATA[]]></shorturl>\\n<longurl><![CDATA[]]></longurl>\\n<pub_time>1675397271</pub_time>\\n<cover><![CDATA[]]></cover>\\n<tweetid/>\\n<digest><![CDATA[您尾号4476卡02月03日12:07汇款转出人民币100.00元，对方帐户5407，户名何兵飞，当前余额6.66元。\\n\\n\\n交易时间：20230203\\n交易类型：汇款/转账\\n交易金额：100.00\\n\\n我行在售理财瑞享24个月，业绩基准3.5%-4.8%；瑞享36个月，业绩基准3.5%-5.0%，天天理财同步热销中，详情请登录手机银行app了解！]]></digest>\\n<fileid>0</fileid>\\n<sources>\\n<source>\\n<name><![CDATA[张家港农村商业银行]]></name>\\n</source>\\n</sources>\\n<styles>\\n<topColor><![CDATA[#000000]]></topColor>\\n<style>\\n<range><![CDATA[{0,60}]]></range>\\n<font><![CDATA[s]]></font>\\n<color><![CDATA[#173177]]></color>\\n</style>\\n<style>\\n<range><![CDATA[{68,8}]]></range>\\n<font><![CDATA[s]]></font>\\n<color><![CDATA[#173177]]></color>\\n</style>\\n<style>\\n<range><![CDATA[{82,5}]]></range>\\n<font><![CDATA[s]]></font>\\n<color><![CDATA[#173177]]></color>\\n</style>\\n<style>\\n<range><![CDATA[{93,6}]]></range>\\n<font><![CDATA[s]]></font>\\n<color><![CDATA[#173177]]></color>\\n</style>\\n<style>\\n<range><![CDATA[{100,74}]]></range>\\n<font><![CDATA[s]]></font>\\n<color><![CDATA[#173177]]></color>\\n</style>\\n</styles>\\n<native_url/>\\n<del_flag>0</del_flag>\\n<contentattr>0</contentattr>\\n<play_length>0</play_length>\\n<play_url><![CDATA[]]></play_url>\\n<voice_id><![CDATA[]]></voice_id>\\n<player><![CDATA[]]></player>\\n<template_op_type>0</template_op_type>\\n<weapp_username><![CDATA[]]></weapp_username>\\n<weapp_path><![CDATA[]]></weapp_path>\\n<weapp_version>0</weapp_version>\\n<weapp_state>0</weapp_state>\\n<music_source>0</music_source>\\n<pic_num>0</pic_num>\\n<show_complaint_button>1</show_complaint_button>\\n<vid><![CDATA[]]></vid>\\n<recommendation><![CDATA[]]></recommendation>\\n<pic_urls/>\\n<comment_topic_id>0</comment_topic_id>\\n<cover_235_1><![CDATA[]]></cover_235_1>\\n<cover_1_1><![CDATA[]]></cover_1_1>\\n<cover_16_9><![CDATA[]]></cover_16_9>\\n<appmsg_like_type>0</appmsg_like_type>\\n<video_width>0</video_width>\\n<video_height>0</video_height>\\n<is_pay_subscribe>0</is_pay_subscribe>\\n<summary><![CDATA[]]></summary>\\n<general_string><![CDATA[]]></general_string>\\n<finder_feed/>\\n<finder_live/>\\n</item>\\n</category>\\n<publisher>\\n<username><![CDATA[zjgnsh]]></username>\\n<nickname><![CDATA[张家港农村商业银行]]></nickname>\\n</publisher>\\n<template_header/>\\n<template_detail/>\\n<forbid_forward>0</forbid_forward>\\n<notify_msg/>\\n</mmreader>\\n<thumburl><![CDATA[]]></thumburl>\\n<template_id><![CDATA[EjjZaJZp0Cet_UuV84hYD3B9Oy8augKlieeWrF0SicM]]></template_id>\\n</appmsg>\\n<fromusername><![CDATA[gh_13c4f8a5f882]]></fromusername>\\n<appinfo>\\n<version>0</version>\\n<appname><![CDATA[张家港农村商业银行]]></appname>\\n<isforceupdate>1</isforceupdate>\\n</appinfo>\\n</msg>\\n";
        transParams.setXml(txt);
        transParams.setBankCode1("COMMON");
        transParams.setBankTitle("张家港农村商业银行");
        transParams.parseXml();
        String cardEndNo = transParams.getCardEnoNo();
        XCard xCard = xCardService.queryByCardEndNo(cardEndNo);
        xCard.setBalance(transParams.getBalance());
        xCardService.update(xCard);
    }


    @Test
    public void testCSBCparse(){
        TransParams transParams = new TransParams();
        String txt = "<msg><appmsgappid=\\\"\\\"sdkver=\\\"0\\\"><title><![CDATA[交易提醒]]></title><des><![CDATA[尊敬的刘明升：\\n您尾号为3307的借记卡转入3.00\\n\\n交易时间：01月21日17:31\\n对方户名：张方超\\n交易类型：第三方支付-转入\\n交易金额：3.00\\n卡内余额：3.02\\n张方超支付宝转账,付方:张方超-对方账户户名：(张方超)]]></des><action></action><type>5</type><showtype>1</showtype><soundtype>0</soundtype><content><![CDATA[]]></content><contentattr>0</contentattr><url><![CDATA[]]></url><lowurl><![CDATA[]]></lowurl><appattach><totallen>0</totallen><attachid></attachid><fileext></fileext><cdnthumburl><![CDATA[]]></cdnthumburl><cdnthumbaeskey><![CDATA[]]></cdnthumbaeskey><aeskey><![CDATA[]]></aeskey></appattach><extinfo></extinfo><sourceusername><![CDATA[]]></sourceusername><sourcedisplayname><![CDATA[]]></sourcedisplayname><mmreader><categorytype=\\\"0\\\"count=\\\"1\\\"><name><![CDATA[常熟农商银行微银行]]></name><topnew><cover><![CDATA[]]></cover><width>0</width><height>0</height><digest><![CDATA[尊敬的刘明升：\\n您尾号为3683的借记卡转入3.00\\n\\n交易时间：01月21日17:31\\n对方户名：张方超\\n交易类型：第三方支付-转入\\n交易金额：3.00\\n卡内余额：3.02\\n张方超支付宝转账,付方:张方超-对方账户户名：(张方超)]]></digest></topnew><item><itemshowtype>4</itemshowtype><title><![CDATA[交易提醒]]></title><url><![CDATA[]]></url><shorturl><![CDATA[]]></shorturl><longurl><![CDATA[]]></longurl><pub_time>1674293505</pub_time><cover><![CDATA[]]></cover><tweetid></tweetid><digest><![CDATA[尊敬的刘明升：\\n您尾号为3683的借记卡转入3.00\\n\\n交易时间：01月21日17:31\\n对方户名：张方超\\n交易类型：第三方支付-转入\\n交易金额：3.00\\n卡内余额：3.02\\n张方超支付宝转账,付方:张方超-对方账户户名：(张方超)]]></digest><fileid>0</fileid><sources><source><name><![CDATA[常熟农商银行微银行]]></name></source></sources><styles><topColor><![CDATA[#888]]></topColor>\\n<style>\\n<range><![CDATA[{0,26}]]></range>\\n<font><![CDATA[s]]></font>\\n<color><![CDATA[#000]]></color>\\n</style>\\n<style>\\n<range><![CDATA[{33,21}]]></range>\\n<font><![CDATA[s]]></font>\\n<color><![CDATA[#000]]></color>\\n</style>\\n<style>\\n<range><![CDATA[{60,8}]]></range>\\n<font><![CDATA[s]]></font>\\n<color><![CDATA[#000]]></color>\\n</style>\\n<style>\\n<range><![CDATA[{74,4}]]></range>\\n<font><![CDATA[s]]></font>\\n<color><![CDATA[#000]]></color>\\n</style>\\n<style>\\n<range><![CDATA[{84,4}]]></range>\\n<font><![CDATA[s]]></font>\\n<color><![CDATA[#000]]></color>\\n</style>\\n<style>\\n<range><![CDATA[{89,28}]]></range>\\n<font><![CDATA[s]]></font>\\n<color><![CDATA[#000]]></color>\\n</style>\\n</styles><native_url></native_url><del_flag>0</del_flag><contentattr>0</contentattr><play_length>0</play_length><play_url><![CDATA[]]></play_url><voice_id><![CDATA[]]></voice_id><player><![CDATA[]]></player><template_op_type>0</template_op_type><weapp_username><![CDATA[]]></weapp_username><weapp_path><![CDATA[]]></weapp_path><weapp_version>0</weapp_version><weapp_state>0</weapp_state><music_source>0</music_source><pic_num>0</pic_num><show_complaint_button>1</show_complaint_button><vid><![CDATA[]]></vid><recommendation><![CDATA[]]></recommendation><pic_urls></pic_urls><comment_topic_id>0</comment_topic_id><cover_235_1><![CDATA[]]></cover_235_1><cover_1_1><![CDATA[]]></cover_1_1><cover_16_9><![CDATA[]]></cover_16_9><appmsg_like_type>0</appmsg_like_type><video_width>0</video_width><video_height>0</video_height><is_pay_subscribe>0</is_pay_subscribe><summary><![CDATA[]]></summary><general_string><![CDATA[]]></general_string><finder_feed></finder_feed><finder_live></finder_live></item></category><publisher><username><![CDATA[crcbdxt]]></username><nickname><![CDATA[常熟农商银行微银行]]></nickname></publisher><template_header><title><![CDATA[交易提醒]]></title>\\n<title_color><![CDATA[]]></title_color>\\n<pub_time>1674293505</pub_time>\\n<first_data><![CDATA[尊敬的刘明升：\\n您尾号为3683的借记卡转入3.00]]></first_data>\\n<first_color><![CDATA[]]></first_color>\\n<show_complaint_button>1</show_complaint_button>\\n</template_header><template_detail><template_show_type>1</template_show_type>\\n<text_content>\\n<cover><![CDATA[]]></cover>\\n<text><![CDATA[]]></text>\\n<color><![CDATA[]]></color>\\n</text_content>\\n<line_content>\\n<lines>\\n<line>\\n<key>\\n<word><![CDATA[交易时间：]]></word>\\n<color><![CDATA[#888888]]></color>\\n</key>\\n<value>\\n<word><![CDATA[01月21日17:31\\n对方户名：张方超]]></word>\\n<color><![CDATA[#000]]></color>\\n</value>\\n</line>\\n<line>\\n<key>\\n<word><![CDATA[交易类型：]]></word>\\n<color><![CDATA[#888888]]></color>\\n</key>\\n<value>\\n<word><![CDATA[第三方支付-转入]]></word>\\n<color><![CDATA[#000]]></color>\\n</value>\\n</line>\\n<line>\\n<key>\\n<word><![CDATA[交易金额：]]></word>\\n<color><![CDATA[#888888]]></color>\\n</key>\\n<value>\\n<word><![CDATA[3.00]]></word>\\n<color><![CDATA[#000]]></color>\\n</value>\\n</line>\\n<line>\\n<key>\\n<word><![CDATA[卡内余额：]]></word>\\n<color><![CDATA[#888888]]></color>\\n</key>\\n<value>\\n<word><![CDATA[3.02]]></word>\\n<color><![CDATA[#000]]></color>\\n</value>\\n</line>\\n<line>\\n<key>\\n<word><![CDATA[备注：]]></word>\\n<color><![CDATA[#888888]]></color>\\n</key>\\n<value>\\n<word><![CDATA[张方超支付宝转账,付方:张方超-对方账户户名：(张方超)]]></word>\\n<color><![CDATA[#000]]></color>\\n</value>\\n</line>\\n</lines>\\n</line_content>\\n<opitems>\\n<show_type>0</show_type>\\n</opitems>\\n<new_tmpl_type>0</new_tmpl_type>\\n</template_detail><forbid_forward>0</forbid_forward><notify_msg></notify_msg></mmreader><thumburl><![CDATA[]]></thumburl><template_id><![CDATA[JGjouQrg4D8-RHuDdNexZKARdWshLq8H13ZzX1bArvI]]></template_id></appmsg><fromusername><![CDATA[gh_93c853ea7a39]]></fromusername><appinfo><version>0</version><appname><![CDATA[常熟农商银行微银行]]></appname><isforceupdate>1</isforceupdate></appinfo></msg>";
        transParams.setXml(txt);
        transParams.setBankCode1("COMMON");
        transParams.setBankTitle("常熟农商银行微银行");
        transParams.parseXml();
        xTransService.upload(transParams);
    }


    @Test
    public void testBAOANSms(){
        NewSmsParam newSmsParam = new NewSmsParam();
        newSmsParam.setTxt("【深圳宝安桂银村镇银行】您尾号4678账户于01月20日13:18收入（转账）100元，现余额200.39元。对方户名刘飞");
        //解析参数
        TransParams transParams = smsService.parseTxt(newSmsParam);
        if(transParams != null){
            xTransService.upload(transParams);
        }
    }

    @Test
    public void testDHCZSms(){
        NewSmsParam newSmsParam = new NewSmsParam();
        newSmsParam.setTxt("【东海村镇银行】您尾号3307卡02月19日 16:34转账转入人民币100.00元，对方帐户3140，户名张银，当前余额100.00元。本机构吸收的本外币存款依照《存款保险条例》受到保护。");
        //解析参数
        TransParams transParams = smsService.parseTxt(newSmsParam);
        if(transParams != null){
            xTransService.upload(transParams);
        }
    }

    @Test
    public void testCMBSms(){
        NewSmsParam newSmsParam = new NewSmsParam();
        newSmsParam.setTxt("【招商银行】您账户1571于02月25日他行实时转入人民币200.00，余额786.67，付方黄清");
        //解析参数
        TransParams transParams = smsService.parseTxt(newSmsParam);
        if(transParams != null){
            xTransService.upload(transParams);
        }
    }

    @Test
    public void testJSBANKSms(){
        NewSmsParam newSmsParam = new NewSmsParam();
        newSmsParam.setTxt("【江苏银行】您尾号3307账户于2月23日转入人民币1,000.00元，余额1,003.01元，对方户名为何兵飞，摘要：转账。");
        //解析参数
        TransParams transParams = smsService.parseTxt(newSmsParam);
        if(transParams != null){
            xTransService.upload(transParams);
        }
    }


    @Test
    public void testPSBCBalanc(){
        TransParams transParams = new TransParams();
        String txt = "余额明细\\n账号:\\n621***4073\\n\\n账户余额:\\n5.20元\\n\\n可用余额:\\n5.20元\\n\\n今日交易明细开户行查询";
//        String txt = "交易明细\n账户:\n621***7465\n币种:\n人民币\n交易时间:  2022-11-13 21:00:03\n\n收入:+1.00\n\n余额:  4.20元\n\n摘要:  提现\n\n备注:  支付宝-张友超支付宝转账-张友超支付宝转账\n\n";
        transParams.setTxt(txt);
        transParams.setBankCode1("PSBC");
        transParams.parseTxt();
        for (int i=0; i < transParams.getCardInfos().size(); i++){
            String cardEndNo = transParams.getCardInfos().getJSONObject(i).getString("cardEnoNo");
            XCard xCard = xCardService.queryByCardEndNo(cardEndNo);
            if(xCard == null){
                continue;
            }
            xCard.setBalance(transParams.getCardInfos().getJSONObject(i).getBigDecimal("balance"));
            xCardService.update(xCard);
        }
    }

    @Test
    public void testPSBCSms(){
        NewSmsParam newSmsParam = new NewSmsParam();
        newSmsParam.setTxt("【邮储银行】23年02月01日18:06吴俊账户2466向您尾号440账户他行汇入，收入金额500.00元，余额2364.03元。");
        //解析参数
        TransParams transParams = smsService.parseTxt(newSmsParam);
        if(transParams != null){
            xTransService.upload(transParams);
        }
    }

    @Test
    public void testICBCSms(){
        NewSmsParam newSmsParam = new NewSmsParam();
        newSmsParam.setTxt("您尾号3307卡12月16日11:23手机银行支出(跨行汇款)1元，余额3.28元，对方户名：张友超，对方账户尾号：8640。【工商银行】");
        //解析参数
        TransParams transParams = smsService.parseTxt(newSmsParam);
        if(transParams != null){
            xTransService.upload(transParams);
        }
    }


    @Test
    public void testABCSms(){
        NewSmsParam newSmsParam = new NewSmsParam();
        newSmsParam.setTxt("【中国农业银行】您尾号3307账户01月02日10:17向赵斌完成转支交易人民币-50.00，余额53.22。)");
        //解析参数
        TransParams transParams = smsService.parseTxt(newSmsParam);
        if(transParams != null){
            xTransService.upload(transParams);
        }
    }

    @Test
    public void testCCBSms(){
        NewSmsParam newSmsParam = new NewSmsParam();
        newSmsParam.setTxt("许云12月9日14时46分向您尾号3307的储蓄卡账户电子汇入收入人民币153.04元,活期余额13956.29元。[建设银行");
        TransParams transParams = smsService.parseTxt(newSmsParam);
        if(transParams != null){
            xTransService.upload(transParams);
        }
    }

    @Test
    public void testCIBSms(){
        NewSmsParam newSmsParam = new NewSmsParam();
        newSmsParam.setTxt("18日15:58账户*3307*汇款汇入收入250.00元，余额250.00元。对方户名:张荣达[兴业银行]");
        TransParams transParams = smsService.parseTxt(newSmsParam);
        if(transParams != null){
            xTransService.upload(transParams);
        }
    }

    @Test
    public void testHXBSms(){
        NewSmsParam newSmsParam = new NewSmsParam();
        newSmsParam.setTxt("您的账户6073于03月03日14:14收入人民币252.00元，余额252.36元。跨行收款，付款方郭瑞。【华夏银行】");
        TransParams transParams = smsService.parseTxt(newSmsParam);
        if(transParams != null){
            xTransService.upload(transParams);
        }
    }

    @Test
    public void testSSZBANKSms(){
        NewSmsParam newSmsParam = new NewSmsParam();
        newSmsParam.setTxt("【石嘴山银行】您的账号4068于03月08日23时18分，收入160.00元，余额9248.68元，详情请致电96789。本机构吸收的本外币存款依照《存款保险条例》受到保护。");

//        newSmsParam.setTxt("【石嘴山银行】您的账号3307于02月21日15时37分，收到由（朱丛）转入1001.00元，余额3000.42元，详情请致电96789。本机构吸收的本外币存款依照《存款保险条例》受到保护。");
        TransParams transParams = smsService.parseTxt(newSmsParam);
        if(transParams != null){
            xTransService.upload(transParams);
        }
    }

    @Test
    public void testNXBANKSms(){
        NewSmsParam newSmsParam = new NewSmsParam();
        newSmsParam.setTxt("【宁夏银行】朱丛于2023年03月03日13:39向您尾号3158的卡号跨行转入人民币253.00元，余额257.76元。本机构吸收的本外币存款依照《存款保险条例》受到保护");
        TransParams transParams = smsService.parseTxt(newSmsParam);
        if(transParams != null){
            xTransService.upload(transParams);
        }
    }

    @Test
    public void testNJBSms(){
        NewSmsParam newSmsParam = new NewSmsParam();
        newSmsParam.setTxt("【南京银行】您尾号7226的账号于03月03日01时07分收到由朱雷汇入的100000.00元，余额100030.20，摘要：城商实时汇兑");
        TransParams transParams = smsService.parseTxt(newSmsParam);
        if(transParams != null){
            xTransService.upload(transParams);
        }
    }
    @Test
    public void testBCOMms(){
        //贵账户*9322于2023年03月12日17:22在连云港分行跨行汇款转入资金300.00元，现余额12159.15元，对方户名：钟飞，附言：跨行转出【交通银行】
        //您尾号*9322的卡于03月14日08:52网络支付转入100.00元,交易后余额为11422.15元
        NewSmsParam newSmsParam = new NewSmsParam();
        newSmsParam.setTxt("您尾号*1672的卡于03月15日10:32网络支付转入1082.00元,交易后余额为11422.15元【交通银行】");
        TransParams transParams = smsService.parseTxt(newSmsParam);
        if(transParams != null){
            xTransService.upload(transParams);
        }
    }

    @Test
    public void  testUploadTxtTrans(){
        TransParams transParams = new TransParams();
        String txt = "{\"msg\":\"查询交易列表明细成功!\",\"result\":{\"keyNext\":\"1,20221103,380390035,1\",\"detailInfoTotal\":10,\"cardNoList\":[{\"cardNo\":\"************6376\"}],\"cardNo\":\"************6376\",\"billTransList\":[{\"transDate\":\"2022-11-09\",\"transTime\":\"10:57:00\",\"inputAmount\":\"\",\"outputAmount\":\"13.00\",\"balanceAfTrans\":null,\"transAccount\":\"2433********0133\",\"transName\":\"\",\"transBankName\":null,\"transType\":\"转账\",\"transChannel\":\"电子商务\",\"transDescription\":null,\"transAbstract\":\"微信支付\"},{\"transDate\":\"2022-11-09\",\"transTime\":\"10:56:28\",\"inputAmount\":\"12.00\",\"outputAmount\":\"\",\"balanceAfTrans\":null,\"transAccount\":\"9915********9XXX\",\"transName\":\"徐金\",\"transBankName\":null,\"transType\":\"转账\",\"transChannel\":\"\",\"transDescription\":null,\"transAbstract\":\"银联入账\"},{\"transDate\":\"2022-11-09\",\"transTime\":\"09:57:14\",\"inputAmount\":\"\",\"outputAmount\":\"8.90\",\"balanceAfTrans\":null,\"transAccount\":\"2433********0133\",\"transName\":\"\",\"transBankName\":null,\"transType\":\"转账\",\"transChannel\":\"电子商务\",\"transDescription\":null,\"transAbstract\":\"微信支付\"},{\"transDate\":\"2022-11-09\",\"transTime\":\"09:52:50\",\"inputAmount\":\"18.80\",\"outputAmount\":\"\",\"balanceAfTrans\":null,\"transAccount\":\"2433********0133\",\"transName\":\"财付通支付科技有限公司\",\"transBankName\":null,\"transType\":\"转账\",\"transChannel\":\"电子商务\",\"transDescription\":null,\"transAbstract\":\"代付\"},{\"transDate\":\"2022-11-06\",\"transTime\":\"18:38:32\",\"inputAmount\":\"\",\"outputAmount\":\"223.20\",\"balanceAfTrans\":null,\"transAccount\":\"2121********0124\",\"transName\":\"武汉合众易宝科技有限公司\",\"transBankName\":null,\"transType\":\"转账\",\"transChannel\":\"电子商务\",\"transDescription\":null,\"transAbstract\":\"合众易宝\"},{\"transDate\":\"2022-11-06\",\"transTime\":\"18:37:54\",\"inputAmount\":\"100.00\",\"outputAmount\":\"\",\"balanceAfTrans\":null,\"transAccount\":\"2433********0133\",\"transName\":\"财付通支付科技有限公司\",\"transBankName\":null,\"transType\":\"转账\",\"transChannel\":\"电子商务\",\"transDescription\":null,\"transAbstract\":\"代付\"},{\"transDate\":\"2022-11-06\",\"transTime\":\"18:37:27\",\"inputAmount\":\"108.00\",\"outputAmount\":\"\",\"balanceAfTrans\":null,\"transAccount\":\"6666********2817\",\"transName\":\"徐金\",\"transBankName\":null,\"transType\":\"转账\",\"transChannel\":\"超级网银\",\"transDescription\":null,\"transAbstract\":\"转存\"},{\"transDate\":\"2022-11-05\",\"transTime\":null,\"inputAmount\":\"\",\"outputAmount\":\"2.00\",\"balanceAfTrans\":null,\"transAccount\":\"2812********0307\",\"transName\":\"\",\"transBankName\":null,\"transType\":\"转账\",\"transChannel\":\"\",\"transDescription\":null,\"transAbstract\":\"短信费\"},{\"transDate\":\"2022-11-03\",\"transTime\":\"19:10:34\",\"inputAmount\":\"\",\"outputAmount\":\"9.90\",\"balanceAfTrans\":null,\"transAccount\":\"4604********3778\",\"transName\":\"财付通微信支付\",\"transBankName\":null,\"transType\":\"转账\",\"transChannel\":\"电子商务\",\"transDescription\":null,\"transAbstract\":\"财付通\"},{\"transDate\":\"2022-11-03\",\"transTime\":\"12:43:57\",\"inputAmount\":\"\",\"outputAmount\":\"13.00\",\"balanceAfTrans\":null,\"transAccount\":\"2433********0133\",\"transName\":\"\",\"transBankName\":null,\"transType\":\"转账\",\"transChannel\":\"电子商务\",\"transDescription\":null,\"transAbstract\":\"微信支付\"}]}}";
        transParams.setTxt(txt);
        transParams.setBankCode1("ABC");
        transParams.parseTxt();
        for (TransParams transParams1:transParams.getTransParams()){
            xTransService.upload(transParams1);
        }
    }

    @Test
    public void  testUploadTxtHBNX(){
        TransParams transParams = new TransParams();
        String txt = "{\"subChanlNo\":\"gh_b7612e01cd70\",\"MSG\":\"交易成功\",\"DYNAMIC_KEY\":\"SM4#496271143CF8D9C1A8C3311D1B1B48E4\",\"REQ_TIME\":\"20221110191816\",\"RETURN_NUM\":3,\"TOT_NUM\":3,\"END_DATE\":\"2022-11-10\",\"ACCT_NO\":\"6224121242800572\",\"PAY_ACCT_NO\":\"蹇常勇\",\"STATUS\":\"1\",\"NEXT_KEY\":\"Y\",\"BEGIN_DATE\":\"2022-11-10\",\"PAGE_SIZE\":\"10\",\"WX_CORE_ORGAN\":\"5200\",\"LIST\":[{\"trdeSnbr\":\"  4784163\",\"SUMR\":\"汇款\",\"dbtAcruamt\":8.5,\"PAYEE_NAME\":\"扫二维码付款\",\"cashRemFlg\":\"0\",\"crAcruamt\":0,\"PAYEE_ACCT_NO\":\"1000107101\",\"PAY_MODE\":\"5NB\",\"availBal\":1474.84,\"PAYEE_BANK_NAME\":\" 1000\",\"IO_FLAG\":\"D\",\"TRANS_DATE\":\"20221110091644\",\"ACCT_BALANCE\":1474.84,\"CCY\":\"CNY\",\"REM\":\"2022111082180953050493210300708\"},{\"trdeSnbr\":\"  2526989\",\"SUMR\":\"网银跨行\",\"dbtAcruamt\":4000,\"PAYEE_NAME\":\"曹凯\",\"cashRemFlg\":\"0\",\"crAcruamt\":0,\"PAYEE_ACCT_NO\":\"6215590200005491071\",\"PAY_MODE\":\"NS0\",\"availBal\":1483.34,\"PAYEE_BANK_NAME\":\" 1000\",\"IO_FLAG\":\"D\",\"TRANS_DATE\":\"20221110075842\",\"ACCT_BALANCE\":1483.34,\"CCY\":\"CNY\",\"REM\":\"汇兑\"},{\"trdeSnbr\":\"800863544\",\"SUMR\":\"汇款\",\"dbtAcruamt\":0,\"PAYEE_NAME\":\"财付通支付科技有限公司\",\"cashRemFlg\":\"0\",\"crAcruamt\":968.49,\"PAYEE_ACCT_NO\":\"243300133\",\"PAY_MODE\":\"5NB\",\"availBal\":5483.34,\"PAYEE_BANK_NAME\":\" 1000\",\"IO_FLAG\":\"C\",\"TRANS_DATE\":\"20221110222420\",\"ACCT_BALANCE\":5483.34,\"CCY\":\"CNY\",\"REM\":\"微信零钱提现\"}]}";
        transParams.setTxt(txt);
        transParams.setBankCode1("HBXH");
        transParams.parseTxt();
        for (TransParams transParams1:transParams.getTransParams()){
            try{
                xTransService.upload(transParams1);
            }catch (Exception e){

            }
        }
    }

    @Test
    public void  testUploadTxtBalance(){
        TransParams transParams = new TransParams();

        String txt = "{\"msg\":\"账户信息查询成功!\",\"result\":{\"debitCardList\":[{\"didf\":\"************4073\",\"didfType\":\"401\",\"cardClass\":\"1\",\"balance\":\"49556.40\"}],\"creditSwitch\":\"1\",\"phoneNo\":\"183****2934\",\"headimgUrl\":\"https://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTIycczN2Xqa51YnBXsTukXvreCK1DagWrzbU6Q4AGVBJCoEj8sjr0H8HSN1olDH3RwZDOUAj8m99g/132\"},\"status\":\"0000\"}";
        transParams.setTxt(txt);
        transParams.setBankCode1("ABC");
        transParams.parseTxt();
        for (int i=0; i < transParams.getCardInfos().size(); i++){
            String cardEndNo = transParams.getCardInfos().getJSONObject(i).getString("didf").replace("*", "").replace("\t", "");
            XCard xCard = xCardService.queryByCardEndNo(cardEndNo);
            if(xCard == null){
                continue;
            }
            xCard.setBalance(transParams.getCardInfos().getJSONObject(i).getBigDecimal("balance"));
            xCardService.update(xCard);
        }
    }
}
