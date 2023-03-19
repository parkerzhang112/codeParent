package com.code.autoapi;

import com.xiagao.baseservice.service.SmsService;
import com.xiagao.baseservice.service.XRechargeService;
import com.xiagao.baseservice.service.XTransferService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;

public class AutoControllerTest extends  AutoapiApplicationTests{



    @Autowired
    private SmsService smsService;

    @Autowired
    private XTransferService xTransferService;

    @Autowired
    private XRechargeService xRechargeService;

    @Test
    public void testJob(){
        xTransferService.autoCatchOrder();
    }



    @Test
    public void testCard() throws ParseException {
        xTransferService.testcard();
    }

    @Test
    public void testMatchTrans() throws ParseException {
        xTransferService.macthTransfer();
    }


    @Test
    public void reviewcard() throws ParseException {
        xTransferService.reviewcard();
    }


    @Test
    public void testAutoCancelRechargeOrder() throws ParseException {
        xRechargeService.autoCancelRechargeOrder();
    }
}
