package com.wuxp.payment.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 交易状态
 */
@AllArgsConstructor
@Getter
public enum TradeStatus {


    CLOSED("已关闭"),

    /**
     * 支付中
     */
    WAIT_PAY("支付中"),

    /**
     * 支付成功
     */
    SUCCESS("支付成功"),

    /**
     * 支付失败
     */
    FAILURE("支付失败"),

    /**
     * 未知状态
     */
    UNKNOWN("未知"),

    /**
     * 退款等待中
     */
    WAIT_REFUND("等待退款"),

    /**
     * 部分退款
     */
    PARTIAL_REFUND("部分退款"),

    /**
     * 已退款
     */
    REFUNDED("已退款"),

    NOT_PAY("未支付"),

    REFUND_FAILURE("退款失败");

    private String desc;
}
