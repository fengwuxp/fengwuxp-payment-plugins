package com.wuxp.payment.req;

import com.wuxp.payment.enums.NotifyMethod;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * 退款通知处理
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class RefundNotifyProcessRequest extends AbstractPaymentRequest {


    private static final long serialVersionUID = -3785149106590765490L;
    /**
     * 应用内退款交易流水号
     */
    @NotNull
    private String refundTradeNo;

    /**
     * 订单总金额
     * 单位：分
     */
    @NotNull
    private Integer orderAmount;

    /**
     * 退款金额
     * 单位：分
     */
    @NotNull
    @Min(value = 1)
    private Integer refundAmount;

    /**
     * 通知方式
     */
    @NotNull
    private NotifyMethod notifyMethod = NotifyMethod.ASYNC;

    /**
     * 支付通知表单参数
     */
    private Map<String, Object> notifyParams;

    /**
     * 如果通知的内容在 RequestBody中，需要该参数
     */
    private Object requestBody;

    public <T> T getRequestBody() {
        if (requestBody == null) {
            return null;
        }
        return (T) requestBody;
    }
}
