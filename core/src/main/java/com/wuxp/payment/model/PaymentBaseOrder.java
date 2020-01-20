package com.wuxp.payment.model;

import com.wuxp.payment.enums.PaymentMethod;
import com.wuxp.payment.enums.PaymentPlatform;
import com.wuxp.payment.enums.TradeStatus;

/**
 * 支付订单
 */
public interface PaymentBaseOrder {

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

    /**
     * 获取第三方流水号
     *
     * @return
     */
    String getOutTradeNo();

    /**
     * 获取应用内的支付流水号
     *
     * @return
     */
    String getTradeNo();

//    /**
//     * 获取支付状态
//     *
//     * @return
//     */
//    TradeStatus getTradeStatus();
}
