package com.wuxp.payment.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 支付方式
 */
@AllArgsConstructor
@Getter
public enum PaymentMethod {

    /**
     * app
     */
    APP("App支付"),

    /**
     * pc
     */
    PC_BROWSER("pc网页支付"),

    /**
     * h5
     */
    H5("h5支付"),

    /**
     * 扫（收款）码
     */
    SCAN_QR_CODE("扫码支付"),

    /**
     * js api (公众号)
     */
    JS_API("公众号支付"),


    /**
     * js api (小程序)
     */
    MIN_APPLETS("小程序支付"),

    /**
     * 付款码（或刷脸）
     */
    AUTH_CODE("付款码支付");

    private String desc;

}
