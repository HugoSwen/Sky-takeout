package com.hugo.aspect;

import com.hugo.annotation.AutoFill;
import com.hugo.constant.AutoFillConstant;
import com.hugo.context.BaseContext;
import com.hugo.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

@Component
@Aspect
@Slf4j
public class AutoFillAspect {

    @Pointcut("@annotation(com.hugo.annotation.AutoFill)")
    public void autoFillPointCut() {}

    @Before("autoFillPointCut()")
    public void autoFill(JoinPoint joinPoint) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        log.info("开启公共字段自动填充...");

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        AutoFill autoFill = signature.getMethod().getAnnotation(AutoFill.class);
        OperationType operationType = autoFill.value();
        Object[] args = joinPoint.getArgs();

        if (args == null || args.length == 0)
            return;

        Object entity = args[0];
        log.info("实体对象：{}", entity);

        LocalDateTime now = LocalDateTime.now();
        Long id = BaseContext.getCurrentId();

        // 通过反射获取set方法
        Method setCreateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
        Method setCreateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
        Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
        Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);

        if (operationType == OperationType.INSERT) {
            setCreateTime.invoke(entity, now);
            setCreateUser.invoke(entity, id);
        }

        setUpdateTime.invoke(entity, now);
        setUpdateUser.invoke(entity, id);
    }
}
