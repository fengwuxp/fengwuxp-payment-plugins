package com.wuxp.payment;

import com.wuxp.payment.enums.PaymentPlatform;
import lombok.extern.slf4j.Slf4j;

/**
 * 抽象的支付服务
 */
@Slf4j
public abstract class AbstractPlatformPaymentService implements PlatformPaymentService {


    protected PaymentCallbackTemplate callbackTemplate;

    protected PaymentPlatform paymentPlatform;

    @Override
    public PaymentPlatform getPaymentPlatform() {
        return this.paymentPlatform;
    }

    public void setCallbackTemplate(PaymentCallbackTemplate callbackTemplate) {
        this.callbackTemplate = callbackTemplate;
    }
}
