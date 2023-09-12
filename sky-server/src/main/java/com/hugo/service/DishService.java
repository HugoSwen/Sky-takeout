package com.hugo.service;

import com.hugo.dto.DishDTO;
import com.hugo.dto.DishPageQueryDTO;
import com.hugo.result.PageResult;
import com.hugo.vo.DishVO;

import java.util.List;

public interface DishService {
    void addDishWithFlavor(DishDTO dishDTO);

    PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO);

    void deleteBatch(List<Long> ids);

    DishDTO getByIdWithFlavor(Long id);

    void updateWithFlavor(DishDTO dishDTO);

    void setStatus(Integer status, Long id);
}
