package com.wuxp.payment.resp;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 退款查询响应
 *
 * @author wuxp
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ToString(callSuper = true)
@NoArgsConstructor
public class QueryRefundOrderResponse extends OrderRefundResponse {

    private static final long serialVersionUID = 7408047023297147586L;


    /**
     * 第三方退款流水号
     */
    private String outTradeRefundNo;

}
