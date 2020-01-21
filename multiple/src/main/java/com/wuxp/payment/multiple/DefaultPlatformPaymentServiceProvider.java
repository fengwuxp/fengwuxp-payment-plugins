package com.wuxp.payment.multiple;

import com.wuxp.payment.PaymentConfigProvider;
import com.wuxp.payment.PaymentPlugin;
import com.wuxp.payment.PlatformPaymentService;
import com.wuxp.payment.PlatformPaymentServiceProvider;
import com.wuxp.payment.enums.PaymentMethod;
import com.wuxp.payment.enums.PaymentPlatform;
import com.wuxp.payment.model.PlatformPaymentPartnerIdentity;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ObjectProvider;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


/**
 * 默认的平台
 */
@Slf4j
@Setter
public class DefaultPlatformPaymentServiceProvider implements PlatformPaymentServiceProvider, BeanFactoryAware, InitializingBean {


    private Map<PaymentPlatform, PaymentConfigProvider> paymentConfigProviders;

    private BeanFactory beanFactory;


    @Override
    public void afterPropertiesSet() throws Exception {
        if (this.paymentConfigProviders == null) {
            Map<PaymentPlatform, PaymentConfigProvider> paymentConfigProviders = new HashMap<>();
            ObjectProvider<PaymentConfigProvider> beanProvider = this.beanFactory.getBeanProvider(PaymentConfigProvider.class);
            beanProvider.stream().forEach(paymentConfigProvider -> {
                paymentConfigProviders.put(paymentConfigProvider.getPaymentPlatform(), paymentConfigProvider);
            });
            this.paymentConfigProviders = paymentConfigProviders;
            if (log.isDebugEnabled()) {
                log.debug("初始化默认的平台服务提供者，共获取到{}个只配置服务提供者", paymentConfigProviders.size());
            }
        }
    }

    @Override
    public PlatformPaymentService getPlatformPaymentService(PlatformPaymentPartnerIdentity partnerIdentity) {


        return null;
    }
}
