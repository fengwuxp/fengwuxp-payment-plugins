package com.wuxp.payment;

import com.wuxp.payment.enums.PaymentPlatform;

/**
 * 平台支付服务，不同的支付平台可以实现对应的接口
 */
public interface PlatformPaymentService extends PaymentPlugin, PaymentNotifyProcessor {

    /**
     * 获取支付的平台类型
     *
     * @return
     */
    PaymentPlatform getPaymentPlatform();

}
