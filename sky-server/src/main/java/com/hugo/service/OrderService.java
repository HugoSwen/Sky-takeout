package com.hugo.service;

import com.hugo.dto.*;
import com.hugo.result.PageResult;
import com.hugo.vo.OrderPaymentVO;
import com.hugo.vo.OrderStatisticsVO;
import com.hugo.vo.OrderSubmitVO;
import com.hugo.vo.OrderVO;

public interface OrderService {
    OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO);

   OrderPaymentVO payOrder(OrdersPaymentDTO ordersPaymentDTO);

    void paySuccess(String orderNumber);

    OrderVO getById(Long id);

    void userCancelOrder(Long id);

    PageResult pageQuery4User(int page, int pageSize, Integer status);

    void repeatOrder(Long id);

    PageResult pageQuery4Admin(OrdersPageQueryDTO ordersPageQueryDTO);

    OrderStatisticsVO getOrderStatistics();

    void confirmOrder(OrdersConfirmDTO ordersConfirmDTO);

    void rejectOrder(OrdersRejectionDTO ordersRejectionDTO);

 void adminCancelOrder(OrdersCancelDTO ordersCancelDTO);

    void deliveryOrder(Long id);

    void completeOrder(Long id);

    void remindOrder(Long id);
}
