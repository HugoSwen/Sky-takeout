package com.hugo.controller.admin;

import com.hugo.dto.OrdersCancelDTO;
import com.hugo.dto.OrdersConfirmDTO;
import com.hugo.dto.OrdersPageQueryDTO;
import com.hugo.dto.OrdersRejectionDTO;
import com.hugo.result.PageResult;
import com.hugo.result.Result;
import com.hugo.service.OrderService;
import com.hugo.vo.OrderStatisticsVO;
import com.hugo.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@Api(tags = "管理端订单相关接口")
@Slf4j
@RestController("adminOrderController")
@RequestMapping("/admin/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 搜索订单
     */
    @ApiOperation(value = "搜索订单")
    @GetMapping("/conditionSearch")
    public Result<PageResult> conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO) {
        log.info("订单搜索：{}", ordersPageQueryDTO);

        PageResult pageResult = orderService.pageQuery4Admin(ordersPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 查询订单详细
     */
    @ApiOperation(value = "查询订单详细")
    @GetMapping("/details/{id}")
    public Result<OrderVO> getOrderDetail(@PathVariable Long id) {
        log.info("商家查询订单详细，订单id：{}", id);

        OrderVO orderVO = orderService.getById(id);
        return Result.success(orderVO);
    }

    /**
     * 各个状态的订单数量统计
     */
    @ApiOperation(value = "各个状态订单数量统计")
    @GetMapping("/statistics")
    public Result<OrderStatisticsVO> getOrderStatistics() {
        log.info("各个状态订单数量统计...");

        OrderStatisticsVO orderStatisticsVO = orderService.getOrderStatistics();
        return Result.success(orderStatisticsVO);
    }

    /**
     * 接单
     */
    @ApiOperation(value = "接单")
    @PutMapping("/confirm")
    public Result confirmOrder(@RequestBody OrdersConfirmDTO ordersConfirmDTO) {
        log.info("商家接单，订单id：{}", ordersConfirmDTO.getId());

        orderService.confirmOrder(ordersConfirmDTO);
        return Result.success();
    }

    /**
     * 拒单
     */
    @ApiOperation(value = "拒单")
    @PutMapping("/rejection")
    public Result rejectOrder(@RequestBody OrdersRejectionDTO ordersRejectionDTO) {
        log.info("商家拒单，订单id：{}，拒单原因：{}", ordersRejectionDTO.getId(), ordersRejectionDTO.getRejectionReason());

        orderService.rejectOrder(ordersRejectionDTO);
        return Result.success();
    }

    /**
     * 取消订单
     */
    @ApiOperation(value = "取消订单")
    @PutMapping("/cancel")
    public Result cancelOrder(@RequestBody OrdersCancelDTO ordersCancelDTO) {
        log.info("商家取消订单，订单id：{}，取消原因：{}", ordersCancelDTO.getId(), ordersCancelDTO.getCancelReason());

        orderService.adminCancelOrder(ordersCancelDTO);
        return Result.success();
    }

    /**
     * 派送订单
     */
    @ApiOperation(value = "派送订单")
    @PutMapping("/delivery/{id}")
    public Result deliveryOrder(@PathVariable Long id) {
        log.info("派送订单，订单id：{}", id);

        orderService.deliveryOrder(id);
        return Result.success();
    }

    /**
     * 完成订单
     */
    @ApiOperation(value = "完成订单")
    @PutMapping("/complete/{id}")
    public Result completeOrder(@PathVariable Long id) {
        log.info("完成订单，订单id：{}", id);

        orderService.completeOrder(id);
        return Result.success();
    }

}
