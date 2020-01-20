package com.wuxp.payment.req;

import com.wuxp.payment.enums.PaymentMethod;
import com.wuxp.payment.enums.PaymentPlatform;

import java.io.Serializable;

/**
 * 用于发起支付相关的请求
 * 如预下单、订单查询、退款等
 */
public interface PaymentBaseRequest extends Serializable {

    /**
     * 获取支付平台
     *
     * @return
     */
    PaymentPlatform getPaymentPlatform();

    /**
     * 获取支付方式
     *
     * @return
     */
    PaymentMethod getPaymentMethod();


}
