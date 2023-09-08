package com.hugo.controller.admin;

import com.hugo.constant.JwtClaimsConstant;
import com.hugo.dto.EmployeeDTO;
import com.hugo.dto.EmployeeLoginDTO;
import com.hugo.dto.EmployeePageQueryDTO;
import com.hugo.dto.PasswordEditDTO;
import com.hugo.entity.Employee;
import com.hugo.properties.JwtProperties;
import com.hugo.result.PageResult;
import com.hugo.result.Result;
import com.hugo.service.EmployeeService;
import com.hugo.utils.JwtUtil;
import com.hugo.vo.EmployeeLoginVO;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 员工管理
 */
@Slf4j
@RestController
@RequestMapping("/admin/employee")
@Api(tags = "员工相关接口")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 登录
     */
    @PostMapping("/login")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("员工登录：{}", employeeLoginDTO);

        Employee employee = employeeService.login(employeeLoginDTO);

        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        claims.put(JwtClaimsConstant.USERNAME, employee.getUsername());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();

        return Result.success(employeeLoginVO);
    }

    /**
     * 退出
     */
    @PostMapping("/logout")
    public Result<String> logout() {
        return Result.success();
    }

    /**
     * 新增员工
     */
    @PostMapping
    public Result<String> addEmp(@RequestBody EmployeeDTO emp){
        log.info("新增员工：{}", emp);
        employeeService.addEmp(emp);
        return Result.success();
    }

    /**
     * 员工分页查询
     */
    @GetMapping("/page")
    public Result<PageResult> pageQuery(EmployeePageQueryDTO employeePageQueryDTO){
        log.info("员工分页查询：{}", employeePageQueryDTO);

        PageResult pageResult = employeeService.pageQuery(employeePageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 禁用启用员工账号
     */
    @PostMapping("/status/{status}")
    public Result enableOrDisable(@PathVariable Integer status, Long id){
        log.info("启禁用员工账号：{},{}", status, id);

        employeeService.setStatus(status, id);
        return Result.success();
    }

    /**
     * 根据id获取员工信息
     */
    @GetMapping("/{id}")
    public Result<Employee> getById(@PathVariable Long id){
        log.info("查询员工id：{}", id);

        Employee employee = employeeService.getById(id);
        return Result.success(employee);
    }

    /**
     * 修改员工密码
     */
    @PutMapping("/editPassword")
    public Result editPassword(@RequestBody PasswordEditDTO passwordEditDTO){
        log.info("修改员工密码：{}", passwordEditDTO);

        employeeService.editPassword(passwordEditDTO);
        return Result.success();
    }

    /**
     * 修改员工信息
     */
    @PutMapping
    public Result update(@RequestBody EmployeeDTO employeeDTO){
        log.info("修改员工信息：{}", employeeDTO);

        employeeService.update(employeeDTO);
        return Result.success();
    }

}
