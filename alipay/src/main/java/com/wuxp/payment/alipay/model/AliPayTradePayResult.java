package com.wuxp.payment.alipay.model;


import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 支付宝交易返回结果
 */
@Data
@Accessors(chain = true)
public abstract class AliPayTradePayResult {


    /**
     * 响应码
     */
    private String code;

    /**
     * 消息
     */
    private String message;
}
