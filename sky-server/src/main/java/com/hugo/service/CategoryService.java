package com.hugo.service;

import com.hugo.dto.CategoryDTO;
import com.hugo.dto.CategoryPageQueryDTO;
import com.hugo.entity.Category;
import com.hugo.result.PageResult;

import java.util.List;

public interface CategoryService {
    void addCategory(CategoryDTO categoryDTO);

    PageResult pageQuery(CategoryPageQueryDTO categoryPageQueryDTO);

    void update(CategoryDTO categoryDTO);

    void setStatus(Integer status, Long id);

    List<Category> getByType(Integer type);

    void deleteById(Integer id);
}
