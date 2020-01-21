package com.wuxp.payment.springboot.configuration;


import com.wuxp.payment.DelegatePlatformPaymentService;
import com.wuxp.payment.PlatformPaymentServiceProvider;
import com.wuxp.payment.springboot.DefaultPlatformPaymentServiceProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PlatformPaymentConfiguration {


    @Bean
    @ConditionalOnMissingBean
    public DelegatePlatformPaymentService delegatePlatformPaymentService() {

        return new DelegatePlatformPaymentService();
    }

    @Bean
    @ConditionalOnMissingBean
    public PlatformPaymentServiceProvider platformPaymentServiceProvider() {
        return new DefaultPlatformPaymentServiceProvider();
    }

    @Bean
    @ConditionalOnMissingBean
    public PaymentPluginProperties paymentPluginProperties() {

        return new PaymentPluginProperties();
    }
}
