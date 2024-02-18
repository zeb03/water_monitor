package com.zeb.water_monitor;

import cn.hutool.json.JSONUtil;
import com.zeb.water_monitor.constant.WaterStatusConstant;
import com.zeb.water_monitor.dto.MessageDTO;
import com.zeb.water_monitor.entity.WaterQuality;
import com.zeb.water_monitor.enums.IndicatorEnum;
import com.zeb.water_monitor.enums.WaterStatusEnum;
import com.zeb.water_monitor.server.SseServer;
import com.zeb.water_monitor.service.StationService;
import com.zeb.water_monitor.service.WaterQualityService;
import com.zeb.water_monitor.utils.DataCalculateUtil;
import com.zeb.water_monitor.utils.IndicatorBeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@SpringBootTest
class WaterMonitorApplicationTests {

    @Autowired
    private WaterQualityService waterQualityService;
    @Autowired
    private StationService stationService;
    @Autowired
    private SseServer server;

    private final Map<String, WaterStatusEnum> statusMap;

    {
        statusMap = new HashMap<>(10);
    }

    @Test
    void contextLoads() {

        WaterQuality waterQuality = waterQualityService.getById(1857);
        log.info("waterQuality: " + waterQuality);
        //实时发送水质数据
        String jsonStr = JSONUtil.toJsonStr(waterQuality);
//        server.send("1", jsonStr);

        //监测数据是否达标，若不达标，则发送数据给前端
        String station = waterQuality.getStation();
        String area = waterQuality.getArea();
        String keyPrefix = WaterStatusConstant.STATUS_PREFIX + station + "-" + area + "-";

        Map<IndicatorEnum, Double> map = IndicatorBeanUtil.IndicatorToMap(waterQuality);
        HashMap<IndicatorEnum, WaterStatusEnum> result = DataCalculateUtil.calSubstandardIndicator(map);
        log.info("result: " + result);
        result.forEach((key, value) -> {
            statusMap.putIfAbsent(keyPrefix + key, WaterStatusEnum.NORMAL);
            //如果状态发送变化，则发送消息
            if (statusMap.get(keyPrefix + key.toString()) != value) {
                MessageDTO<Object> messageDTO = MessageDTO.builder().message(key + "-" + value.toString()).data(waterQuality).build();
                log.info("message:" + messageDTO);
                System.out.println(messageDTO);
//                server.send("1", JSON.toJSONString(messageDTO));
            }
        });
    }

}
