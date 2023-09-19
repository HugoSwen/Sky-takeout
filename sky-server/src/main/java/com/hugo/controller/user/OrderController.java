package com.hugo.controller.user;

import com.hugo.context.BaseContext;
import com.hugo.dto.OrdersSubmitDTO;
import com.hugo.result.Result;
import com.hugo.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = "订单相关接口")
@Slf4j
@RestController("userOrderController")
@RequestMapping("/user/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 用户下单
     */
    @ApiOperation(value = "用户下单")
    @PostMapping("/submit")
    public Result submitOrder(@RequestBody OrdersSubmitDTO ordersSubmitDTO){
        log.info("用户下单，用户id：{}，订单：{}", BaseContext.getCurrentId(), ordersSubmitDTO);

        orderService.submit(ordersSubmitDTO);
        return Result.success();
    }
}
