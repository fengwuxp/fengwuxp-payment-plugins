package com.wuxp.payment;

/**
 * 支付配置
 */
public interface PaymentConfig {

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
}
