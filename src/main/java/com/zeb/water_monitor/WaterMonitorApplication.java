package com.zeb.water_monitor;

import com.zeb.water_monitor.mqtt.SubscribeSample;
import com.zeb.water_monitor.service.WaterQualityService;
import com.zeb.water_monitor.utils.SpringUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author zeb
 * @since 2023-05-06
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.zeb")
public class WaterMonitorApplication {

    public static void main(String[] args) {
        SpringApplication.run(WaterMonitorApplication.class, args);

        //实时获取水质数据
        ApplicationContext context = SpringUtil.getApplicationContext();
        WaterQualityService waterQualityService = context.getBean(WaterQualityService.class);
        SubscribeSample.insertData(waterQualityService);
    }


}
