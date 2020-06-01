package com.wuxp.payment.examples.controller;

import com.wuxp.payment.PlatformPaymentService;
import com.wuxp.payment.enums.NotifyMethod;
import com.wuxp.payment.enums.PaymentMethod;
import com.wuxp.payment.examples.services.OrderService;
import com.wuxp.payment.examples.services.info.OrderInfo;
import com.wuxp.payment.req.PaymentNotifyProcessRequest;
import com.wuxp.payment.req.PreOrderRequest;
import com.wuxp.payment.resp.PreOrderResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private PlatformPaymentService platformPaymentService;

    @Autowired
    private OrderService orderService;

    /**
     * 预下单测试
     *
     * @param request
     * @return
     */
    @RequestMapping("/pre_oder")
    public PreOrderResponse preOrder(PreOrderRequest request) {

        // TODO
        request.setPartnerId(null);

        return platformPaymentService.preOrder(request);
    }


    /**
     * 回调测试测试
     *
     * @param request
     * @return
     */
    @RequestMapping("/notify/{notifyMethod}/{treadNo}/")
    public String paymentNotifyCallback(@PathVariable NotifyMethod notifyMethod,
                                        @PathVariable String treadNo,
                                        HttpServletRequest request) {

        OrderInfo orderInfo = orderService.findOrderByNo(treadNo);
        if (orderInfo == null) {
            return null;
        }
        PaymentNotifyProcessRequest paymentNotifyProcessRequest = new PaymentNotifyProcessRequest();
        Map<String, String> params = new HashMap<>();
        for (String key : request.getParameterMap().keySet()) {
            String[] value = request.getParameterMap().get(key);
            if (value != null && value.length > 0) {
                params.put(key, value[0]);
            }
        }

        paymentNotifyProcessRequest.setNotifyMethod(notifyMethod)
                .setNotifyParams(params)
                .setTradeNo(treadNo)
                .setOrderAmount(orderInfo.getOrderAmount())
                .setPaymentMethod(orderInfo.getPaymentMethod())
                .setPaymentPlatform(orderInfo.getPaymentPlatform());

        return platformPaymentService.paymentProcess(paymentNotifyProcessRequest, orderInfo);
    }

    /**
     * 回调测试测试
     *
     * @return
     */
    @RequestMapping("/notify_2/{notifyMethod}/{treadNo}/")
    public String paymentNotifyCallbackByEntry(@PathVariable NotifyMethod notifyMethod,
                                               @PathVariable String treadNo,
                                               @RequestBody String requestBody,
                                               @RequestParam Map<String, String> requestParams) {

        OrderInfo orderInfo = orderService.findOrderByNo(treadNo);
        if (orderInfo == null) {
            return null;
        }
        PaymentNotifyProcessRequest paymentNotifyProcessRequest = new PaymentNotifyProcessRequest();
        paymentNotifyProcessRequest.setNotifyMethod(notifyMethod)
                .setNotifyParams(requestParams)
                .setRequestBody(requestBody)
                .setTradeNo(treadNo)
                .setOrderAmount(orderInfo.getOrderAmount())
                .setPaymentMethod(orderInfo.getPaymentMethod())
                .setPaymentPlatform(orderInfo.getPaymentPlatform());

        return platformPaymentService.paymentProcess(paymentNotifyProcessRequest, orderInfo);
    }

}
