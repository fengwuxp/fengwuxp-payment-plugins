package com.wuxp.payment.springboot.configuration;


import com.wuxp.payment.DelegatePlatformPaymentService;
import com.wuxp.payment.PlatformPaymentServiceProvider;
import com.wuxp.payment.springboot.DefaultPlatformPaymentServiceProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author wxup
 */
@Configuration
@EnableConfigurationProperties(PaymentPluginProperties.class)
@ConditionalOnProperty(prefix = PaymentPluginProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
public class PlatformPaymentAutoConfiguration {


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

}
