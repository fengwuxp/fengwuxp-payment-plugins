package com.wuxp.payment.alipay.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * app 支付返回结果
 */
@Data
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class AliPayQrCodeTradePayResult extends AliPayTradePayResult {

    /**
     * 二维码
     */
    private String qrCode;

    /**
     * 第三方交易流水号
     */
    private String outTradeNo;
}
