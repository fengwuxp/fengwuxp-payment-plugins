package com.wuxp.payment.examples.services.info;

import com.wuxp.payment.enums.PaymentMethod;
import com.wuxp.payment.enums.PaymentPlatform;
import com.wuxp.payment.model.PaymentBaseOrder;
import lombok.Data;

/**
 * 订单信息
 */
@Data
public class OrderInfo implements PaymentBaseOrder {

    private Long id;

    private Long parentId;

    private String tradeNo;

    private String outTradeNo;

    private PaymentMethod paymentMethod;

    private PaymentPlatform paymentPlatform;

    private Integer orderAmount;

    public Long getParentId() {
        return parentId;
    }
}
