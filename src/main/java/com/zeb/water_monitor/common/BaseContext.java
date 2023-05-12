package com.zeb.water_monitor.common;

/**
 * @author zeb
 * @since 2023-05-06
 * 基于ThreadLocal封装工具类，用于保存和获取用户id
 */
public class BaseContext {
    private static final ThreadLocal<Integer> THREAD_LOCAL = new ThreadLocal<>();

    private BaseContext() {
    }

    public static void setCurrentId(Integer id) {
        THREAD_LOCAL.set(id);
    }

    public static Integer getCurrentId() {
        return THREAD_LOCAL.get();
    }

    public static void removeThreadLocal(){
        THREAD_LOCAL.remove();
    }
}
