package com.hugo.mapper;

import com.github.pagehelper.Page;
import com.hugo.annotation.AutoFill;
import com.hugo.dto.EmployeeDTO;
import com.hugo.dto.EmployeePageQueryDTO;
import com.hugo.entity.Employee;
import com.hugo.enumeration.OperationType;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface EmployeeMapper {

    @Select("select * from employee where username = #{username}")
    Employee getByUsername(String username);

    @AutoFill(value = OperationType.INSERT)
    @Insert("insert into employee (name, username, phone, sex, id_number, create_time, update_time, create_user, update_user)" +
            "values (#{name},#{username},#{phone},#{sex},#{idNumber},#{createTime},#{updateTime},#{createUser},#{updateUser})")
    void insert(Employee emp);


    @Select("select * from employee where id = #{id}")
    Employee getById(Long id);

    Page<Employee> pageQuery(EmployeePageQueryDTO employeePageQueryDTO);

    void update(Employee employee);
}
