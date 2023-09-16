package com.hugo.service.impl;

import com.hugo.context.BaseContext;
import com.hugo.dto.ShoppingCartDTO;
import com.hugo.entity.Dish;
import com.hugo.entity.SetMeal;
import com.hugo.entity.ShoppingCart;
import com.hugo.mapper.DishMapper;
import com.hugo.mapper.SetMealMapper;
import com.hugo.mapper.ShoppingCartMapper;
import com.hugo.service.ShoppingCartService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private SetMealMapper setMealMapper;

    @Override
    public void addShoppingCart(ShoppingCartDTO shoppingCartDTO) {
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
        shoppingCart.setUserId(BaseContext.getCurrentId());

        // 查找购物车中是否存在该菜品或者套餐
        ShoppingCart cart = shoppingCartMapper.getByDishIdOrSetMealId(shoppingCart);
        // 存在更新数量
        if (cart != null) {
            cart.setNumber(cart.getNumber() + 1);
            shoppingCartMapper.updateNumberById(cart);
        } else { // 不存在插入新数据
            if (shoppingCart.getDishId() != null) {
                Long dishId = shoppingCart.getDishId();
                Dish dish = dishMapper.getById(dishId);
                shoppingCart.setName(dish.getName());
                shoppingCart.setImage(dish.getImage());
                shoppingCart.setAmount(dish.getPrice());
            } else {
                Long setmealId = shoppingCart.getSetmealId();
                SetMeal setMeal = setMealMapper.getById(setmealId);
                shoppingCart.setName(setMeal.getName());
                shoppingCart.setImage(setMeal.getImage());
                shoppingCart.setAmount(setMeal.getPrice());
            }
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartMapper.insert(shoppingCart);
        }

    }

    @Override
    public List<ShoppingCart> list() {
        return shoppingCartMapper.list(BaseContext.getCurrentId());
    }

    @Override
    public void delete(ShoppingCartDTO shoppingCartDTO) {
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
        shoppingCart.setUserId(BaseContext.getCurrentId());

        // 查询商品的数量
        ShoppingCart cart = shoppingCartMapper.getByDishIdOrSetMealId(shoppingCart);
        // 若是等于1 删除商品
        if (cart.getNumber() == 1)
            shoppingCartMapper.delete(cart.getId());
        else { // 若是大于1 number - 1
            cart.setNumber(cart.getNumber() - 1);
            shoppingCartMapper.updateNumberById(cart);
        }
    }

    @Override
    public void clean() {
        shoppingCartMapper.clean(BaseContext.getCurrentId());
    }
}
