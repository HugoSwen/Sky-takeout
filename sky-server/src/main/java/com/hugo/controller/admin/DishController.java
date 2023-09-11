package com.hugo.controller.admin;

import com.hugo.dto.DishDTO;
import com.hugo.dto.DishPageQueryDTO;
import com.hugo.result.PageResult;
import com.hugo.result.Result;
import com.hugo.service.DishService;
import com.hugo.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "菜品相关接口")
@Slf4j
@RestController
@RequestMapping("/admin/dish")
public class DishController {

    @Autowired
    private DishService dishService;

    /**
     * 新增菜品
     */
    @ApiOperation(value = "新增菜品")
    @PostMapping
    public Result addDishWithFlavor(@RequestBody DishDTO dishDTO){
        log.info("新增菜品及口味：{}",dishDTO);

        dishService.addDishWithFlavor(dishDTO);
        return Result.success();
    }

    /**
     * 菜品分页查询
     */
    @GetMapping("/page")
    public Result<PageResult> pageQuery(DishPageQueryDTO dishPageQueryDTO){
        log.info("菜品分页查询，页码：{}，页面大小：{}", dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());

        PageResult pageResult = dishService.pageQuery(dishPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 根据id查询菜品及其口味
     */
    @GetMapping("/{id}")
    public Result<DishDTO> getByIdWithFlavor(@PathVariable Long id){
        log.info("根据id查询菜品及其口味：{}", id);

        DishDTO dishDTO = dishService.getByIdWithFlavor(id);
        return Result.success(dishDTO);
    }

    /**
     * 修改菜品信息
     */
    // TODO 完善sql语句
    @PutMapping
    public Result update(@RequestBody DishDTO dishDTO){
        log.info("修改菜品及其口味信息：{}", dishDTO);

        dishService.updateWithFlavor(dishDTO);
        return Result.success();
    }

    /**
     * 删除菜品
     */
    @DeleteMapping
    public Result delete(@RequestParam List<Long> ids){
        log.info("批量删除菜品：{}", ids);

        dishService.deleteBatch(ids);
        return Result.success();
    }
}
