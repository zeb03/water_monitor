package com.zeb.water_monitor.mqtt;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.zeb.water_monitor.constant.WaterStatusConstant;
import com.zeb.water_monitor.dto.MessageDTO;
import com.zeb.water_monitor.dto.MqttDataDTO;
import com.zeb.water_monitor.entity.WaterQuality;
import com.zeb.water_monitor.enums.IndicatorEnum;
import com.zeb.water_monitor.enums.WaterStatusEnum;
import com.zeb.water_monitor.server.SseServer;
import com.zeb.water_monitor.service.StationService;
import com.zeb.water_monitor.service.WaterQualityService;
import com.zeb.water_monitor.utils.DataCalculateUtil;
import com.zeb.water_monitor.utils.IndicatorBeanUtil;
import com.zeb.water_monitor.utils.RandomNum;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zeb
 * @Date 2023-05-11 22:14
 */
@Slf4j
//@Component
public class SubScribeSample implements CommandLineRunner {

    @Autowired
    private WaterQualityService waterQualityService;
    @Autowired
    private StationService stationService;
    @Autowired
    private SseServer server;

    private static int count = 0;
    private static final String CLIENT_ID_KEY = "waterhouduan";
    private static final String BROKER = "tcp://39.106.34.203:1883";
    private static final String TOPIC = "LeCar214923124s";
    private MqttClient client;

    private final Map<String, WaterStatusEnum> statusMap;

    {
        statusMap = new HashMap<>(10);
    }

    /**
     * 接收Mqtt协议数据并保存到数据库
     */
    public void insertData() {
        String clientId = CLIENT_ID_KEY + String.format("%08d", RandomNum.generateRandomNum());
        int qos = 0;
        try {
            log.info("尝试连接");
            client = new MqttClient(BROKER, clientId, new MemoryPersistence());
            // 连接参数
            MqttConnectOptions options = new MqttConnectOptions();
            options.setConnectionTimeout(60);
            options.setKeepAliveInterval(60);

            client.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                    log.info("connectionLost: " + cause.getMessage());
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) {
                    log.info("topic: " + topic);
                    log.info("Qos: " + message.getQos());
                    String content = new String(message.getPayload());

                    MqttDataDTO mqttDataDTO = JSON.parseObject(content.substring(content.indexOf("{")).replaceAll("TEMP:", ""), MqttDataDTO.class);

                    WaterQuality waterQuality = new WaterQuality();
                    waterQuality.setPh(mqttDataDTO.getPH());
                    waterQuality.setConductivity(mqttDataDTO.getEC());
                    waterQuality.setTemperature(mqttDataDTO.getTemper());
                    waterQuality.setTurbidity(mqttDataDTO.getTU());
                    waterQuality.setDate(LocalDateTime.now());
                    //BeanUtils.copyProperties(mqttDataDTO, waterQuality);

                    //每隔两分钟执行一次数据库保存
                    long timestamp = Instant.now().getEpochSecond();
                    if (timestamp % 120 == 0) {
                        waterQualityService.save(waterQuality);
                    }

                    log.info("{}", waterQuality);
                    log.info("已经添加" + count++ + "条数据");

                    //实时发送水质数据
                    String jsonStr = JSONUtil.toJsonStr(waterQuality);
                    server.send("1", jsonStr);

                    //监测数据是否达标，若不达标，则发送数据给前端
                    String station = waterQuality.getStation();
                    String area = waterQuality.getArea();
                    String keyPrefix = WaterStatusConstant.STATUS_PREFIX + station + "-" + area + "-";

                    Map<IndicatorEnum, Double> map = IndicatorBeanUtil.IndicatorToMap(waterQuality);
                    HashMap<IndicatorEnum, WaterStatusEnum> result = DataCalculateUtil.calSubstandardIndicator(map);
                    result.forEach((key, value) -> {
                        statusMap.putIfAbsent(keyPrefix + key, WaterStatusEnum.NORMAL);
                        //如果状态发送变化，则发送消息
                        if (statusMap.get(keyPrefix + key.toString()) != value) {
                            MessageDTO<Object> messageDTO = MessageDTO.builder().message(value.toString()).data(waterQuality).build();
                            server.send("1", JSON.toJSONString(messageDTO));
                        }
                    });
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    log.info("deliveryComplete---------" + token.isComplete());
                }
            });

            client.connect(options);
            client.subscribe(TOPIC, qos);
            log.info("接收数据中...");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (client != null) {
                    client.close();
                }
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 容器启动后调用此方法
     *
     * @param args
     */
    @Override
    public void run(String... args) {
        //开始接收数据
        insertData();
        //启动sse连接
        server.conect("1");
    }
}
