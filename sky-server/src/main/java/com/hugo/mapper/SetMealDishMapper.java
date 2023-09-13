package com.hugo.mapper;

import com.github.pagehelper.Page;
import com.hugo.dto.SetMealPageQueryDTO;
import com.hugo.entity.SetMealDish;
import com.hugo.vo.SetMealVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetMealDishMapper {

    Long countByDishIds(List<Long> ids);

    void insertBatch(List<SetMealDish> setMealDishes);

    @Select("select * from setmeal_dish where setmeal_id = #{setMealId}")
    List<SetMealDish> getBySetMealId(Long setMealId);

    @Delete("delete from setmeal_dish where setmeal_id = #{setMealId}")
    void deleteBySetMealId(Long setMealId);

    void deleteBySetMealIds(List<Long> ids);
}
