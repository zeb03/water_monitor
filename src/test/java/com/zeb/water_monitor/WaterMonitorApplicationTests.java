package com.zeb.water_monitor;

import com.alibaba.fastjson.JSON;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;

@SpringBootTest
class WaterMonitorApplicationTests {

    @Test
    void contextLoads() {
        String content = "{ \"msg\": \"hello\" }";
        System.out.println(content);
        HashMap al = JSON.parseObject(content,HashMap.class);
        Object user = JSON.parseObject(content,Object.class);
        System.out.println(al);
        System.out.println(user);
    }

}
