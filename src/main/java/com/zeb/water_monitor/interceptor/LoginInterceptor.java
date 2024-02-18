package com.zeb.water_monitor.interceptor;


import com.alibaba.fastjson.JSON;
import com.zeb.water_monitor.common.BaseContext;
import com.zeb.water_monitor.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Date: 2023-01-28-20:35
 * @author ze
 */
@Slf4j
//@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 打印请求信息
        log.info("------------- LoginInterceptor 开始 -------------");
        long startTime = System.currentTimeMillis();
        request.setAttribute("requestStartTime", startTime);

        // OPTIONS请求不做校验,
        // 前后端分离的架构, 前端会发一个OPTIONS请求先做预检, 对预检请求不做校验
        if ("OPTIONS".equals(request.getMethod().toUpperCase())) {
            return true;
        }

        String path = request.getRequestURL().toString();
        log.info("接口登录拦截：，path：{}", path);

        Integer user = (Integer) request.getSession().getServletContext().getAttribute("user");
        if (user != null) {
            log.info("用户线程id: {}", Thread.currentThread().getId());
            BaseContext.setCurrentId(user);
            return true;
        }
        log.info("用户未登录");
//        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.getWriter().write(JSON.toJSONString(Result.error("not login")));
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        long startTime = (Long) request.getAttribute("requestStartTime");
        log.info("------------- LoginInterceptor 结束 耗时：{} ms -------------", System.currentTimeMillis() - startTime);
    }
}
