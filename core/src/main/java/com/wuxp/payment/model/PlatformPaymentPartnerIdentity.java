package com.wuxp.payment.model;

import com.wuxp.payment.enums.PaymentMethod;

/**
 * 平台支付商户标识
 * @author wxup
 */
public interface PlatformPaymentPartnerIdentity extends PartnerIdentity, PlatformPaymentIdentity {


    /**
     * 获取支付方式
     *
     * @return
     */
    PaymentMethod getPaymentMethod();


}
