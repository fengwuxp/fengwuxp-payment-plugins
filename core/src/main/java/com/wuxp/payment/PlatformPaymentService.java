package com.wuxp.payment;

import com.wuxp.payment.model.PlatformPaymentIdentity;

/**
 * 平台支付服务，不同的支付平台可以实现对应的接口
 */
public interface PlatformPaymentService extends PlatformPaymentIdentity, PaymentPlugin, PaymentNotifyProcessor {


}
