package com.hugo.controller.admin;

import com.hugo.dto.CategoryDTO;
import com.hugo.dto.CategoryPageQueryDTO;
import com.hugo.entity.Category;
import com.hugo.result.PageResult;
import com.hugo.result.Result;
import com.hugo.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("admin/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 新增分类
     */
    @PostMapping
    public Result addCategory(@RequestBody CategoryDTO categoryDTO) {
        log.info("新增分类：{}", categoryDTO);

        categoryService.addCategory(categoryDTO);
        return Result.success();
    }

    /**
     * 分类分页查询
     */
    @GetMapping("/page")
    public Result<PageResult> pageQuery(CategoryPageQueryDTO categoryPageQueryDTO) {
        log.info("员工分页查询：{}", categoryPageQueryDTO);

        PageResult pageResult = categoryService.pageQuery(categoryPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 修改分类
     */
    @PutMapping
    public Result update(@RequestBody CategoryDTO categoryDTO) {
        log.info("修改分类：{}", categoryDTO);

        categoryService.update(categoryDTO);
        return Result.success();
    }

    /**
     * 启用禁用分类
     */
    @PostMapping("/status/{status}")
    public Result enableOrDisable(@PathVariable Integer status, Long id) {
        log.info("启用禁用分类：{},{}", status, id);

        categoryService.setStatus(status, id);
        return Result.success();
    }

    /**
     * 根据类型查询分类
     */
    @GetMapping("/list")
    public Result<List<Category>> getByType(Integer type) {
        log.info("根据类型查询分类：{}", type);

        List<Category> list = categoryService.getByType(type);
        return Result.success(list);
    }

    /**
     * 根据Id删除分类
     */
    @DeleteMapping
    public Result deleteById(Integer id) {
        log.info("根据id删除分类：{}", id);

        categoryService.deleteById(id);
        return Result.success();
    }
}
