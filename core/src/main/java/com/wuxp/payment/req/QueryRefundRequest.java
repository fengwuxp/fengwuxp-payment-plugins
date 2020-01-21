package com.wuxp.payment.req;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 交易退款请求
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryRefundRequest extends AbstractPaymentRequest {


    private static final long serialVersionUID = 7569941115473623611L;

    /**
     * 应用内的交易流水号
     */
    private String tradeNo;

    /**
     * 第三方交易流水号
     */
    private String outTradeNo;

    /**
     * 交易退款流水号
     */
    private String requestRefundSn;

    /**
     * 第三方退款流水号
     */
    private String outTradeRefundSn;

}
