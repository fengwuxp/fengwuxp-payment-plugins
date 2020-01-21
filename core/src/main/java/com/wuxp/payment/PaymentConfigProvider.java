package com.wuxp.payment;


import com.wuxp.payment.model.PartnerIdentity;
import com.wuxp.payment.model.PlatformPaymentIdentity;

/**
 * 支付配置提供者
 */
public interface PaymentConfigProvider extends PlatformPaymentIdentity {

    /**
     * 获取支付配置
     *
     * @param <T>
     * @param partnerId 商户标识
     * @return
     */
    <T extends PaymentConfig> T getPaymentConfig(PartnerIdentity partnerId);
}
