package com.wuxp.payment.model;

import com.wuxp.payment.enums.PaymentMethod;
import com.wuxp.payment.enums.PaymentPlatform;

/**
 * 平台支付商户标识
 */
public interface PlatformPaymentPartnerIdentity extends PartnerIdentity {


    /**
     * 获取支付平台
     *
     * @return
     */
    PaymentPlatform getPaymentPlatform();

    /**
     * 获取支付方式
     *
     * @return
     */
    PaymentMethod getPaymentMethod();


}
