package com.wuxp.payment.resp;

import com.wuxp.payment.enums.PaymentMethod;
import com.wuxp.payment.enums.PaymentPlatform;
import com.wuxp.payment.req.PaymentBaseRequest;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * 抽象的响应对象
 * @author wuxp
 */
@Data
@Accessors(chain = true)
public abstract class AbstractPaymentResponse implements PaymentBaseResponse {

    private static final long serialVersionUID = -6719744679108332506L;

    @NotNull
    protected Boolean success = false;

    /**
     * 支付平台
     */
    @NotNull
    private PaymentPlatform paymentPlatform;

    /**
     * 支付方式
     */
    @NotNull
    private PaymentMethod paymentMethod;

    /**
     * 交易返回码
     */
    @NotNull
    protected String code;

    /**
     * 交易返回信息
     */
    @NotNull
    protected String message;

    /**
     * 原始响应信息
     */
    @NotNull
    protected Object rawResponse;

    public AbstractPaymentResponse() {
    }

    public AbstractPaymentResponse(PaymentBaseRequest request) {
        this.paymentMethod = request.getPaymentMethod();
        this.paymentPlatform = request.getPaymentPlatform();
    }

    @Override
    public boolean isSuccess() {
        return this.success;
    }

}
