package com.wuxp.payment;

import com.wuxp.payment.model.PaymentBaseOrder;
import com.wuxp.payment.req.PaymentNotifyProcessRequest;
import com.wuxp.payment.req.RefundNotifyProcessRequest;

/**
 * 支付通知处理
 *
 * @see PaymentCallbackTemplate
 */
public interface PaymentNotifyProcessor {


    /**
     * 处理支付通知
     *
     * @param request
     * @param paymentBaseOrder 订单信息
     * @return
     */
    String paymentProcess(PaymentNotifyProcessRequest request, PaymentBaseOrder paymentBaseOrder);

    /**
     * 处理退款通知
     *
     * @param request
     * @param paymentBaseOrder 订单信息
     * @return
     */
    String refundProcess(RefundNotifyProcessRequest request, PaymentBaseOrder paymentBaseOrder);

}
