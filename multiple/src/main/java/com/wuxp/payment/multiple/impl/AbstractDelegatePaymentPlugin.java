package com.wuxp.payment.multiple.impl;

import com.wuxp.payment.PaymentPlugin;
import com.wuxp.payment.enums.PaymentMethod;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;

import java.io.InputStream;

/**
 * 抽象的代理插件实现
 */
@Slf4j
@Setter
public class AbstractDelegatePaymentPlugin implements PaymentPlugin, BeanFactoryAware, InitializingBean {

    protected BeanFactory beanFactory;

    protected PlatformPaymentServiceProvider paymentPluginProvider;

    protected PaymentMethod paymentMethod;

    @Override
    public void afterPropertiesSet() throws Exception {

        if (this.paymentPluginProvider == null) {
            this.paymentPluginProvider = this.beanFactory.getBean(PlatformPaymentServiceProvider.class);
        }
    }


    @Override
    public <T> PaymentResult<T> payment(DoPaymentReq req, String authCode) {
        return null;
    }

    @Override
    public <T> PrePaymentResult<T> prePayment(DoPaymentReq req) {
        return null;
    }

    @Override
    public PaymentResult queryPaymentStatus(DoQueryPaymentReq req) {
        return null;
    }

    @Override
    public RefundResult doRefund(DoRefundReq req) {
        return null;
    }

    @Override
    public RefundResult queryRefundStatus(DoQueryRefundReq req) {
        return null;
    }

    @Override
    public PaymentResult verifyNotify(DoVerifyNotifyReq req, InputStream inputStream) throws Exception {
        return null;
    }

    @Override
    public RefundResult verifyRefundNotify(DoVerifyRefundNotifyReq req, InputStream inputStream) throws Exception {
        return null;
    }

    @Override
    public String getNotifyReturn(boolean success) {
        return null;
    }

    @Override
    public String getRefundNotifyReturn(boolean success) {
        return null;
    }
}
