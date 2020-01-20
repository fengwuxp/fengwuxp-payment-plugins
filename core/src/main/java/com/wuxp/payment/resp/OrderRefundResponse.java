package com.wuxp.payment.resp;

import com.wuxp.payment.enums.TradeStatus;
import com.wuxp.payment.req.PaymentBaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 订单退款响应
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@NoArgsConstructor
public class OrderRefundResponse extends AbstractPaymentResponse {


    private static final long serialVersionUID = 4065370331573700214L;


    /**
     * 交易状态
     */
    private TradeStatus tradeStatus;

    /**
     * 退款的金额
     * 单位：分
     */
    private Integer refundAmount;

    /**
     * 订单金额
     * 单位分
     */
    private Integer orderAmount;

    /**
     * 应用内的退款流水号
     */
    private String tradeRefundNo;



    /**
     * 是否全额退款
     *
     * @return
     */
    public boolean isFullRefund() {
        return refundAmount.equals(orderAmount);
    }

    public OrderRefundResponse(PaymentBaseRequest request) {
        super(request);
    }
}
