package com.code.backapi;

import com.xiagao.baseservice.dto.backapi.SpilitParams;
import com.xiagao.baseservice.dto.frontapi.OperaBalanceParams;
import com.xiagao.baseservice.dto.frontapi.OperaOrderParams;
import com.xiagao.baseservice.entity.XCard;
import com.xiagao.baseservice.entity.XRecharge;
import com.xiagao.baseservice.entity.XTransfer;
import com.xiagao.baseservice.service.XCardService;
import com.xiagao.baseservice.service.XMerchantService;
import com.xiagao.baseservice.service.XRechargeService;
import com.xiagao.baseservice.service.XTransferService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

public class OrderControllerTest extends BackapiApplicationTest {

    @Autowired
    XTransferService xTransferService;

    @Autowired
    private XRechargeService xRechargeService;

    @Autowired
    private XCardService xCardService;

    @Autowired
    private XMerchantService xMerchantService;

    @Test
    public void testConfrim() {
        OperaOrderParams operaOrderParams = new OperaOrderParams();
        operaOrderParams.setOrderType(2);
        if (new Integer(1).equals(operaOrderParams.getOrderType())) {
            XTransfer xTransfer = xTransferService.queryById("ADCW1619700624485M9LVaYJ5LmlRIi3");
//            List<XTransfer> xTransfers = xTransferService.queryAllByLimit(0, 1);
            List<XCard> cards = xCardService.queryAllByLimit(0, 1);
//            if (xTransfers.size() > 0) {
                operaOrderParams.setOrderNo(xTransfer.getOrderNo());
                operaOrderParams.setOrderType(1);
                operaOrderParams.setCloseReason("支付成功");
                operaOrderParams.setPaid_amt(xTransfer.getPayAmount());
                operaOrderParams.setCardId(cards.get(0).getCardId());
                xTransferService.confirmOrder(operaOrderParams);
//            }
        } else {
            XRecharge xRecharges = xRechargeService.queryById("DADC1654338133759jLw2gsffNPmYX3T");
            operaOrderParams.setOrderNo(xRecharges.getOrderNo());
            operaOrderParams.setOrderType(1);
            operaOrderParams.setCloseReason("支付成功");
            operaOrderParams.setPaid_amt(xRecharges.getPayAmount());
            operaOrderParams.setCardId(xRecharges.getCardId());
            xRechargeService.confirmOrder(operaOrderParams);

        }
    }

    @Test
    public void testCancel() {
        OperaOrderParams operaOrderParams = new OperaOrderParams();
        operaOrderParams.setOrderType(0);
        if (new Integer(1).equals(operaOrderParams.getOrderType())) {
            List<XTransfer> xTransfers = xTransferService.queryAllByLimit(0, 1);
            List<XCard> cards = xCardService.queryAllByLimit(0, 1);
            if (xTransfers.size() > 0) {
                operaOrderParams.setOrderNo(xTransfers.get(0).getOrderNo());
                operaOrderParams.setOrderType(1);
                operaOrderParams.setCloseReason("支付失败");
                operaOrderParams.setPaid_amt(xTransfers.get(0).getPayAmount());
                operaOrderParams.setCardId(cards.get(0).getCardId());
                xTransferService.cancelOrder(operaOrderParams);
            }
        } else {
            XRecharge xRecharges = xRechargeService.queryById("DADC16543380123967b66SCnbQBZDKVc");
            operaOrderParams.setOrderNo(xRecharges.getOrderNo());
            operaOrderParams.setOrderType(1);
            operaOrderParams.setCloseReason("支付失败");
            operaOrderParams.setPaid_amt(xRecharges.getPayAmount());
            operaOrderParams.setCardId(xRecharges.getCardId());
            xRechargeService.cancelOrder(operaOrderParams);
        }
    }

    @Test
    public void testNotify() {
        OperaOrderParams operaOrderParams = new OperaOrderParams();
        if (new Integer(1).equals(operaOrderParams.getOrderType())) {
//            XTransfer xTransfer = xTransferService.queryById(operaOrderParams.getOrderNo());
            List<XTransfer> xTransfers = xTransferService.queryAllByLimit(0, 1);
            if (xTransfers.size() > 0) {
                xTransferService.notify(xTransfers.get(0));
            }
        } else {
            List<XRecharge> xRecharges = xRechargeService.queryAllByLimit(0, 1);
            if (xRecharges.size() > 0) {
                xRechargeService.notify(xRecharges.get(0));
            }
        }
    }

    @Test
    public void testAddBalance(){
        OperaBalanceParams operaBalanceParams = new OperaBalanceParams();
        operaBalanceParams.setRemark("上分");
        operaBalanceParams.setAmount(new BigDecimal("1000"));
        operaBalanceParams.setTransType(1);
        operaBalanceParams.setMerchantId(3);
        xMerchantService.operatBalance(operaBalanceParams);
    }

    @Test
    public void testRollbackTrans(){
        OperaBalanceParams operaBalanceParams = new OperaBalanceParams();
        operaBalanceParams.setRemark("回滚入款");
        operaBalanceParams.setOrderNo("DADC1619431892002f7Y70fhZjizkiDg");
        operaBalanceParams.setAmount(new BigDecimal("1000"));
        operaBalanceParams.setTransType(1);
        operaBalanceParams.setMerchantId(3);
        xRechargeService.rollbackOrder(operaBalanceParams);
    }

    @Test
    public void testRollbackRecharge(){
        OperaBalanceParams operaBalanceParams = new OperaBalanceParams();
        operaBalanceParams.setRemark("冲正出款");
        operaBalanceParams.setOrderNo("ADCW1619350516013C0Dea2e5ks0QfKf");
        operaBalanceParams.setAmount(new BigDecimal("1000"));
        operaBalanceParams.setMerchantId(3);
        operaBalanceParams.setTransType(1);
        xTransferService.rollbackOrder(operaBalanceParams);
    }

    @Test
    public void tetstSplit(){
        SpilitParams spilitParams = new SpilitParams();
        spilitParams.setOrder_no("rg2W16657449190529Nd1Y5nt4ysty5H");
        spilitParams.setNum(2);
        xTransferService.spilit(spilitParams);
    }
}
