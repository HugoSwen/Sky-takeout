package com.hugo.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hugo.constant.MessageConstant;
import com.hugo.dto.SetMealDTO;
import com.hugo.dto.SetMealPageQueryDTO;
import com.hugo.entity.SetMeal;
import com.hugo.entity.SetMealDish;
import com.hugo.exception.DeletionNotAllowedException;
import com.hugo.mapper.SetMealDishMapper;
import com.hugo.mapper.SetMealMapper;
import com.hugo.result.PageResult;
import com.hugo.service.SetMealService;
import com.hugo.vo.SetMealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class SetMealServiceImpl implements SetMealService {

    @Autowired
    private SetMealMapper setMealMapper;

    @Autowired
    private SetMealDishMapper setMealDishMapper;

    @Transactional
    @Override
    public void addSetMeal(SetMealDTO setmealDTO) {
        SetMeal setMeal = new SetMeal();
        BeanUtils.copyProperties(setmealDTO, setMeal);
        setMealMapper.insert(setMeal);

        Long id = setMeal.getId();

        List<SetMealDish> setMealDishes = setmealDTO.getSetmealDishes();
        if (setMealDishes != null && !setMealDishes.isEmpty()){
            setMealDishes.forEach(setMealDish -> setMealDish.setSetMealId(id));
            setMealDishMapper.insertBatch(setMealDishes);
        }
    }

    @Override
    public PageResult pageQuery(SetMealPageQueryDTO setMealPageQueryDTO) {
        PageHelper.startPage(setMealPageQueryDTO.getPage(), setMealPageQueryDTO.getPageSize());

        Page<SetMealVO> page = setMealMapper.pageQuery(setMealPageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public SetMealVO getByIdWithDishes(Long id) {
        SetMeal setMeal = setMealMapper.getById(id);
        List<SetMealDish> setMealDishes = setMealDishMapper.getBySetMealId(id);

        SetMealVO setMealVO = new SetMealVO();
        BeanUtils.copyProperties(setMeal, setMealVO);
        setMealVO.setSetmealDishes(setMealDishes);

        return setMealVO;
    }

    @Override
    public void updateWithDishes(SetMealDTO setMealDTO) {
        SetMeal setMeal = new SetMeal();
        BeanUtils.copyProperties(setMealDTO, setMeal);
        setMealMapper.update(setMeal);

        setMealDishMapper.deleteBySetMealId(setMeal.getId());
        List<SetMealDish> setMealDishes = setMealDTO.getSetmealDishes();
        if (setMealDishes != null && !setMealDishes.isEmpty()){
            setMealDishes.forEach(setMealDish -> setMealDish.setSetMealId(setMeal.getId()));
            setMealDishMapper.insertBatch(setMealDishes);
        }
    }

    @Override
    public void setStatus(Integer status, Long id) {
        SetMeal setMeal = SetMeal.builder()
                .id(id)
                .status(status)
                .build();

        setMealMapper.update(setMeal);
    }

    @Override
    public void deleteBatch(List<Long> ids) {
        // 判断套餐是否起售
        long enableCnt = setMealMapper.countEnableByIds(ids);
        if (enableCnt > 0)
            throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
        // 删除套餐
        setMealMapper.deleteByIds(ids);
        // 删除套餐对应菜品信息
        setMealDishMapper.deleteBySetMealIds(ids);
    }


}
