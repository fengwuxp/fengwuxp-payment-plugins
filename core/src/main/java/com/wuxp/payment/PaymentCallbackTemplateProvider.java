package com.wuxp.payment;


import com.wuxp.payment.model.PlatformPaymentPartnerIdentity;

/**
 * 支付回调提供者
 */
public interface PaymentCallbackTemplateProvider {

    /**
     * 获取支付回调
     *
     * @param <T>
     * @param identity
     * @return
     */
    <T extends PaymentCallbackTemplate> T getPaymentCallbackTemplate(PlatformPaymentPartnerIdentity identity);
}
