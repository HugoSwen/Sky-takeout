package com.hugo.service.impl;

import com.hugo.constant.OrdersConstant;
import com.hugo.constant.StatusConstant;
import com.hugo.mapper.DishMapper;
import com.hugo.mapper.OrderMapper;
import com.hugo.mapper.SetMealMapper;
import com.hugo.mapper.UserMapper;
import com.hugo.service.WorkspaceService;
import com.hugo.vo.BusinessDataVO;
import com.hugo.vo.DishOverViewVO;
import com.hugo.vo.OrderOverViewVO;
import com.hugo.vo.SetMealOverViewVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class WorkspaceServiceImpl implements WorkspaceService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private SetMealMapper setMealMapper;

    @Override
    public BusinessDataVO getBusinessData(LocalDateTime begin, LocalDateTime end) {

        // 新增用户数量
        Long newUsers = userMapper.countByMap(begin, end);

        // 营业额
        Double turnover = orderMapper.sumByMap(begin, end, OrdersConstant.COMPLETED);
        turnover = turnover == null ? 0.0 : turnover;

        // 总订单数
        Long totalOrderCount = orderMapper.countByMap(begin, end, null);

        // 有效订单数
        Long validOrderCount = orderMapper.countByMap(begin, end, OrdersConstant.COMPLETED);

        // 订单完成率
        Double orderCompletionRate = totalOrderCount == 0 ? 0.0 : validOrderCount.doubleValue() / totalOrderCount;

        // 平均客单价
        Double unitPrice = validOrderCount == 0 ? 0.0 : turnover / validOrderCount;

        return BusinessDataVO.builder()
                .newUsers(newUsers)
                .turnover(turnover)
                .validOrderCount(validOrderCount)
                .orderCompletionRate(orderCompletionRate)
                .unitPrice(unitPrice)
                .build();
    }

    @Override
    public DishOverViewVO getDishOverView() {
        Long sold = dishMapper.countByMap(StatusConstant.ENABLE);
        Long discontinued = dishMapper.countByMap(StatusConstant.DISABLE);

        return DishOverViewVO.builder()
                .sold(sold)
                .discontinued(discontinued)
                .build();
    }

    @Override
    public SetMealOverViewVO getSetMealOverView() {
        Long sold = setMealMapper.countByMap(StatusConstant.ENABLE);
        Long discontinued = setMealMapper.countByMap(StatusConstant.DISABLE);

        return SetMealOverViewVO.builder()
                .sold(sold)
                .discontinued(discontinued)
                .build();
    }

    @Override
    public OrderOverViewVO getOrderOverView() {
        LocalDateTime begin = LocalDateTime.now().with(LocalTime.MIN);
        LocalDateTime end = LocalDateTime.now().with(LocalTime.MAX);

        // 全部订单
        Long allOrders = orderMapper.countByMap(begin, end, null);

        // 已取消订单
        Long cancelledOrders = orderMapper.countByMap(begin, end, OrdersConstant.CANCELLED);

        // 已完成订单
        Long completedOrders = orderMapper.countByMap(begin, end, OrdersConstant.COMPLETED);

        // 派送中订单
        Long deliveredOrders = orderMapper.countByMap(begin, end, OrdersConstant.DELIVERY_IN_PROGRESS);

        // 待接单订单
        Long waitingOrders = orderMapper.countByMap(begin, end, OrdersConstant.TO_BE_CONFIRMED);

        return OrderOverViewVO.builder()
                .allOrders(allOrders)
                .cancelledOrders(cancelledOrders)
                .completedOrders(completedOrders)
                .deliveredOrders(deliveredOrders)
                .waitingOrders(waitingOrders)
                .build();
    }
}
