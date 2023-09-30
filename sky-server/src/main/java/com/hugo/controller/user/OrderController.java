package com.hugo.controller.user;

import com.hugo.context.BaseContext;
import com.hugo.dto.OrdersPaymentDTO;
import com.hugo.dto.OrdersSubmitDTO;
import com.hugo.result.PageResult;
import com.hugo.result.Result;
import com.hugo.service.OrderService;
import com.hugo.vo.OrderPaymentVO;
import com.hugo.vo.OrderSubmitVO;
import com.hugo.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = "用户端订单相关接口")
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
    public Result<OrderSubmitVO> submitOrder(@RequestBody OrdersSubmitDTO ordersSubmitDTO) {
        log.info("用户下单，用户id：{}，订单：{}", BaseContext.getCurrentId(), ordersSubmitDTO);

        OrderSubmitVO orderSubmitVO = orderService.submitOrder(ordersSubmitDTO);
        return Result.success(orderSubmitVO);
    }

    @ApiOperation(value = "订单支付")
    @PutMapping("/payment")
    public Result payOrder(@RequestBody OrdersPaymentDTO ordersPaymentDTO) {
        log.info("订单支付, 订单号：{}，支付方式：{}", ordersPaymentDTO.getOrderNumber(), ordersPaymentDTO.getPayMethod());

        String orderNumber = ordersPaymentDTO.getOrderNumber();
        // 模拟生成预支付交易单
        OrderPaymentVO orderPaymentVO = orderService.payOrder(ordersPaymentDTO);
        log.info("订单号：{}，模拟生成预支付交易单成功", orderNumber);

        // 模拟支付成功
        log.info("订单号：{}，模拟支付成功!", orderNumber);
        orderService.paySuccess(orderNumber);
        return Result.success();
    }

    /**
     * 查询订单详细
     */
    @ApiOperation(value = "订单信息")
    @GetMapping("/orderDetail/{id}")
    public Result<OrderVO> getOrderDetail(@PathVariable Long id) {
        log.info("用户查询订单，id：{}", id);

        OrderVO orderVO = orderService.getById(id);
        return Result.success(orderVO);
    }

    /**
     * 历史订单查询
     */
    @ApiOperation(value = "历史订单查询")
    @GetMapping("/historyOrders")
    public Result<PageResult> pageQuery(@RequestParam(defaultValue = "1") int page,
                                        @RequestParam(defaultValue = "10") int pageSize,
                                        Integer status) {
        log.info("用户历史订单分页查询...");

        PageResult pageResult = orderService.pageQuery4User(page, pageSize, status);
        return Result.success(pageResult);
    }

    /**
     * 取消订单
     */
    @ApiOperation(value = "取消订单")
    @PutMapping("/cancel/{id}")
    public Result cancelOrder(@PathVariable Long id) {
        log.info("用户取消订单， id：{}", id);

        orderService.userCancelOrder(id);
        return Result.success();
    }

    /**
     * 再来一单
     */
    @ApiOperation(value = "再来一单")
    @PostMapping("/repetition/{id}")
    public Result repeatOrder(@PathVariable Long id) {
        log.info("用户再来一单，原订单id：{}", id);

        orderService.repeatOrder(id);
        return Result.success();
    }

    /**
     * 客户催单
     */
    @ApiOperation(value = "催单")
    @GetMapping("reminder/{id}")
    public Result remindOrder(@PathVariable Long id) {
        log.info("客户催单，订单id：{}", id);

        orderService.remindOrder(id);
        return Result.success();
    }

}
