package com.hugo.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hugo.annotation.AutoFill;
import com.hugo.constant.MessageConstant;
import com.hugo.constant.StatusConstant;
import com.hugo.context.BaseContext;
import com.hugo.dto.CategoryDTO;
import com.hugo.dto.CategoryPageQueryDTO;
import com.hugo.entity.Category;
import com.hugo.enumeration.OperationType;
import com.hugo.exception.DeletionNotAllowedException;
import com.hugo.mapper.CategoryMapper;
import com.hugo.mapper.DishMapper;
import com.hugo.mapper.SetMealMapper;
import com.hugo.result.PageResult;
import com.hugo.service.CategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private SetMealMapper setMealMapper;

    @Override
    public void addCategory(CategoryDTO categoryDTO) {
        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO, category);
        categoryMapper.insert(category);
    }

    @Override
    public PageResult pageQuery(CategoryPageQueryDTO categoryPageQueryDTO) {
        PageHelper.startPage(categoryPageQueryDTO.getPage(), categoryPageQueryDTO.getPageSize());

        Page<Category> page = categoryMapper.pageQuery(categoryPageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public void update(CategoryDTO categoryDTO) {
        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO, category);
        categoryMapper.update(category);
    }

    @Override
    public void setStatus(Integer status, Long id) {
        Category category = Category.builder()
                .id(id)
                .status(status)
                .build();

        categoryMapper.update(category);
    }

    @Override
    public List<Category> getByType(Integer type) {
        return categoryMapper.getByType(type);
    }

    @Override
    public void deleteById(Long id) {
        // 分类是否启用
        Category category = categoryMapper.getById(id);

        if (Objects.equals(category.getStatus(), StatusConstant.ENABLE))
            throw new DeletionNotAllowedException(MessageConstant.CATEGORY_ENABLE);

        // 分类是否关联了菜品或者套餐
        Long dishCnt = dishMapper.countByCategoryId(id);
        if (dishCnt > 0)
            throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_DISH);

        Long setMealCnt = setMealMapper.countByCategoryId(id);
        if (setMealCnt > 0)
            throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_SETMEAL);

        categoryMapper.deleteById(id);
    }

}
