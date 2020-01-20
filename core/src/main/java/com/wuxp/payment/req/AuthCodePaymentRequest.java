package com.wuxp.payment.req;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

/**
 * 当面付
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AuthCodePaymentRequest extends PreOrderRequest {

    private static final long serialVersionUID = 6928416287624167924L;
    /**
     * 付款码支付
     */
    @NotBlank
    private String authCode;
}
