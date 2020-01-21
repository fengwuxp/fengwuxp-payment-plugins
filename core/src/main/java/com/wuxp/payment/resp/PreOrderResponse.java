package com.wuxp.payment.resp;

import com.wuxp.payment.req.PaymentBaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;


/**
 * 预下单响应结果
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@NoArgsConstructor
public class PreOrderResponse<T> extends AbstractPaymentResponse {

    private static final long serialVersionUID = -8386852184980588202L;


    /**
     * 应用内的交易流水号
     */
    @NotNull
    private String tradeNo;

    /**
     * 第三方交易流水号
     */
    private String outTradeNo;


    /**
     * 订单金额
     * 单位分
     */
    private Integer orderAmount;

    /**
     * 是否沙箱环境
     */
    private Boolean useSandboxEnv = false;

    /**
     * 支付结果
     */
    private T result;

    public PreOrderResponse(PaymentBaseRequest request) {
        super(request);
    }

    public <T> T getResult() {
        return (T) result;
    }
}
