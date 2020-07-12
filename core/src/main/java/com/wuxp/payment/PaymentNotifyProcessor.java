package com.wuxp.payment;

import com.wuxp.payment.model.PaymentBaseOrder;
import com.wuxp.payment.model.PlatformPaymentIdentity;
import com.wuxp.payment.model.PlatformPaymentPartnerIdentity;
import com.wuxp.payment.model.RefundBaseOrder;
import com.wuxp.payment.req.PaymentNotifyProcessRequest;
import com.wuxp.payment.req.RefundNotifyProcessRequest;

/**
 * 支付通知处理者
 *
 * @author wxup
 * @see PaymentCallbackTemplate
 */
public interface PaymentNotifyProcessor {


    /**
     * 处理支付通知
     *
     * @param request          支付通知请求信息
     * @param paymentBaseOrder 支付单信息
     * @return 处理成功或者失败的响应码 {@link #getNotifyReturnCode(boolean)}
     * @throws RuntimeException 处理失败抛出异常
     */
    String paymentProcess(PaymentNotifyProcessRequest request, PaymentBaseOrder paymentBaseOrder) throws RuntimeException;

    /**
     * 处理退款通知
     *
     * @param request          退款请求通知信息
     * @param refundBaseOrder  退款单信息
     * @return 处理成功或者失败的响应码 {@link #getNotifyReturnCode(boolean)}
     */
    String refundProcess(RefundNotifyProcessRequest request, RefundBaseOrder refundBaseOrder);


    /**
     * 获取通知响应码
     *
     * @param success 是否处理成功
     * @return 处理成功或者失败的响应码
     */
    default String getNotifyReturnCode(boolean success) {
        return null;
    }

    /**
     * 获取通知响应码
     *
     * @param success  是否处理成功
     * @param partnerIdentity 识别支付平台
     * @return 处理成功或者失败的响应码
     */
    default String getNotifyReturnCode(boolean success,PlatformPaymentPartnerIdentity partnerIdentity) {
        return this.getNotifyReturnCode(success);
    }

}
