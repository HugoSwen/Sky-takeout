package com.hugo.mapper;

import com.github.pagehelper.Page;
import com.hugo.annotation.AutoFill;
import com.hugo.dto.SetMealPageQueryDTO;
import com.hugo.entity.SetMeal;
import com.hugo.enumeration.OperationType;
import com.hugo.vo.SetMealVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetMealMapper {

    @Select("select count(id) from setmeal where category_id = #{categoryId}")
    Integer countByCategoryId(Long id);

    @AutoFill(OperationType.INSERT)
    void insert(SetMeal setMeal);

    Page<SetMealVO> pageQuery(SetMealPageQueryDTO setMealPageQueryDTO);

    @Select("select * from setmeal where id = #{id}")
    SetMeal getById(Long id);

    @AutoFill(OperationType.UPDATE)
    void update(SetMeal setMeal);

    long countEnableByIds(List<Long> ids);

    void deleteByIds(List<Long> ids);
}
