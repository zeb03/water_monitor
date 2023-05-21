package com.zeb.water_monitor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
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

//        ApplicationContext context = SpringUtil.getApplicationContext();
//        WaterQualityService waterQualityService = context.getBean(WaterQualityService.class);
//        SubscribeSample.insertData(waterQualityService);
    }


}
