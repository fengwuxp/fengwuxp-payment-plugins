package com.wuxp.payment.wechat;

import com.wuxp.payment.PaymentPlugin;
import com.wuxp.payment.req.PreOrderRequest;
import com.wuxp.payment.resp.PreOrderResponse;
import com.wuxp.payment.wechat.req.PagePreOrderRequest;

/**
* @author: zhuox
* @create: 2020-02-05
* @description: 微信网页支付
**/
public interface WechatPagePaymentPlugin extends PaymentPlugin {


    /**
     * 网页支付
     *
     * @param request
     * @return
     */
    <T> PreOrderResponse<T> payment(PagePreOrderRequest request);

    @Override
    default <T> PreOrderResponse<T> preOrder(PreOrderRequest request) {
        return this.payment((PagePreOrderRequest) request);
    }
}
