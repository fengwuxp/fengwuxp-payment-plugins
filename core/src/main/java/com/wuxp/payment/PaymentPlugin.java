package com.wuxp.payment;


import com.wuxp.payment.req.PreOrderRequest;
import com.wuxp.payment.req.QueryOrderRequest;
import com.wuxp.payment.req.QueryRefundRequest;
import com.wuxp.payment.req.RefundRequest;
import com.wuxp.payment.resp.OrderRefundResponse;
import com.wuxp.payment.resp.PreOrderResponse;
import com.wuxp.payment.resp.QueryOrderResponse;
import com.wuxp.payment.resp.QueryRefundOrderResponse;

/**
 * 支付插件接口
 * 适配不同第三方平台的支付，实现统一调用
 */
public interface PaymentPlugin {


    /**
     * 预下单
     *
     * @param request
     * @return {@see PreOrderResponse}
     */
   <T> PreOrderResponse<T> preOrder(PreOrderRequest request);


    /**
     * 查询订单
     *
     * @param request
     * @return
     */
    QueryOrderResponse queryOrder(QueryOrderRequest request);

    /**
     * 订单退款
     *
     * @param request
     * @return
     */
    OrderRefundResponse orderRefund(RefundRequest request);

    /**
     * 订单退款查询
     *
     * @param request
     * @return
     */
    QueryRefundOrderResponse queryOrderRefund(QueryRefundRequest request);

}
