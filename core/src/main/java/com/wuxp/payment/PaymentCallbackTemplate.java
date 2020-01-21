package com.wuxp.payment;


import com.wuxp.payment.enums.NotifyMethod;
import com.wuxp.payment.model.PaymentBaseOrder;
import com.wuxp.payment.resp.QueryOrderResponse;
import com.wuxp.payment.resp.QueryRefundOrderResponse;

/**
 * 支付回调模处理模板方法
 */
public interface PaymentCallbackTemplate {


    /**
     * 处理支付回调
     *
     * @param notifyMethod
     * @param response
     * @param paymentBaseOrder
     * @return
     */
    boolean handlePaymentCallback(NotifyMethod notifyMethod, QueryOrderResponse response, PaymentBaseOrder paymentBaseOrder);

    /**
     * 处理支付退款回调
     *
     * @param notifyMethod
     * @param response
     * @param paymentBaseOrder
     * @return
     */
    boolean handleRefundCallback(NotifyMethod notifyMethod, QueryRefundOrderResponse response, PaymentBaseOrder paymentBaseOrder);
}
