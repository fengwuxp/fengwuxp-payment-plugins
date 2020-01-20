package com.wuxp.payment;

import lombok.Data;


/**
 * 支付宝的配置实现
 */
@Data
public class AliPayPaymentConfig implements PaymentConfig {


    /**
     * 网关URL
     */
    private String serviceUrl = "https://openapi.alipay.com/gateway.do";

    /**
     * 合作者(商户号)
     */
    private String partner;

    /**
     * 商户加密key
     */
    private String partnerSecret;

    /**
     * appId
     */
    private String appId;

    /**
     * AppSecret
     */
    private String AppSecret;

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


}
