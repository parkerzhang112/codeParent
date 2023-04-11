package com.code.autoapi;


import com.code.baseservice.dto.autoapi.TransParams;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TransControllerTest extends  AutoapiApplicationTests{


    @Test
    public void testAliPay(){
        String content = "你好, <退出|我的支付宝|安全中心|服务大厅 <U\\n 我的支付宝/ 交易记录◆◆ 会员保障 应用中心\\n交易记录\\n充值记录\\n提现记录\\n电子对账单\\n交易记录切换到标准版可用余额 0.00 元 余额收支明细 | 回收站\\n交易时间：最近一周交易状态：所有状态\\n关键词：流水号 \\n时间类型：创建时间\\n金额范围：\\n - \\n资金流向：全部\\n交易方式：担保交易 即时到账 货到付款交易分类：全部 \\n统计金额下载账单: Excel格式 Txt格式\\n所有状态进行中等待付款等待发货等待确认收货退款成功失败维权\\n创建时间\\t\\t名称\\t商家订单号|交易号\\t对方\\t金额\\t|明细\\t状态\\t操作\\2023.03.18  \\n\\n04:27  \\n\\n 余额宝-2023.03.17-收益发放\\n\\n流水号:20230318310983467791\\n\\n兴全基金管理有限公司\\n\\n0.02\\t\\t\\n交易成功\\n\\n备注 \\n\n" +
                "2023.03.17\\n\\n04:16\\n\\n余额宝-2023.03.16-收益发放\\n\\n流水号:20230317304955436791\\n\\n兴全基金管理有限公司\\n\\n0.02\\t\\t\\n交易成功\\n\\n备注 \\n\n" +
                "2023.03.16\\n\\n04:06\\n\\n余额宝-2023.03.15-收益发放\\n\\n流水号:20230316398385082791\\n\\n兴全基金管理有限公司\\n\\n0.01\\t\\t\\n交易成功\\n\\n备注 \\n\n" +
                "2023.03.15\\n\\n04:23\\n\\n余额宝-2023.03.14-收益发放\\n\\n流水号:20230315391685175791\\n\\n兴全基金管理有限公司\\n\\n0.01\\t\\t\\n交易成功\\n\\n备注 \\n\n" +
                "2023.03.14\\n\\n05:36\\n\\n余额宝-2023.03.13-收益发放\\n\\n流水号:20230314386936057791\\n\\n兴全基金管理有限公司\\n\\n0.02\\t\\t\\n交易成功\\n\\n备注 \\n\n" +
                "2023.03.13\\n\\n03:29\\n\\n余额宝-2023.03.12-收益发放\\n\\n流水号:20230313379425274791\\n\\n兴全基金管理有限公司\\n\\n0.01\\t\\t\\n交易成功\\n\\n备注 \\n\n" +
                "2023.03.12\\n\\n03:54\\n\\n余额宝-2023.03.11-收益发放\\n\\n流水号:20230312373352599791\\n\\n兴全基金管理有限公司\\n\\n0.01\\t\\t\\n交易成功\\n\\n备注 \\n图释: 支付明细 备注 服务费 \n" +
                "余额宝分期\\n回到顶部\\n使用遇到问题？\\n扣款成功，但还是显示“等待买家付款”？\\n答：银行账户已扣款，但交易状态仍为【等待买家付款】，请您不要担心，耐心等待10秒钟之后，按键盘上F5键刷新页面，重新刷新后交易状态会变为【买家已付款】。\\n使用红包、集分宝、商城积分抵扣，为什么交易总额不变？\\n答：红包、集分宝、商城积分等，仅是帮助您抵扣掉部分交易金额，您实际银行卡或者余额扣款的金额，具体请点击进入支付宝收支明细查看。\\n更多相关帮助\\n\\n \\n诚征英才 | 联系我们 | International Business\\nICP证：合字B2-20190046\\nconsumeprod-40-8780 ";
//        String content = "您的借记卡账户长城电子借记卡，于09月17日收入(网银跨行)人民币100.00元,交易后余额1250.10【中国银行】";
        TransParams transParams = new TransParams();
        transParams.setBankCode("alipay");
        transParams.setTxt(content);
        transParams.parseTxt();

    }

}
