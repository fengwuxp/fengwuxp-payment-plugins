package com.wuxp.payment;

import com.wuxp.payment.alipay.AbstractAliPayPaymentService;
import com.wuxp.payment.alipay.AliPayAppPaymentService;
import com.wuxp.payment.alipay.AliPayAuthCodePaymentService;
import com.wuxp.payment.alipay.AliPayQrCodePaymentService;
import com.wuxp.payment.enums.PaymentMethod;
import com.wuxp.payment.req.PreOrderRequest;
import com.wuxp.payment.resp.PreOrderResponse;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;

import javax.validation.constraints.NotNull;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Alipay 统一的支付
 */
@Slf4j
public class AliPayPlatformPaymentService extends AbstractAliPayPaymentService implements BeanFactoryAware, InitializingBean {

    private Map<PaymentMethod, AbstractAliPayPaymentService> paymentServices = new HashMap<>(6);

    private BeanFactory beanFactory;

    public AliPayPlatformPaymentService(AliPayPaymentConfig payPaymentConfig) {
        super(payPaymentConfig);
        this.paymentServices.put(PaymentMethod.APP, new AliPayAppPaymentService(payPaymentConfig));
        this.paymentServices.put(PaymentMethod.AUTH_CODE, new AliPayAuthCodePaymentService(payPaymentConfig));
        this.paymentServices.put(PaymentMethod.SCAN_QR_CODE, new AliPayQrCodePaymentService(payPaymentConfig));
    }


    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

        AliPayAuthCodePaymentService abstractAliPayPaymentService = (AliPayAuthCodePaymentService) this.paymentServices.get(PaymentMethod.AUTH_CODE);
        abstractAliPayPaymentService.setPaymentHandleStrategy(this.beanFactory.getBean(AuthCodePaymentHandleStrategy.class));

    }

    @Override
    public <T> PreOrderResponse<T> preOrder(PreOrderRequest request) {
        @NotNull PaymentMethod paymentMethod = request.getPaymentMethod();
        AbstractAliPayPaymentService payPaymentService = this.paymentServices.get(paymentMethod);
        if (payPaymentService == null) {
            throw new RuntimeException(MessageFormat.format("未找到支付宝的{0}支付方式实现", paymentMethod));
        }
        return payPaymentService.preOrder(request);
    }
}
