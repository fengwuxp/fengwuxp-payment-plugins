package com.wuxp.payment;


import com.wuxp.payment.enums.PaymentPlatform;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


/**
 * 支付宝的配置实现
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AliPayPaymentConfig extends AbstractPaymentConfiguration {


    /**
     * 网关URL
     */
    private String serviceUrl = "https://openapi.alipay.com/gateway.do";


    /**
     * 私钥
     */
    private String appPrivateKey;

    /**
     * 公钥
     */
    private String aliPayPublicKey;

    /**
     * 字符编码
     */
    private String charset = "UTF-8";

    public AliPayPaymentConfig() {
        this.paymentPlatform = PaymentPlatform.ALI_PAY;
    }
}
