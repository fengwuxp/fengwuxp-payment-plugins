package com.wuxp.payment.examples.services;

import com.wuxp.payment.examples.services.info.OrderInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    @Override
    public OrderInfo findOrderByNo(String treadNo) {
        OrderInfo orderInfo = new OrderInfo();
        return orderInfo;
    }
}
