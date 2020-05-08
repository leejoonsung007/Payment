package com.shopping.payment.controller;

import com.lly835.bestpay.config.WxPayConfig;
import com.lly835.bestpay.enums.BestPayTypeEnum;
import com.lly835.bestpay.model.PayResponse;
import com.shopping.payment.config.BestPayConfig;
import com.shopping.payment.pojo.PayInfo;
import com.shopping.payment.service.impl.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/payment")
@Slf4j
public class PaymentController {

    @Autowired
    private PaymentService payService;

    @Autowired
    private WxPayConfig wxPayConfig;

    @GetMapping("/create")
    public ModelAndView create(@RequestParam("orderId") String orderId,
                               @RequestParam("amount") BigDecimal amount,
                               @RequestParam("payType") BestPayTypeEnum payType) {
        PayResponse response = payService.create(orderId, amount, payType);
        Map<String, String> map = new HashMap<>();

        //wechat native pay
        if (payType == BestPayTypeEnum.WXPAY_NATIVE){
            map.put("codeUrl", response.getCodeUrl());
            map.put("orderId", orderId);
            map.put("redirectUrl", wxPayConfig.getReturnUrl());
            return new ModelAndView("createForWechatNative", map);
        } else if (payType == BestPayTypeEnum.ALIPAY_PC) {
            map.put("body", response.getBody());
            return new ModelAndView("createForAlipayPC", map);
        }
        throw new RuntimeException("payment type are not support");
    }

    @PostMapping("/notify")
    @ResponseBody
    public void asyncNotify(@RequestBody String notifyData) {
        payService.asyncNotify(notifyData);
    }

    @GetMapping("queryByOrderId")
    @ResponseBody
    public PayInfo queryByOrderId(@RequestParam String orderId) {
        log.info("check the payment record");
        return payService.queryByOrderId(orderId);
    }
}
