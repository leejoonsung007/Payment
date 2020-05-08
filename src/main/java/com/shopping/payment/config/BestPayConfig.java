package com.shopping.payment.config;

import com.lly835.bestpay.config.AliPayConfig;
import com.lly835.bestpay.config.WxPayConfig;
import com.lly835.bestpay.service.BestPayService;
import com.lly835.bestpay.service.impl.BestPayServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class BestPayConfig {

    @Autowired
    private WxPayCountConfig wxPayCountConfig;

    @Autowired
    private AliPayAccountConfig aliPayAccountConfig;

    @Bean
    public BestPayService bestPayService(WxPayConfig wxPayConfig, AliPayConfig aliPayConfig) {
        BestPayServiceImpl bestPayService = new BestPayServiceImpl();
        bestPayService.setWxPayConfig(wxPayConfig);
        bestPayService.setAliPayConfig(aliPayConfig);
        return bestPayService;
    }

    @Bean
    public WxPayConfig wxPayConfig() {
        WxPayConfig wxPayConfig = new WxPayConfig();
        wxPayConfig.setAppId(wxPayCountConfig.getAppId());
        wxPayConfig.setMchId(wxPayCountConfig.getMchId());
        wxPayConfig.setMchKey(wxPayCountConfig.getMchKey());
        wxPayConfig.setNotifyUrl(wxPayCountConfig.getNotifyUrl());
        wxPayConfig.setReturnUrl(wxPayCountConfig.getReturnUrl());
        return wxPayConfig;
    }

    @Bean
    public AliPayConfig aliPayConfig() {
        AliPayConfig aliPayConfig = new AliPayConfig();
        aliPayConfig.setAppId(aliPayAccountConfig.getAppId());
        aliPayConfig.setPrivateKey(aliPayAccountConfig.getPrivateKey());
        aliPayConfig.setAliPayPublicKey(aliPayAccountConfig.getAliPayPublicKey());
        aliPayConfig.setNotifyUrl(aliPayAccountConfig.getNotifyUrl());
        aliPayConfig.setReturnUrl(aliPayAccountConfig.getReturnUrl());
        return aliPayConfig;
    }
}
