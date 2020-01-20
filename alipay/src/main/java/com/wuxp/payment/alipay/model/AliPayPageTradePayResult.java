package com.wuxp.payment.alipay.model;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * page 支付返回结果
 */
@Data
@Accessors(chain = true)
public class AliPayPageTradePayResult extends AliPayTradePayResult {

    /**
     * 用于唤起app支付的字符串
     */
    private String orderInfo;

    private String merchantOrderNo;

    private String outTradeNo;

    private String sellerId;

    private String totalAmount;

    private String tradeNo;
}
