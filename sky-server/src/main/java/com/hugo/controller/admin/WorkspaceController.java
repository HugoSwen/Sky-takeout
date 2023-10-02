package com.hugo.controller.admin;

import com.hugo.result.Result;
import com.hugo.service.WorkspaceService;
import com.hugo.vo.BusinessDataVO;
import com.hugo.vo.DishOverViewVO;
import com.hugo.vo.OrderOverViewVO;
import com.hugo.vo.SetMealOverViewVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "工作台相关接口")
@Slf4j
@RestController
@RequestMapping("/admin/workspace")
public class WorkspaceController {

    @Autowired
    private WorkspaceService workspaceService;

    /**
     * 今日运营数据
     */
    @ApiOperation(value = "今日运营数据")
    @GetMapping("/businessData")
    public Result<BusinessDataVO> getBusinessData() {
        BusinessDataVO businessDataVO = workspaceService.getBusinessData();
        return Result.success(businessDataVO);
    }

    /**
     * 菜品总览
     */
    @ApiOperation(value = "菜品总览")
    @GetMapping("/overviewDishes")
    public Result<DishOverViewVO> getDishOverView() {
        DishOverViewVO dishOverViewVO = workspaceService.getDishOverView();
        return Result.success(dishOverViewVO);
    }

    /**
     * 套餐总览
     */
    @ApiOperation(value = "套餐总览")
    @GetMapping("/overviewSetmeals")
    public Result<SetMealOverViewVO> getSetMealOverView() {
        SetMealOverViewVO setMealOverViewVO = workspaceService.getSetMealOverView();
        return Result.success(setMealOverViewVO);
    }

    /**
     * 订单总览
     */
    @ApiOperation(value = "订单总览")
    @GetMapping("/overviewOrders")
    public Result<OrderOverViewVO> getOrderOverView() {
        OrderOverViewVO orderOverViewVO = workspaceService.getOrderOverView();
        return Result.success(orderOverViewVO);
    }
}
