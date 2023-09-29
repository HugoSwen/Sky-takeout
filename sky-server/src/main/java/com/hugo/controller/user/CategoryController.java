package com.hugo.controller.user;

import com.hugo.entity.Category;
import com.hugo.result.Result;
import com.hugo.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "分类相关接口")
@Slf4j
@RestController("userCategoryController")
@RequestMapping("/user/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @ApiOperation(value = "根据类型获取分类")
    @Cacheable(cacheNames = "userCache:category", sync = true)
    @GetMapping("/list")
    public Result<List<Category>> list(Integer type){
        log.info("根据type（可为null）获取分类：{}", type);

        List<Category> list = categoryService.getByType(type);
        return Result.success(list);
    }
}
