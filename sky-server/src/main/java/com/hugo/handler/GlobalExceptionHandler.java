package com.hugo.handler;

import com.hugo.constant.MessageConstant;
import com.hugo.exception.BaseException;
import com.hugo.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常处理器，处理项目中抛出的业务异常
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 捕获业务异常
     */
    @ExceptionHandler
    public Result exceptionHandler(BaseException ex){
        log.error("异常信息：{}", ex.getMessage());
        return Result.error(ex.getMessage());
    }

    /**
     * 捕获sql字段重复异常
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public Result SQLIntegrityConstraintViolationHandler(SQLIntegrityConstraintViolationException ex){
        // Duplicate entry 'admin' for key 'employee.idx_username'
        String message = ex.getMessage();
        if (message.contains("Duplicate entry")){
            log.error("字段重复：{}", ex.getMessage());
            String[] s = message.split(" ");
            String username = s[2];
            String msg = username + MessageConstant.ALREADY_EXIST;
            return Result.error(msg);
        } else
            return Result.error(MessageConstant.UNKNOWN_ERROR);
    }
}
