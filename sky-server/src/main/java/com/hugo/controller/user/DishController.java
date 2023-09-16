package com.hugo.controller.user;

import com.hugo.entity.Dish;
import com.hugo.result.Result;
import com.hugo.service.DishService;
import com.hugo.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "菜品相关接口")
@Slf4j
@RestController("userDishController")
@RequestMapping("/user/dish")
public class DishController {

    @Autowired
    private DishService dishService;

    @ApiOperation(value = "根据分类id查询菜品")
    @Cacheable(cacheNames = "userCache:dish", key = "#categoryId")
    @GetMapping("/list")
    public Result<List<DishVO>> list(Long categoryId){
        log.info("根据分类id查询菜品：{}", categoryId);

        List<DishVO> list = dishService.getByCategoryIdWithFlavor(categoryId);
        return Result.success(list);
    }
}
