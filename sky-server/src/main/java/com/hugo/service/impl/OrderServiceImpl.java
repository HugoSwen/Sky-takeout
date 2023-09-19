package com.hugo.service.impl;

import com.hugo.dto.OrdersSubmitDTO;
import com.hugo.mapper.OrderDetailMapper;
import com.hugo.mapper.OrderMapper;
import com.hugo.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderDetailMapper orderDetailMapper;

    @Override
    public void submit(OrdersSubmitDTO ordersSubmitDTO) {

    }
}
