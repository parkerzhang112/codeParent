package com.code.autoapi;


import com.code.baseservice.dto.autoapi.TransParams;
import com.code.baseservice.service.ZfTransRecordService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TransControllerTest extends  AutoapiApplicationTests{

    @Autowired
    ZfTransRecordService zfTransRecordService;

    @Test
    public void testAliPay(){
//        String content = "你好, <退出|我的支付宝|安全中心|服务大厅 <U\\n 我的支付宝/ 交易记录◆◆ 会员保障 应用中心\\n交易记录\\n充值记录\\n提现记录\\n电子对账单\\n交易记录切换到标准版可用余额 0.00 元 余额收支明细 | 回收站\\n交易时间：最近一周交易状态：所有状态\\n关键词：流水号 \\n时间类型：创建时间\\n金额范围：\\n - \\n资金流向：全部\\n交易方式：担保交易 即时到账 货到付款交易分类：全部 \\n统计金额下载账单: Excel格式 Txt格式\\n所有状态进行中等待付款等待发货等待确认收货退款成功失败维权\\n创建时间\\t\\t名称\\t商家订单号|交易号\\t对方\\t金额\\t|明细\\t状态\\t操作\\2023.03.18  \\n\\n04:27  \\n\\n 余额宝-2023.03.17-收益发放\\n\\n流水号:20230318310983467791\\n\\n兴全基金管理有限公司\\n\\n0.02\\t\\t\\n交易成功\\n\\n备注 \\n\n" +
//                "2023.03.17\\n\\n04:16\\n\\n余额宝-2023.03.16-收益发放\\n\\n流水号:20230317304955436791\\n\\n兴全基金管理有限公司\\n\\n0.02\\t\\t\\n交易成功\\n\\n备注 \\n\n" +
//                "2023.03.16\\n\\n04:06\\n\\n余额宝-2023.03.15-收益发放\\n\\n流水号:20230316398385082791\\n\\n兴全基金管理有限公司\\n\\n0.01\\t\\t\\n交易成功\\n\\n备注 \\n\n" +
//                "2023.03.15\\n\\n04:23\\n\\n余额宝-2023.03.14-收益发放\\n\\n流水号:20230315391685175791\\n\\n兴全基金管理有限公司\\n\\n0.01\\t\\t\\n交易成功\\n\\n备注 \\n\n" +
//                "2023.03.14\\n\\n05:36\\n\\n余额宝-2023.03.13-收益发放\\n\\n流水号:20230314386936057791\\n\\n兴全基金管理有限公司\\n\\n0.02\\t\\t\\n交易成功\\n\\n备注 \\n\n" +
//                "2023.03.13\\n\\n03:29\\n\\n余额宝-2023.03.12-收益发放\\n\\n流水号:20230313379425274791\\n\\n兴全基金管理有限公司\\n\\n0.01\\t\\t\\n交易成功\\n\\n备注 \\n\n" +
//                "2023.03.12\\n\\n03:54\\n\\n余额宝-2023.03.11-收益发放\\n\\n流水号:20230312373352599791\\n\\n兴全基金管理有限公司\\n\\n0.01\\t\\t\\n交易成功\\n\\n备注 \\n图释: 支付明细 备注 服务费 \n" +
//                "余额宝分期\\n回到顶部\\n使用遇到问题？\\n扣款成功，但还是显示“等待买家付款”？\\n答：银行账户已扣款，但交易状态仍为【等待买家付款】，请您不要担心，耐心等待10秒钟之后，按键盘上F5键刷新页面，重新刷新后交易状态会变为【买家已付款】。\\n使用红包、集分宝、商城积分抵扣，为什么交易总额不变？\\n答：红包、集分宝、商城积分等，仅是帮助您抵扣掉部分交易金额，您实际银行卡或者余额扣款的金额，具体请点击进入支付宝收支明细查看。\\n更多相关帮助\\n\\n \\n诚征英才 | 联系我们 | International Business\\nICP证：合字B2-20190046\\nconsumeprod-40-8780 ";
//        String content = "您的借记卡账户长城电子借记卡，于09月17日收入(网银跨行)人民币100.00元,交易后余额1250.10【中国银行】";
        String content = " 我的支付宝/ 交易记录◆◆ 会员保障 应用中心\n" +
                "交易记录\n" +
                "充值记录\n" +
                "提现记录\n" +
                "电子对账单\n" +
                "交易记录切换到标准版可用余额 303.41 元 余额收支明细 | 回收站\n" +
                "交易时间：最近一周交易状态：所有状态\n" +
                "关键词：流水号 \n" +
                "时间类型：创建时间\n" +
                "金额范围：\n" +
                " - \n" +
                "资金流向：全部\n" +
                "交易方式：担保交易 即时到账 货到付款交易分类：全部 \n" +
                "统计金额下载账单: Excel格式 Txt格式\n" +
                "所有状态进行中等待付款等待发货等待确认收货退款成功失败维权\n" +
                "创建时间                名称    商家订单号|交易号       对方    金额    |明细   态      操作\n" +
                "2023.04.19\n" +
                "\n" +
                "23:03\n" +
                "\n" +
                "收款\n" +
                "\n" +
                "流水号:20230419200040011100480011080924\n" +
                "\n" +
                "吕映宏\n" +
                "\n" +
                "+ 21.00\n" +
                "交易成功\n" +
                "\n" +
                "详情 \n" +
                "2023.04.19\n" +
                "\n" +
                "22:50\n" +
                "\n" +
                "收款\n" +
                "\n" +
                "流水号:20230419200040011100480011091690\n" +
                "\n" +
                "吕映宏\n" +
                "\n" +
                "+ 35.00\n" +
                "交易成功\n" +
                "\n" +
                "详情 \n" +
                "2023.04.19\n" +
                "\n" +
                "22:32\n" +
                "\n" +
                "收款\n" +
                "\n" +
                "流水号:20230419200040011100630011582403\n" +
                "\n" +
                "习兵\n" +
                "\n" +
                "+ 10.00\n" +
                "交易成功\n" +
                "\n" +
                "详情 \n" +
                "2023.04.19\n" +
                "\n" +
                "22:26\n" +
                "\n" +
                "收款\n" +
                "\n" +
                "流水号:20230419200040011100630011643805\n" +
                "\n" +
                "习兵\n" +
                "\n" +
                "+ 10.00\n" +
                "交易成功\n" +
                "\n" +
                "详情 \n" +
                "2023.04.16\n" +
                "\n" +
                "23:46\n" +
                "\n" +
                "收款\n" +
                "\n" +
                "流水号:20230416200040011100040007737182\n" +
                "\n" +
                "方超\n" +
                "\n" +
                "+ 123.00\n" +
                "交易成功\n" +
                "\n" +
                "详情 \n" +
                "2023.04.16\n" +
                "\n" +
                "23:45\n" +
                "\n" +
                "收款\n" +
                "\n" +
                "流水号:20230416200040011100040007787479\n" +
                "\n" +
                "方超\n" +
                "\n" +
                "+ 100.00\n" +
                "交易成功\n" +
                "\n" +
                "详情 \n" +
                "图释: 支付明细 备注 服务费 余额宝分期\n" +
                "回到顶部\n" +
                "使用遇到问题？\n" +
                "扣款成功，但还是显示“等待买家付款”？\n" +
                "答：银行账户已扣款，但交易状态仍为【等待买家付款】，请您不要担心，耐心等待10秒钟之后，按键盘上F5键刷新页面，重新刷新后交易状态会变为【买家已付款】。\n" +
                "使用红包、集分宝、商城积分抵扣，为什么交易总额不变？\n" +
                "答：红包、集分宝、商城积分等，仅是帮助您抵扣掉部分交易金额，您实际银行卡或者余额扣款的金额，具体请点击进入支付宝收支明细查看。\n" +
                "更多相关帮助\n" +
                "\n" +
                " \n" +
                "诚征英才 | 联系我们 | International Business";
        TransParams transParams = new TransParams();
        transParams.setBankCode("alipay");
        transParams.setTxt(content);
        transParams.setAccount("13123123");
        transParams.parseTxt();
        zfTransRecordService.upload(transParams);
    }

    @Test
    public void testAliPayCompany(){
        String content = "小程序\\n\\nmessage3\\nlogo\\n支付宝\\n商家平台\\n首页\\n资金管理\\n对账中心\\n产品中心\\n运营中心\\n数据中心\\n账号中心\\n对账总览\\n实时账单(业务查询)\\n交易订单\\n资金流水(账务明细)\\n资金操作记录(资金业务)\\n账单管理\\n账单下载\\n费用账单\\n发票管理\\n给我的发票\\n我开的发票\\n发票资料及收票地址\\n财务凭证\\n凭证申请\\n资金账户\\n支付宝账户\\n账务明细\\n账户\\n收支类型\\n\\n全部\\n\\n收入\\n\\n支出\\n查询时间\\n2023-04-20 00:00:00\\n至\\n2023-04-20 23:59:59\\n今日昨日7日30日\\n收入（元）\\n+200.001笔\\n支出（元）\\n0.000笔\\n数据明细\\n已选\\n0\\n项\\n\\n入账时间\\n支付宝交易号\\t商户订单号\\t对方信息\\t账务类型\\t收支金额（元）\\t账户余额（元）\\t付款备注\\t备注\\t操作\\n\\n2023-04-20 15:22:41\\t20230420...1957\\t46819753...7764\\t\\n150******06\\n**中\\n在线支付\\t200.00\\t200.00\\t\\t\\t\\n共有 1 条\\n经营许可证编号：合字B2-20190046 网站备案：沪ICP备 15027489号";
        TransParams transParams = new TransParams();
        transParams.setBankCode("AlipayComany");
        transParams.setTxt(content);
        transParams.setAccount("13123123");
        transParams.parseTxt();
        zfTransRecordService.upload(transParams);
    }

    @Test
    public void testAliPayApp(){
        String content = "收款-诗泽|+100.00|转账红包|04-21|18:48; 收款-弥彦y|+100.00|转账红包|04-21|16:05; 收款-Paul|+100.00|转账红包|04-21|15:59; 收款-志轩|+100.00|转账红包|04-21|15:53; 余额宝-收益发放|0.01|投资理财|04-20|03:38; ";
//        String content = "您的借记卡账户长城电子借记卡，于09月17日收入(网银跨行)人民币100.00元,交易后余额1250.10【中国银行】";
        TransParams transParams = new TransParams();
        transParams.setBankCode("alipayapp");
        transParams.setTxt(content);
        transParams.setAccount("13123123");
        transParams.parseTxt();
        zfTransRecordService.upload(transParams);
    }

    @Test
    public void testAliPayAppC(){
        String content = "收钱码收款-来自**中|+100.00|收入|昨天|23:09;";
//        String content = "您的借记卡账户长城电子借记卡，于09月17日收入(网银跨行)人民币100.00元,交易后余额1250.10【中国银行】";
        TransParams transParams = new TransParams();
        transParams.setBankCode("alipayapp");
        transParams.setTxt(content);
        transParams.setAccount("13123123");
        transParams.parseTxt();
        zfTransRecordService.upload(transParams);
    }

}
