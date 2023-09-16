package com.hugo.mapper;

import com.hugo.entity.ShoppingCart;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ShoppingCartMapper {
    @Select("select * from shopping_cart where user_id = #{userId}")
    List<ShoppingCart> list(Long userId);

    ShoppingCart getByDishIdOrSetMealId(ShoppingCart shoppingCart);

    @Update("update shopping_cart set number = #{number} where id = #{id}")
    void updateNumberById(ShoppingCart shoppingCart);

    @Insert("insert into shopping_cart (name, image, user_id, dish_id, setmeal_id, dish_flavor, amount, create_time) " +
            "values (#{name}, #{image}, #{userId}, #{dishId}, #{setmealId}, #{dishFlavor}, #{amount}, #{createTime})")
    void insert(ShoppingCart shoppingCart);

    @Delete("delete from shopping_cart where id = #{id}")
    void delete(Long id);

    @Delete("delete from shopping_cart where user_id = #{userId}")
    void clean(Long userId);
}
