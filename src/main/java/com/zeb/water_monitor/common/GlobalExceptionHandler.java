package com.zeb.water_monitor.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * @author zeb
 * @since 2023-05-06
 * 全局异常处理
 */
@ControllerAdvice(annotations = {RestController.class, Controller.class})
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public Result<String> exceptionHandler(SQLIntegrityConstraintViolationException ex) {
        if (ex.getMessage().contains("Duplicate entry")) {
            String[] split = ex.getMessage().split(" ");
            String msg = split[2] + "已经存在";
            return Result.error(msg);
        } else if (ex.getMessage().contains("cannot be null")) {
            String[] split = ex.getMessage().split(" ");
            String msg = split[1] + "为空";
            return Result.error(msg);
        }
        return Result.error("未知错误");
    }

    @ExceptionHandler(CustomException.class)
    public Result<String> customException(CustomException customException) {
        return Result.error(customException.getMessage());
    }

}
