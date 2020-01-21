package com.wuxp.payment;


import com.wuxp.payment.model.PlatformPaymentPartnerIdentity;

/**
 * 支付配置
 */
public interface PaymentConfiguration extends PlatformPaymentPartnerIdentity {

    /**
     * 获取App Id
     *
     * @return
     */
    String getAppId();

    /**
     * 获取App Secret
     *
     * @return
     */
    String getAppSecret();

    /**
     * 获取商户号
     *
     * @return
     */
    String getPartner();


    /**
     * 获取商户加密秘钥
     *
     * @return
     */
    String getPartnerSecret();

    /**
     * 支付方式是否启用
     *
     * @return
     */
    boolean isEnabled();
}
