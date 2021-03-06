package com.wuxp.payment.model;


/**
 * 支付订单
 *
 * @author wxup
 */
public interface PaymentBaseOrder extends PlatformPaymentPartnerIdentity {


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

    /**
     * 支付金额
     *
     * @return
     */
    Integer getAmount();

//    /**
//     * 获取支付状态
//     *
//     * @return
//     */
//    TradeStatus getTradeStatus();
}
