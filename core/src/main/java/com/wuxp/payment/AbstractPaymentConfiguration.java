package com.wuxp.payment;

import com.wuxp.payment.enums.PaymentMethod;
import com.wuxp.payment.enums.PaymentPlatform;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * @author wxup
 */
@Data
@Accessors(chain = true)
public abstract class AbstractPaymentConfiguration implements PaymentConfiguration {


    /**
     * appId
     */
    protected String appId;


    /**
     * AppSecret
     */
    protected String appSecret;

    /**
     * 合作者(商户号)
     */
    protected String partner;

    /**
     * 商户加密key，需要在微信商户平台进行设置
     */
    protected String partnerSecret;

    /**
     * 支付平台
     */
    @NotNull
    protected PaymentPlatform paymentPlatform;

    /**
     * 支付方式
     */
    @NotNull
    protected PaymentMethod paymentMethod;

    /**
     * 是否启用
     */
    protected Boolean enabled;

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
}
