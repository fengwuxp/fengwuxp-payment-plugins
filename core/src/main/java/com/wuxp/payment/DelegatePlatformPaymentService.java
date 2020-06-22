package com.wuxp.payment;

import com.wuxp.payment.enums.PaymentPlatform;
import com.wuxp.payment.model.PaymentBaseOrder;
import com.wuxp.payment.model.PlatformPaymentIdentity;
import com.wuxp.payment.model.PlatformPaymentPartnerIdentity;
import com.wuxp.payment.req.*;
import com.wuxp.payment.resp.OrderRefundResponse;
import com.wuxp.payment.resp.PreOrderResponse;
import com.wuxp.payment.resp.QueryOrderResponse;
import com.wuxp.payment.resp.QueryRefundOrderResponse;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;

/**
 * 代理的平台服务提供者
 *
 * @author wxup
 */
@Slf4j
@Setter
public class DelegatePlatformPaymentService implements PlatformPaymentService, BeanFactoryAware, InitializingBean {


    private PlatformPaymentServiceProvider platformPaymentServiceProvider;

    private BeanFactory beanFactory;


    @Override
    public void afterPropertiesSet() throws Exception {
        if (this.platformPaymentServiceProvider == null) {
            this.platformPaymentServiceProvider = this.beanFactory.getBean(PlatformPaymentServiceProvider.class);
        }
    }

    @Override
    public PaymentPlatform getPaymentPlatform() {
        return null;
    }

    @Override
    public String paymentProcess(PaymentNotifyProcessRequest request, PaymentBaseOrder paymentBaseOrder) {
        PlatformPaymentService platformPaymentService = this.platformPaymentServiceProvider.getPlatformPaymentService(paymentBaseOrder);
        return platformPaymentService.paymentProcess(request, paymentBaseOrder);
    }

    @Override
    public String refundProcess(RefundNotifyProcessRequest request, PaymentBaseOrder paymentBaseOrder) {
        PlatformPaymentService platformPaymentService = this.platformPaymentServiceProvider.getPlatformPaymentService(paymentBaseOrder);
        return platformPaymentService.refundProcess(request, paymentBaseOrder);
    }

    @Override
    public <T> PreOrderResponse<T> preOrder(PreOrderRequest request) {
        return this.platformPaymentServiceProvider.getPlatformPaymentService(request).preOrder(request);
    }

    @Override
    public QueryOrderResponse queryOrder(QueryOrderRequest request) {
        return this.platformPaymentServiceProvider.getPlatformPaymentService(request).queryOrder(request);
    }

    @Override
    public OrderRefundResponse orderRefund(RefundRequest request) {
        return this.platformPaymentServiceProvider.getPlatformPaymentService(request).orderRefund(request);
    }

    @Override
    public QueryRefundOrderResponse queryOrderRefund(QueryRefundRequest request) {
        return this.platformPaymentServiceProvider.getPlatformPaymentService(request).queryOrderRefund(request);
    }


    @Override
    public String getNotifyReturnCode(boolean success, PlatformPaymentPartnerIdentity partnerIdentity) {
        return this.platformPaymentServiceProvider.getPlatformPaymentService(partnerIdentity).getNotifyReturnCode(success);
    }
}
