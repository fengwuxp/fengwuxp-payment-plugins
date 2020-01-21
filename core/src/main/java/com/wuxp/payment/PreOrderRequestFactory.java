package com.wuxp.payment;

import com.wuxp.payment.req.PreOrderRequest;

/**
 * 用于创建  对象的工厂，使用更复杂的支付场景
 *
 * @see PreOrderRequest
 */
public interface PreOrderRequestFactory<T> {


    /**
     * @param data
     * @return
     */
    PreOrderRequest factory(T data);

    /**
     * @param request
     * @param data
     * @return
     */
    PreOrderRequest factory(PreOrderRequest request, T data);

}
