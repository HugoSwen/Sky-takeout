package com.hugo.mapper;

import com.github.pagehelper.Page;
import com.hugo.annotation.AutoFill;
import com.hugo.dto.DishDTO;
import com.hugo.dto.DishPageQueryDTO;
import com.hugo.entity.Dish;
import com.hugo.enumeration.OperationType;
import com.hugo.vo.DishVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface DishMapper {

    @Select("select count(*) from dish where category_id = #{categoryId}")
    Long countByCategoryId(Long categoryId);

    @AutoFill(OperationType.INSERT)
    void insert(Dish dish);

    Page<DishVO> pageQuery(DishPageQueryDTO dishPageQueryDTO);

    Long countEnableByIds(List<Long> ids);

    void deleteBatch(List<Long> ids);

    @Select("select * from dish where id = #{id}")
    Dish getById(Long id);

    @AutoFill(OperationType.UPDATE)
    void update(Dish dish);

    List<Dish> getByCategoryId(Long categoryId);

    Long countByMap(Integer status);
}
