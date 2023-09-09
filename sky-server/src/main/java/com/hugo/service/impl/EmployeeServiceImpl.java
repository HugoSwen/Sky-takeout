package com.hugo.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hugo.annotation.AutoFill;
import com.hugo.constant.MessageConstant;
import com.hugo.constant.StatusConstant;
import com.hugo.context.BaseContext;
import com.hugo.dto.EmployeeDTO;
import com.hugo.dto.EmployeeLoginDTO;
import com.hugo.dto.EmployeePageQueryDTO;
import com.hugo.dto.PasswordEditDTO;
import com.hugo.entity.Employee;
import com.hugo.enumeration.OperationType;
import com.hugo.exception.AccountLockedException;
import com.hugo.exception.AccountNotFoundException;
import com.hugo.exception.PasswordErrorException;
import com.hugo.mapper.EmployeeMapper;
import com.hugo.result.PageResult;
import com.hugo.service.EmployeeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();

        //1、根据用户名查询数据库中的数据
        Employee employee = employeeMapper.getByUsername(username);

        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (employee == null)
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);

        //密码比对
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!password.equals(employee.getPassword()))
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);

        if (Objects.equals(employee.getStatus(), StatusConstant.DISABLE))
            //账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);

        //3、返回实体对象
        return employee;
    }

    @Override
    public void addEmp(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO, employee);
        employeeMapper.insert(employee);
    }

    public PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO) {
        PageHelper.startPage(employeePageQueryDTO.getPage(), employeePageQueryDTO.getPageSize());

        Page<Employee> page = employeeMapper.pageQuery(employeePageQueryDTO);
        long total = page.getTotal();
        List<Employee> records = page.getResult();
        return new PageResult(total, records);
    }

    @Override
    public Employee getById(Long id) {
        return employeeMapper.getById(id);
    }

    @Override
    public void setStatus(Integer status, Long id) {
        Employee employee = Employee.builder()
                .status(status)
                .id(id)
                .build();
        employeeMapper.update(employee);
    }

    @Override
    public void editPassword(PasswordEditDTO passwordEditDTO) {
        Employee employee = employeeMapper.getById(passwordEditDTO.getEmpId());

        String password = employee.getPassword();
        String oldPassword = DigestUtils.md5DigestAsHex(passwordEditDTO.getOldPassword().getBytes());
        String newPassword = DigestUtils.md5DigestAsHex(passwordEditDTO.getNewPassword().getBytes());

        Employee emp = Employee.builder()
                .id(employee.getId())
                .password(newPassword)
                .build();

        if (password.equals(oldPassword))
            employeeMapper.update(emp);
        else
            throw new PasswordErrorException(MessageConstant.PASSWORD_EDIT_FAILED);

    }

    @Override
    public void update(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO, employee);
        employeeMapper.update(employee);
    }

}
