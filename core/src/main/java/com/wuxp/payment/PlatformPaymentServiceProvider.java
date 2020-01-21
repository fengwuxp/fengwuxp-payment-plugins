package com.wuxp.payment;

import com.wuxp.payment.model.PlatformPaymentPartnerIdentity;

/**
 * 平台服务提供者
 */
public interface PlatformPaymentServiceProvider {


    /**
     * 获取平台服务
     *
     * @param partnerIdentity
     * @return
     */
    PlatformPaymentService getPlatformPaymentService(PlatformPaymentPartnerIdentity partnerIdentity);


}
