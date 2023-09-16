package com.hugo.controller.user;

import com.hugo.entity.Dish;
import com.hugo.entity.SetMeal;
import com.hugo.result.Result;
import com.hugo.service.SetMealService;
import com.hugo.vo.DishItemVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.util.List;

@Api(tags = "套餐相关接口")
@Slf4j
@RestController("userSetMealController")
@RequestMapping("/user/setmeal")
public class SetMealController {

    @Autowired
    private SetMealService setMealService;

    /**
     * 根据分类id查询套餐
     */
    @ApiOperation(value = "根据分类id查询套餐")
    @Cacheable(cacheNames = "userCache:setMeal", key = "#categoryId")
    @GetMapping("/list")
    public Result<List<SetMeal>> list(Long categoryId){
        log.info("根据分类id查询套餐：{}", categoryId);

        List<SetMeal> list = setMealService.getByCategoryId(categoryId);
        return Result.success(list);
    }

    /**
     * 根据套餐id查询菜品
     */
    @ApiOperation(value = "根据套餐id查询菜品")
    @Cacheable(cacheNames = "userCache:setMeal:dish", key = "#setMealId")
    @GetMapping("/dish/{id}")
    public Result<List<DishItemVO>> getDishItemBySetMealId(@PathVariable("id") Long setMealId){
        log.info("根据套餐id查询菜品：{}", setMealId);

        List<DishItemVO> list = setMealService.getDishItemBySetMealId(setMealId);
        return Result.success(list);
    }

}
