package com.hugo.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hugo.constant.MessageConstant;
import com.hugo.dto.DishDTO;
import com.hugo.dto.DishPageQueryDTO;
import com.hugo.entity.Dish;
import com.hugo.entity.DishFlavor;
import com.hugo.exception.DeletionNotAllowedException;
import com.hugo.mapper.DishFlavorMapper;
import com.hugo.mapper.DishMapper;
import com.hugo.mapper.SetMealDishMapper;
import com.hugo.mapper.SetMealMapper;
import com.hugo.result.PageResult;
import com.hugo.service.DishService;
import com.hugo.vo.DishVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private DishFlavorMapper dishFlavorMapper;

    @Autowired
    private SetMealDishMapper setMealDishMapper;

    @Transactional
    @Override
    public void addDishWithFlavor(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        dishMapper.insert(dish);
        // 主键返回
        Long id = dish.getId();

        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (flavors != null && !flavors.isEmpty())
        {
            flavors.forEach(flavor -> flavor.setDishId(id));
            dishFlavorMapper.insertBatch(flavors);
        }
    }

    @Override
    public PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO) {
        PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());

        Page<DishVO> page = dishMapper.pageQuery(dishPageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Transactional
    @Override
    public void deleteBatch(List<Long> ids) {
        // 判断菜品是否起售
        Long enableCnt = dishMapper.countEnableByIds(ids);
        if (enableCnt > 0)
            throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);

        // 判断菜品是否关联了套餐
        Long setMealCnt = setMealDishMapper.countByDishIds(ids);
        if (setMealCnt > 0)
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);

        // 删除菜品
        dishMapper.deleteBatch(ids);

        // 删除菜品对应的口味
        dishFlavorMapper.deleteByDishIds(ids);
    }

    @Override
    public DishDTO getByIdWithFlavor(Long id) {
        Dish dish = dishMapper.getById(id);
        List<DishFlavor> flavors = dishFlavorMapper.getByDishId(id);

        DishDTO dishDTO = new DishDTO();
        BeanUtils.copyProperties(dish ,dishDTO);
        dishDTO.setFlavors(flavors);
        return dishDTO;
    }

    @Transactional
    @Override
    public void updateWithFlavor(DishDTO dishDTO) {
        Dish dish = new Dish();
        List<DishFlavor> flavors;

        BeanUtils.copyProperties(dishDTO, dish);
        flavors = dishDTO.getFlavors();
        flavors.forEach(flavor -> flavor.setDishId(dish.getId()));

        // 修改菜品表及其基本信息
        dishMapper.update(dish);
        // 删除口味表原来的信息
        dishFlavorMapper.deleteByDishId(dish.getId());
        // 插入口味信息
        dishFlavorMapper.insertBatch(flavors);
    }
}
