package com.wuxp.payment.req;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 查询订单请求对象
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryOrderRequest extends AbstractPaymentRequest {

    private static final long serialVersionUID = -1838887158000195846L;

    /**
     * 应用内的交易流水号
     */
    private String tradeNo;

    /**
     * 第三方交易流水号
     */
    private String outTradeNo;


}
