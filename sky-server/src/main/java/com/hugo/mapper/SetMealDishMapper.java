package com.hugo.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SetMealDishMapper {

    Long countByDishIds(List<Long> ids);
}
