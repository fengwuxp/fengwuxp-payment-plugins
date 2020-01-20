package com.wuxp.payment.multiple;

import com.wuxp.payment.PaymentPlugin;
import com.wuxp.payment.enums.PaymentMethod;
import com.wuxp.payment.enums.PaymentPlatform;

import java.util.List;
import java.util.Set;

/**
 * 支付插件提供者
 */
public interface PaymentPluginProvider {


    /**
     * 获取所有的支付插件
     *
     * @param parentId
     * @return
     */
    Set<PaymentPlugin> getAllPlugins(String parentId);


    /**
     * 获取支付插件
     *
     * @param parentId        商户在应用系统的唯一标识
     * @param paymentPlatform 支付的平台
     * @param paymentMethod   支付的方式
     * @return
     */
    PaymentPlugin getPaymentPlugin(String parentId, PaymentPlatform paymentPlatform, PaymentMethod paymentMethod);


}
