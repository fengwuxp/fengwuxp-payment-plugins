package com.wuxp.payment.examples.payment;

import com.wuxp.payment.AliPayPaymentConfig;
import com.wuxp.payment.PaymentConfiguration;
import com.wuxp.payment.PaymentConfigurationProvider;
import com.wuxp.payment.enums.PaymentMethod;
import com.wuxp.payment.enums.PaymentPlatform;
import com.wuxp.payment.model.PlatformPaymentPartnerIdentity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Memory的支付配置提供者
 *
 * @author wxup
 */
@Slf4j
@Component
public class MemoryPaymentConfigurationProvider implements PaymentConfigurationProvider {


    @Override
    public PaymentConfiguration getPaymentConfig(PlatformPaymentPartnerIdentity identity) {

        PaymentPlatform paymentPlatform = identity.getPaymentPlatform();
        switch (paymentPlatform) {
            case ALI_PAY:
                return aliPayPaymentConfig(identity);
            case WE_CHAT:
            default:
        }

        return null;
    }


    protected PaymentConfiguration aliPayPaymentConfig(PlatformPaymentPartnerIdentity identity) {

        AliPayPaymentConfig aliPayPaymentConfig = new AliPayPaymentConfig();
        aliPayPaymentConfig.setAliPayPublicKey("11")
                .setAppPrivateKey("11")
                .setAppId("111")
                .setAppSecret("111")
                .setEnabled(true)
                .setPartner("222")
                .setPaymentMethod(PaymentMethod.APP);
        return aliPayPaymentConfig;

    }
}
