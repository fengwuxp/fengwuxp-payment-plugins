package com.wuxp.payment;

import com.wuxp.payment.enums.PaymentMethod;
import com.wuxp.payment.enums.PaymentPlatform;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;

/**
 * 抽象的支付服务
 *
 * @author wxup
 */
@Slf4j
public abstract class AbstractPlatformPaymentService<T extends PaymentConfiguration> implements PlatformPaymentService, BeanFactoryAware, InitializingBean {

    /**
     * 支付回调处理
     */
    protected PaymentCallbackTemplate callbackTemplate;

    protected PaymentPlatform paymentPlatform;

    protected PaymentMethod paymentMethod;

    /**
     * 支付配置
     */
    protected T paymentConfig;

    protected BeanFactory beanFactory;

    public AbstractPlatformPaymentService(PaymentMethod paymentMethod, T paymentConfig) {
        this.paymentMethod = paymentMethod;
        this.paymentConfig = paymentConfig;
    }

    public AbstractPlatformPaymentService(PaymentPlatform paymentPlatform, PaymentMethod paymentMethod, T paymentConfig) {
        this(paymentMethod, paymentConfig);
        this.paymentPlatform = paymentPlatform;
    }

    @Override
    public PaymentPlatform getPaymentPlatform() {
        return this.paymentPlatform;
    }

    @Override
    public boolean isEnabled() {
        return this.paymentConfig.isEnabled();
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        if (this.callbackTemplate == null) {
            try {
                this.callbackTemplate = this.beanFactory.getBean(PaymentCallbackTemplate.class);
            } catch (BeansException e) {
                e.printStackTrace();
            }
        }

    }


    public void setCallbackTemplate(PaymentCallbackTemplate callbackTemplate) {
        this.callbackTemplate = callbackTemplate;
    }

    public void setPaymentPlatform(PaymentPlatform paymentPlatform) {
        this.paymentPlatform = paymentPlatform;
    }

    public void setPaymentConfig(T paymentConfig) {
        this.paymentConfig = paymentConfig;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

}
