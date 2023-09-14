package com.hugo.controller.admin;

import com.hugo.dto.SetMealDTO;
import com.hugo.dto.SetMealPageQueryDTO;
import com.hugo.result.PageResult;
import com.hugo.result.Result;
import com.hugo.service.SetMealService;
import com.hugo.vo.SetMealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "套餐相关接口")
@Slf4j
@RestController("adminSetMealController")
@RequestMapping("/admin/setmeal")
public class SetMealController {

    @Autowired
    private SetMealService setMealService;

    /**
     * 新增套餐
     */
    @ApiOperation(value = "新增套餐")
    @PostMapping
    public Result addSetMeal(@RequestBody SetMealDTO setmealDTO) {
        log.info("新增套餐：{}", setmealDTO);

        setMealService.addSetMeal(setmealDTO);
        return Result.success();
    }

    /**
     * 套餐页面查询
     */
    @ApiOperation(value = "套餐页面查询")
    @GetMapping("/page")
    public Result<PageResult> pageQuery(SetMealPageQueryDTO setMealPageQueryDTO) {
        log.info("套餐页面查询：{}", setMealPageQueryDTO);

        PageResult pageResult = setMealService.pageQuery(setMealPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 根据id查询套餐
     */
    @ApiOperation(value = "根据id查询套餐")
    @GetMapping("/{id}")
    public Result<SetMealVO> getByIdWithDishes(@PathVariable Long id){
        log.info("根据id查询套餐：{}", id);

        SetMealVO setMealVO = setMealService.getByIdWithDishes(id);
        return Result.success(setMealVO);
    }

    /**
     * 修改套餐
     */
    @ApiOperation(value = "修改套餐")
    @PutMapping
    public Result update(@RequestBody SetMealDTO setMealDTO){
        log.info("修改套餐：{}",setMealDTO);

        setMealService.updateWithDishes(setMealDTO);
        return Result.success();
    }

    /**
     * 套餐起售停售
     */
    @ApiOperation(value = "套餐起售停售")
    @PostMapping("/status/{status}")
    public Result enableOrDisable(@PathVariable Integer status, Long id){
        log.info("套餐起售停售，套餐id：{}，设置状态：{}", id, status);

        setMealService.setStatus(status, id);
        return Result.success();
    }

    /**
     * 套餐删除
     */
    @ApiOperation(value = "批量删除套餐")
    @DeleteMapping
    public Result delete(@RequestParam List<Long> ids){
        log.info("批量删除套餐：{}", ids);

        setMealService.deleteBatch(ids);
        return Result.success();
    }

}
