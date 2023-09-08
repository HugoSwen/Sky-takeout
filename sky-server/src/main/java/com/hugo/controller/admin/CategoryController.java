package com.hugo.controller.admin;

import com.hugo.dto.CategoryDTO;
import com.hugo.result.Result;
import com.hugo.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("admin/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public Result addCategory(CategoryDTO categoryDTO){
        log.info("新增分类：{}", categoryDTO);
        return Result.success();
    }
}
