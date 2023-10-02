package com.hugo.service.impl;

import com.hugo.constant.OrdersConstant;
import com.hugo.mapper.DishMapper;
import com.hugo.mapper.OrderMapper;
import com.hugo.mapper.SetMealMapper;
import com.hugo.mapper.UserMapper;
import com.hugo.service.WorkspaceService;
import com.hugo.vo.BusinessDataVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
    public BusinessDataVO getBusinessData() {
        LocalDateTime beginTime = LocalDateTime.MIN;
        LocalDateTime endTime = LocalDateTime.MAX;

        // 新增用户数量
        Long newUsers = userMapper.countByMap(beginTime, endTime);

        // 营业额
        Double turnover = orderMapper.sumByMap(beginTime, endTime, OrdersConstant.COMPLETED);
        turnover = turnover == null ? 0.0 : turnover;

        // 总订单数
        Long totalOrderCount = orderMapper.countByMap(beginTime, endTime, null);

        // 有效订单数
        Long validOrderCount = orderMapper.countByMap(beginTime, endTime, OrdersConstant.COMPLETED);

        // 订单完成率
        Double orderCompletionRate = totalOrderCount == 0 ? 0.0 : validOrderCount.doubleValue() / totalOrderCount;


    }
}
