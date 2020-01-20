package com.wuxp.payment.multiple;

import com.wuxp.payment.PaymentPlugin;
import com.wuxp.payment.enums.PaymentMethod;
import com.wuxp.payment.enums.PaymentPlatform;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;


/**
 * 默认的插件提供者
 */
@Slf4j
public class DefaultPaymentPluginProvider implements PaymentPluginProvider {


    @Override
    public Set<PaymentPlugin> getAllPlugins(String parentId) {
        return null;
    }


    @Override
    public PaymentPlugin getPaymentPlugin(String parentId, PaymentPlatform paymentPlatform, PaymentMethod paymentMethod) {
        return null;
    }
}
