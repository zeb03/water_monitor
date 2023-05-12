package com.zeb.water_monitor.common;

/**
 * 自定义业务异常类
 * @author zeb
 * @since 2023-05-06
 */
public class CustomException extends RuntimeException {
    public CustomException(String message){
        super(message);
    }
}
