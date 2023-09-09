package com.hugo.mapper;

import com.github.pagehelper.Page;
import com.hugo.annotation.AutoFill;
import com.hugo.dto.CategoryPageQueryDTO;
import com.hugo.entity.Category;
import com.hugo.enumeration.OperationType;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CategoryMapper {

    @AutoFill(value = OperationType.INSERT)
    @Insert("insert into category (type, name, sort, create_time, update_time, create_user, update_user)" +
            "value (#{type},#{name},#{sort},#{createTime},#{updateTime},#{createUser},#{updateUser})")
    void insert(Category category);

    Page<Category> pageQuery(CategoryPageQueryDTO categoryPageQueryDTO);

    @AutoFill(value = OperationType.UPDATE)
    void update(Category category);

    List<Category> getByType(Integer type);

    @Delete("delete from category where id = #{id}")
    void deleteById(Integer id);
}
