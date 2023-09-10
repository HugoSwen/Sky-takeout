package com.hugo.controller.admin;

import com.hugo.dto.DishDTO;
import com.hugo.result.Result;
import com.hugo.service.DishService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "菜品相关接口")
@Slf4j
@RestController
@RequestMapping("/admin/dish")
public class DishController {

    @Autowired
    private DishService dishService;

    @ApiOperation(value = "新增菜品")
    @PostMapping
    public Result addDishWithFlavor(@RequestBody DishDTO dishDTO){
        log.info("新增菜品及口味：{}",dishDTO);

        dishService.addDishWithFlavor(dishDTO);
        return Result.success();
    }
}
