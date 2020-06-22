package com.wuxp.payment;

import com.wuxp.payment.model.PlatformPaymentPartnerIdentity;

/**
 * 平台服务提供者
 * @author wxup
 */
public interface PlatformPaymentServiceProvider {


    /**
     * 获取平台服务
     *
     * @param partnerIdentity
     * @return
     */
    PlatformPaymentService getPlatformPaymentService(PlatformPaymentPartnerIdentity partnerIdentity);


    /**
     * 移除一个平台服务
     *
     * @param partnerIdentity
     */
    void removePlatformPaymentService(PlatformPaymentPartnerIdentity partnerIdentity);

}
