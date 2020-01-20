package com.wuxp.payment;


/**
 * 支付配置提供者
 */
public interface PaymentConfigProvider {

    /**
     * 获取支付配置
     *
     * @param <T>
     * @param partnerId 商户标识
     * @return
     */
    <T extends PaymentConfig> T getPaymentConfig(String partnerId);
}
