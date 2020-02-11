package com.wuxp.payment.springboot;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.wuxp.payment.*;
import com.wuxp.payment.alipay.AliPayAppPaymentService;
import com.wuxp.payment.alipay.AliPayAuthCodePaymentService;
import com.wuxp.payment.alipay.AliPayPagePaymentService;
import com.wuxp.payment.alipay.AliPayQrCodePaymentService;
import com.wuxp.payment.enums.PaymentMethod;
import com.wuxp.payment.enums.PaymentPlatform;
import com.wuxp.payment.model.PlatformPaymentPartnerIdentity;
import com.wuxp.payment.springboot.configuration.PaymentPluginProperties;
import com.wuxp.payment.wechat.*;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;


/**
 * 默认的平台支付服务提供者
 */
@Slf4j
@Setter
public class DefaultPlatformPaymentServiceProvider implements PlatformPaymentServiceProvider, BeanFactoryAware, InitializingBean {

    private final static Map<PaymentPlatform, Map<PaymentMethod, Class<? extends PlatformPaymentService>>> PAYMENT_PLATFORM_CLASS_MAP = new HashMap<>(16);

    private PaymentConfigurationProvider paymentConfigurationProvider;

    private BeanFactory beanFactory;

    private PaymentCallbackTemplateProvider paymentCallbackTemplateProvider;

    private Cache<PaymentPlatform, PlatformPaymentService> PAYMENT_PLATFORM_CACHE = Caffeine.newBuilder()
            .expireAfterWrite(1, TimeUnit.DAYS)
            .maximumSize(16 * 7)
            .build();


    static {
        initAliPay();
        initWechatPay();
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        if (this.paymentConfigurationProvider == null) {

            this.paymentConfigurationProvider = this.beanFactory.getBean(PaymentConfigurationProvider.class);
        }
        if (this.paymentCallbackTemplateProvider == null) {
            this.paymentCallbackTemplateProvider = this.beanFactory.getBean(PaymentCallbackTemplateProvider.class);
        }
        PaymentPluginProperties pluginProperties = this.beanFactory.getBean(PaymentPluginProperties.class);
        PAYMENT_PLATFORM_CACHE = Caffeine.newBuilder()
                .expireAfterWrite(pluginProperties.getCacheTimeout())
                .maximumSize(pluginProperties.getCacheSize() * 7)
                .build();


    }

    @Override
    public PlatformPaymentService getPlatformPaymentService(PlatformPaymentPartnerIdentity partnerIdentity) {

        PaymentPlatform paymentPlatform = partnerIdentity.getPaymentPlatform();

        PlatformPaymentService platformPaymentService = PAYMENT_PLATFORM_CACHE.getIfPresent(paymentPlatform);
        if (platformPaymentService == null) {
            platformPaymentService = this.newInstancePlatformPaymentService(partnerIdentity);
            PAYMENT_PLATFORM_CACHE.put(paymentPlatform, platformPaymentService);
        }

        if (!platformPaymentService.isEnabled()) {
            throw new RuntimeException(MessageFormat.format("平台：{0}的支付方式{1}，尚未启用", paymentPlatform, partnerIdentity.getPaymentMethod()));
        }

        return platformPaymentService;
    }

    protected PlatformPaymentService newInstancePlatformPaymentService(PlatformPaymentPartnerIdentity partnerIdentity) {
        PaymentPlatform paymentPlatform = partnerIdentity.getPaymentPlatform();
        Map<PaymentMethod, Class<? extends PlatformPaymentService>> classMap = PAYMENT_PLATFORM_CLASS_MAP.get(paymentPlatform);
        Class<? extends PlatformPaymentService> aClass = classMap.get(partnerIdentity.getPaymentMethod());
        if (aClass == null) {
            throw new RuntimeException(MessageFormat.format("平台：{0}，未存在支付服务", paymentPlatform));
        }

        Constructor<PlatformPaymentService>[] constructors = (Constructor<PlatformPaymentService>[]) aClass.getConstructors();
        PlatformPaymentService paymentService;
        Optional<Constructor<PlatformPaymentService>> optional = Arrays.stream(constructors)
                .filter(constructor -> constructor.getParameters().length == 0)
                .findFirst();
        if (!optional.isPresent()) {
            throw new RuntimeException(MessageFormat.format("平台：{0}的支付服务：{1} 未存在空构造", aClass, paymentPlatform));
        }
        try {
            paymentService = optional.get().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        if (paymentService instanceof AbstractPlatformPaymentService) {
            PaymentConfigurationProvider paymentConfigProvider = this.paymentConfigurationProvider;
            PaymentCallbackTemplateProvider callbackTemplateProvider = this.paymentCallbackTemplateProvider;
            ((AbstractPlatformPaymentService) paymentService).setPaymentConfig(paymentConfigProvider.getPaymentConfig(partnerIdentity));
            ((AbstractPlatformPaymentService) paymentService).setCallbackTemplate(callbackTemplateProvider.getPaymentCallbackTemplate(partnerIdentity));
            ((AbstractPlatformPaymentService) paymentService).setPaymentPlatform(paymentPlatform);
        }


        if (paymentService instanceof BeanFactoryAware) {
            ((BeanFactoryAware) paymentService).setBeanFactory(this.beanFactory);
        }

        if (paymentService instanceof InitializingBean) {
            try {
                ((InitializingBean) paymentService).afterPropertiesSet();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        return paymentService;
    }

    /**
     * 初始化支付宝的服务
     */
    private static void initAliPay() {
        Map<PaymentMethod, Class<? extends PlatformPaymentService>> classMap = new HashMap<>(7);
        classMap.put(PaymentMethod.AUTH_CODE, AliPayAuthCodePaymentService.class);
        classMap.put(PaymentMethod.APP, AliPayAppPaymentService.class);
        classMap.put(PaymentMethod.SCAN_QR_CODE, AliPayQrCodePaymentService.class);
        classMap.put(PaymentMethod.PC_BROWSER, AliPayPagePaymentService.class);
        PAYMENT_PLATFORM_CLASS_MAP.put(PaymentPlatform.ALI_PAY, classMap);
    }

    /**
     * 初始化微信支付服务
     */
    private static void initWechatPay() {
        Map<PaymentMethod, Class<? extends PlatformPaymentService>> classMap = new HashMap<>(8);
        classMap.put(PaymentMethod.AUTH_CODE, WechatMicropayPaymentService.class);
        classMap.put(PaymentMethod.APP, WechatAppPaymentService.class);
        classMap.put(PaymentMethod.SCAN_QR_CODE, WechatScanPaymentService.class);
        classMap.put(PaymentMethod.PC_BROWSER, WechatPagePaymentService.class);
        classMap.put(PaymentMethod.JS_API, WechatJsPaymentService.class);
        PAYMENT_PLATFORM_CLASS_MAP.put(PaymentPlatform.WE_CHAT, classMap);
    }
}
