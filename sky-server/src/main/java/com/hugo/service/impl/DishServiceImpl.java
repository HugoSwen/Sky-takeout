package com.hugo.service.impl;

import com.aliyuncs.ram.model.v20150501.ListUsersForGroupRequest;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hugo.constant.MessageConstant;
import com.hugo.constant.StatusConstant;
import com.hugo.dto.DishDTO;
import com.hugo.dto.DishPageQueryDTO;
import com.hugo.entity.Dish;
import com.hugo.entity.DishFlavor;
import com.hugo.entity.SetMeal;
import com.hugo.exception.DeletionNotAllowedException;
import com.hugo.mapper.DishFlavorMapper;
import com.hugo.mapper.DishMapper;
import com.hugo.mapper.SetMealDishMapper;
import com.hugo.mapper.SetMealMapper;
import com.hugo.result.PageResult;
import com.hugo.service.DishService;
import com.hugo.service.SetMealService;
import com.hugo.vo.DishVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;
    @Autowired
    private SetMealDishMapper setMealDishMapper;
    @Autowired
    private SetMealService setMealService;

    @Transactional
    @Override
    public void addDishWithFlavor(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        dishMapper.insert(dish);
        // 主键返回
        Long id = dish.getId();

        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (flavors != null && !flavors.isEmpty()) {
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
    public DishVO getByIdWithFlavor(Long id) {
        Dish dish = dishMapper.getById(id);
        List<DishFlavor> flavors = dishFlavorMapper.getByDishId(id);

        DishVO dishVO = new DishVO();
        BeanUtils.copyProperties(dish, dishVO);
        dishVO.setFlavors(flavors);
        return dishVO;
    }

    @Transactional
    @Override
    public void updateWithFlavor(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);

        // 修改菜品表及其基本信息
        dishMapper.update(dish);
        // 删除口味表原来的信息
        dishFlavorMapper.deleteByDishId(dish.getId());

        List<DishFlavor> flavors = dishDTO.getFlavors();
        // 插入口味信息
        if (flavors != null && !flavors.isEmpty()) {
            flavors.forEach(flavor -> flavor.setDishId(dish.getId()));
            dishFlavorMapper.insertBatch(flavors);
        }
    }

    @Override
    public void setStatus(Integer status, Long id) {
        if (Objects.equals(status, StatusConstant.DISABLE)) {
            List<Long> setMealIds = setMealDishMapper.getSetMealIdsByDishId(id);
            for (Long setMealId : setMealIds)
                setMealService.setStatus(StatusConstant.DISABLE, setMealId);
        }

        Dish dish = Dish.builder()
                .id(id)
                .status(status)
                .build();
        dishMapper.update(dish);
    }

    @Override
    public List<Dish> getByCategoryId(Long categoryId) {
        return dishMapper.getByCategoryId(categoryId);
    }

    @Override
    public List<DishVO> getByCategoryIdWithFlavor(Long categoryId) {
        List<DishVO> dishVOList = new ArrayList<>();

        List<Dish> dishList = dishMapper.getByCategoryId(categoryId);
        for (Dish dish: dishList){
            List<DishFlavor> flavors = dishFlavorMapper.getByDishId(dish.getId());
            DishVO dishVO = DishVO.builder()
                    .flavors(flavors)
                    .build();
            BeanUtils.copyProperties(dish, dishVO);
            dishVOList.add(dishVO);
        }

        return dishVOList;
    }
}
