package com.wuxp.payment.resp;

import com.sun.istack.internal.NotNull;
import com.wuxp.payment.enums.TradeStatus;
import com.wuxp.payment.req.PaymentBaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 订单查询响应信息
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@NoArgsConstructor
public class QueryOrderResponse extends AbstractPaymentResponse {

    private static final long serialVersionUID = 2443519753568133262L;


    /**
     * 支付交易流水号(系统内的)
     */
    @NotNull
    private String tradeNo;

    /**
     * 第三方交易流水号
     */
    private String outTradeNo;

    /**
     * 订单金额
     * 单位：分
     */
    private Integer orderAmount;

    /**
     * 实付金额
     * 单元分
     * 买家实际付款的金额
     */
    private Integer buyerPayAmount;

    /**
     * 实收金额
     * 单位分
     * 该金额为本笔交易，商户账户能够实际收到的金额
     */
    private Integer receiptAmount;

    /**
     * 是否沙箱环境
     */
    private Boolean useSandboxEnv = false;

    /**
     * 交易状态
     */
    private TradeStatus tradeStatus;

    /**
     * 付款账号
     */
    private String payerAccount;

    public QueryOrderResponse(PaymentBaseRequest request) {
        super(request);
    }
}
