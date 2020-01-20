package com.wuxp.payment;

import com.wuxp.payment.req.AuthCodePaymentRequest;
import com.wuxp.payment.req.PreOrderRequest;
import com.wuxp.payment.resp.PreOrderResponse;

/**
 * 当面付（授权码支付）
 */
public interface AuthCodePaymentPlugin extends PaymentPlugin {


    /**
     * 当面付
     *
     * @param request
     * @return
     */
    <T> PreOrderResponse<T> payment(AuthCodePaymentRequest request);

    @Override
    default <T> PreOrderResponse<T> preOrder(PreOrderRequest request) {
        return this.payment((AuthCodePaymentRequest) request);
    }
}
