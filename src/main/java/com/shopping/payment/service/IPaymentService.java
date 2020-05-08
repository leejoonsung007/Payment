package com.shopping.payment.service;

import com.lly835.bestpay.enums.BestPayTypeEnum;
import com.lly835.bestpay.model.PayResponse;
import com.shopping.payment.pojo.PayInfo;

import java.math.BigDecimal;

public interface IPaymentService {

    // create the payment
    PayResponse create(String orderId, BigDecimal amount, BestPayTypeEnum bestPayTypeEnum);

    String asyncNotify(String notifyData);

    PayInfo queryByOrderId(String orderId);
}
