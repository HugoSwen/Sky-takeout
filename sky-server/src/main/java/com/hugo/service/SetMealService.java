package com.hugo.service;

import com.hugo.dto.SetMealDTO;
import com.hugo.dto.SetMealPageQueryDTO;
import com.hugo.result.PageResult;
import com.hugo.vo.SetMealVO;

import java.util.List;

public interface SetMealService {
    void addSetMeal(SetMealDTO setmealDTO);

    PageResult pageQuery(SetMealPageQueryDTO setMealPageQueryDTO);

    SetMealVO getByIdWithDishes(Long id);

    void updateWithDishes(SetMealDTO setMealDTO);

    void setStatus(Integer status, Long id);

    void deleteBatch(List<Long> ids);
}
