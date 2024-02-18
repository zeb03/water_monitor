package com.zeb.water_monitor.config;


import com.zeb.water_monitor.common.JacksonObjectMapper;
import com.zeb.water_monitor.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.List;

/**
 * @author zeb
 * Date: 2023-04-28
 */
@Configuration
//@EnableSwagger2
public abstract class MyWebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        LoginInterceptor loginInterceptor = new LoginInterceptor();
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/**",
                        "/excel/export/water-data",
                        "/excel/export/all/water-data",
                        "/user/login",
                        "/user/register",
                        "/water/quality/query",
                        "/water/quality/all",
                        "/water/quality/station",
                        "/water/quality/recent",
                        "/csrf",
                        "/img/**",
                        "/error",
                        "/common/**",
                        "/doc.html",
                        "/webjars/**",
                        "/swagger-resources/**",
                        "/swagger-ui.html/**",
                        "/favicon.ico",
                        "/v2/api-docs");
    }

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
        messageConverter.setObjectMapper(new JacksonObjectMapper());
        converters.add(0, messageConverter);
    }


    @Bean
    public abstract Docket createRestApi();
}
