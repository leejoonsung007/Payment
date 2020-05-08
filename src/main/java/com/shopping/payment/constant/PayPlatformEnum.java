package com.shopping.payment.constant;

import com.lly835.bestpay.enums.BestPayTypeEnum;

public enum PayPlatformEnum {

    ALIPAY(1),

    WX(2);

    Integer code;

    PayPlatformEnum(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public static PayPlatformEnum getByBestPayTypeEnum(BestPayTypeEnum bestPayTypeEnum) {
        for (PayPlatformEnum payPlatformEnum : PayPlatformEnum.values()) {
            if (payPlatformEnum.name().equals(bestPayTypeEnum.getPlatform().name())) {
                return payPlatformEnum;
            }
        }
        throw new RuntimeException("cannot find this platform" + bestPayTypeEnum.name());
    }
}
