package com.hugo.service;

import com.hugo.dto.EmployeeLoginDTO;
import com.hugo.entity.Employee;

public interface EmployeeService {

    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

}
