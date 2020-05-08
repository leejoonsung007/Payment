package com.shopping.payment.service.impl;

import com.lly835.bestpay.config.WxPayConfig;
import com.lly835.bestpay.enums.BestPayPlatformEnum;
import com.lly835.bestpay.enums.BestPayTypeEnum;
import com.lly835.bestpay.enums.OrderStatusEnum;
import com.lly835.bestpay.model.PayRequest;
import com.lly835.bestpay.model.PayResponse;
import com.lly835.bestpay.service.BestPayService;
import com.lly835.bestpay.service.impl.BestPayServiceImpl;
import com.shopping.payment.constant.PayPlatformEnum;
import com.shopping.payment.dao.PayInfoMapper;
import com.shopping.payment.pojo.PayInfo;
import com.shopping.payment.service.IPaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@Service
public class PaymentService implements IPaymentService {

    @Autowired
    private BestPayService bestPayService;

    @Autowired
    private PayInfoMapper payInfoMapper;

    @Override
    public PayResponse create(String orderId, BigDecimal amount, BestPayTypeEnum bestPayTypeEnum) {
        //write data to database
        PayInfo payinfo = new PayInfo(Long.parseLong(orderId),
                PayPlatformEnum.getByBestPayTypeEnum(bestPayTypeEnum).getCode(),
                OrderStatusEnum.NOTPAY.name(),
                amount);
        payInfoMapper.insertSelective(payinfo);

        PayRequest request = new PayRequest();
        request.setOrderName("8979198-最好的支付sdk");
        request.setOrderId(orderId);
        request.setOrderAmount(amount.doubleValue());
        request.setPayTypeEnum(bestPayTypeEnum);
        // TODO when use send the pay request again, close the last order first

        PayResponse response = bestPayService.pay(request);
        log.info("Pay request response={}", response);

        return response;
    }

    @Override
    public String asyncNotify(String notifyData) {
        // 1.check the signature
        PayResponse payResponse = bestPayService.asyncNotify(notifyData);
        log.info("Async notification response={}", payResponse);

        // 2.check the order amount (database)
        PayInfo payInfo = payInfoMapper.selectByOrderNumber(Long.parseLong(payResponse.getOrderId()));
        if (payInfo == null) {
            // TODO: send a email/message to developer
            throw new RuntimeException("cannot find the record in db");
        }
        if (!OrderStatusEnum.SUCCESS.name().equals(payInfo.getPlatformStatus())) {
            if (payInfo.getPayAmount().compareTo(BigDecimal.valueOf(payResponse.getOrderAmount())) != 0) {
                // TODO: send a email/message to developer
                throw new RuntimeException("the amount in async notify is not same with the amount in DB, orderNo=" + payResponse.getOrderId());
            }
            // 3.change the status of order
            payInfo.setPlatformStatus(OrderStatusEnum.SUCCESS.name());
            payInfo.setPlatformNumber(payResponse.getOutTradeNo());
            payInfoMapper.updateByPrimaryKeySelective(payInfo);
        }

        // TODO send rabbit mq message from payment to mall

        if (payResponse.getPayPlatformEnum() == BestPayPlatformEnum.WX) {
            // 4.tell wechat not to notify again
            return "<xml>\n" +
                    "  <return_code><![CDATA[SUCCESS]]></return_code>\n" +
                    "  <return_msg><![CDATA[OK]]></return_msg>\n" +
                    "</xml>";
        } else if (payResponse.getPayPlatformEnum() == BestPayPlatformEnum.ALIPAY) {
            return "success";
        }
        throw new RuntimeException("async notification is still sending");
    }

    @Override
    public PayInfo queryByOrderId(String orderId) {
        return payInfoMapper.selectByOrderNumber(Long.parseLong(orderId));
    }
}
