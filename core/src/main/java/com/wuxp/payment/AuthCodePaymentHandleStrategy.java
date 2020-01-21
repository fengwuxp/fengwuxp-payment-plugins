package com.wuxp.payment;

import com.wuxp.payment.req.AuthCodePaymentRequest;
import com.wuxp.payment.resp.PreOrderResponse;

/**
 * 授权码支付处理策略
 * 由于授权码支付可能没有异步回调，需要应用程序做异步的查询，并且模拟业务回调
 */
public interface AuthCodePaymentHandleStrategy {


    /**
     * 必须使用异步处理实现
     *
     * @param response
     * @param request
     */
    void handle(PreOrderResponse response, AuthCodePaymentRequest request);

}
