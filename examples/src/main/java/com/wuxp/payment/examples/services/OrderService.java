package com.wuxp.payment.examples.services;

import com.wuxp.payment.examples.services.info.OrderInfo;

public interface OrderService {

    OrderInfo findOrderByNo(String treadNo);
}
