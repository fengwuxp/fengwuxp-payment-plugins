package com.wuxp.payment.req;

import com.wuxp.payment.enums.PaymentMethod;
import com.wuxp.payment.enums.PaymentPlatform;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

@Data
@Accessors(chain = true)
public abstract class AbstractPaymentRequest  implements PaymentBaseRequest{

    private static final long serialVersionUID = -8578569660707494474L;
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
}
