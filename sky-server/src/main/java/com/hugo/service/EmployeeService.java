package com.hugo.service;

import com.hugo.dto.EmployeeDTO;
import com.hugo.dto.EmployeeLoginDTO;
import com.hugo.dto.EmployeePageQueryDTO;
import com.hugo.dto.PasswordEditDTO;
import com.hugo.entity.Employee;
import com.hugo.result.PageResult;

public interface EmployeeService {

    Employee login(EmployeeLoginDTO employeeLoginDTO);

    void addEmp(EmployeeDTO emp);

    PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO);

    Employee getById(Long id);

    void update(EmployeeDTO employeeDTO);

    void setStatus(Integer status, Long id);

    void editPassword(PasswordEditDTO passwordEditDTO);
}
