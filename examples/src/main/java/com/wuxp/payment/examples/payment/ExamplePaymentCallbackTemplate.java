package com.wuxp.payment.examples.payment;

import com.wuxp.payment.PaymentCallbackTemplate;
import com.wuxp.payment.enums.NotifyMethod;
import com.wuxp.payment.model.PaymentBaseOrder;
import com.wuxp.payment.resp.QueryOrderResponse;
import com.wuxp.payment.resp.QueryRefundOrderResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author wxup
 */
@Slf4j
@Service
public class ExamplePaymentCallbackTemplate implements PaymentCallbackTemplate {


    @Override
    public boolean handlePaymentCallback(NotifyMethod notifyMethod, QueryOrderResponse response, PaymentBaseOrder paymentBaseOrder) {

        String tradeNo = paymentBaseOrder.getTradeNo().intern();
        synchronized (tradeNo) {
            log.debug("模拟订单支付回调处理 {}", paymentBaseOrder);
        }

        return false;
    }

    @Override
    public boolean handleRefundCallback(NotifyMethod notifyMethod, QueryRefundOrderResponse response, PaymentBaseOrder paymentBaseOrder) {
        String tradeNo = paymentBaseOrder.getTradeNo().intern();
        synchronized (tradeNo) {
            log.debug("模拟订单退款回调处理 {}", paymentBaseOrder);
        }

        return false;
    }
}
