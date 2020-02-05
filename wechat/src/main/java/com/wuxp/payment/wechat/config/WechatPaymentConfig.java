package com.wuxp.payment.wechat.config;

import com.wuxp.payment.AbstractPaymentConfiguration;
import com.wuxp.payment.enums.PaymentPlatform;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: zhuox
 * @create: 2020-02-04
 * @description: 微信支付配置
 **/
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@Data
public class WechatPaymentConfig extends AbstractPaymentConfiguration {

    /**
     * 设置微信公众号或者小程序等的appid
     */
    private String appId;

    /**
     * 微信支付商户号
     */
    private String mchId;

    /**
     * 微信支付商户密钥
     */
    private String mchKey;

    /**
     * 服务商模式下的子商户公众账号ID，普通模式请不要配置，请在配置文件中将对应项删除
     */
    private String subAppId;

    /**
     * 服务商模式下的子商户号，普通模式请不要配置，最好是请在配置文件中将对应项删除
     */
    private String subMchId;

    /**
     * apiclient_cert.p12文件的绝对路径，或者如果放在项目中，请以classpath:开头指定
     */
    private String keyPath;



    public WechatPaymentConfig() {
        this.paymentPlatform = PaymentPlatform.WE_CHAT;
    }
}
