package com.hugo.mapper;

import com.github.pagehelper.Page;
import com.hugo.annotation.AutoFill;
import com.hugo.dto.DishPageQueryDTO;
import com.hugo.entity.Dish;
import com.hugo.enumeration.OperationType;
import com.hugo.vo.DishVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface DishMapper {

    @Select("select count(*) from dish where category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);

    @AutoFill(OperationType.INSERT)
    void insert(Dish dish);

    Page<DishVO> pageQuery(DishPageQueryDTO dishPageQueryDTO);
}
