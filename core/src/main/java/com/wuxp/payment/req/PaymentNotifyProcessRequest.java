package com.wuxp.payment.req;

import com.wuxp.payment.enums.NotifyMethod;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * 支付通知参数处理
 *
 * @author wxup
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class PaymentNotifyProcessRequest extends AbstractPaymentRequest {

    private static final long serialVersionUID = -7245298267201732380L;

    /**
     * 应用内支付交易流水号
     */
    @NotNull
    private String tradeNo;

    /**
     * 订单总金额
     * 单位：分
     */
    @NotNull
    private Integer orderAmount;

    /**
     * 通知方式
     */
    @NotNull
    private NotifyMethod notifyMethod = NotifyMethod.ASYNC;

    /**
     * 支付通知表单参数
     */
    private Map<String, String> notifyParams;

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
