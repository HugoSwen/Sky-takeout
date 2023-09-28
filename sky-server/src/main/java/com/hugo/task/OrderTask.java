package com.hugo.task;

import com.hugo.constant.OrdersConstant;
import com.hugo.entity.Orders;
import com.hugo.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
public class OrderTask {

    @Autowired
    private OrderMapper orderMapper;

    /**
     * 每分钟执行一次支付超时查询
     */
    @Scheduled(cron = "0 * * * * ? ")
    public void CheckPaymentTimeOut() {
        log.info("订单支付超时处理...");

        LocalDateTime orderTime = LocalDateTime.now().minusMinutes(15);
        List<Orders> list = orderMapper.getByStatusAndOrderTime(OrdersConstant.PENDING_PAYMENT, orderTime);

        for (Orders orders : list) {
            orders.setStatus(OrdersConstant.CANCELLED);
            orders.setCancelTime(LocalDateTime.now());
            orders.setCancelReason("超时未支付");

            orderMapper.update(orders);
        }
    }

    /**
     * 每天凌晨一点查找前一天处于派送中的订单
     */
    @Scheduled(cron = "0 0 1 * * ? ")
    public void CheckDeliveryInProgress() {
        log.info("派送中订单未完成处理...");

        LocalDateTime orderTime = LocalDateTime.now().minusHours(1);
        List<Orders> list = orderMapper.getByStatusAndOrderTime(OrdersConstant.DELIVERY_IN_PROGRESS, orderTime);

        for (Orders orders : list) {
            orders.setStatus(OrdersConstant.COMPLETED);
            orders.setDeliveryTime(orderTime);

            orderMapper.update(orders);
        }
    }

}
