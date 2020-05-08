package com.shopping.payment.service.impl;

import com.lly835.bestpay.enums.BestPayTypeEnum;
import com.shopping.payment.PaymentApplicationTests;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

public class PaymentServiceTest extends PaymentApplicationTests {

    @Autowired
    private PaymentService payService;

    @Test
    public void create() {
        payService.create("12345231231267", BigDecimal.valueOf(0.01), BestPayTypeEnum.WXPAY_NATIVE);
    }
}