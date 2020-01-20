package com.wuxp.payment.req;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 交易退款请求
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class RefundRequest extends AbstractPaymentRequest {


    private static final long serialVersionUID = 8514897252783130486L;

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
    private String requestRefundNo;

    /**
     * 退款金额
     * 单位：分
     */
    @NotNull
    @Min(value = 1)
    private Integer refundAmount;


    /**
     * 订单总金额
     * 单位：分
     */
    @NotNull
    @Min(value = 1)
    private Integer orderAmount;

    /**
     * 退款通知url
     */
    @NotBlank
    private String refundNotifyUrl;

    /**
     * 退款原因
     */
    private String refundReason;


}
