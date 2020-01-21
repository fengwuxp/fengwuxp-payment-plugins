package com.wuxp.payment;


import com.wuxp.payment.model.PlatformPaymentPartnerIdentity;

/**
 * 支付配置提供者
 */
public interface PaymentConfigurationProvider {

    /**
     * 获取支付配置
     *
     * @param <T>
     * @param identity
     * @return
     */
    <T extends PaymentConfiguration> T getPaymentConfig(PlatformPaymentPartnerIdentity identity);
}
