package com.wuxp.payment.model;

import com.wuxp.payment.enums.PaymentPlatform;

/**
 * 平台标识
 */
public interface PlatformPaymentIdentity {

    /**
     * 获取支付的平台类型
     *
     * @return
     */
    PaymentPlatform getPaymentPlatform();
}
