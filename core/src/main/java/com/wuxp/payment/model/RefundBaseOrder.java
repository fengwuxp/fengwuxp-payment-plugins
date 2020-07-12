package com.wuxp.payment.model;


/**
 * 退款单
 *
 * @author wxup
 */
public interface RefundBaseOrder extends PlatformPaymentPartnerIdentity {


    /**
     * 获取第三方退款流水号
     *
     * @return
     */
    String getOutTradeNo();

    /**
     * 获取应用内的退款流水号
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
