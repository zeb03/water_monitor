package com.zeb.water_monitor.mqtt;

import com.alibaba.fastjson.JSON;
import com.zeb.water_monitor.dto.MqttDataDTO;
import com.zeb.water_monitor.entity.WaterQuality;
import com.zeb.water_monitor.service.WaterQualityService;
import com.zeb.water_monitor.utils.RandomNum;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author zeb
 * @Date 2023-05-11 22:14
 */
@Slf4j
@Component
public class SubScribeSample {

    private static int count = 0;
    private static final String CLIENT_ID_KEY = "waterhouduan";
    private static final String BROKER = "tcp://39.106.34.203:1883";
    private static final String TOPIC = "LeCar214923124s";

    public static void insertData(WaterQualityService waterQualityService) {
        String clientId = CLIENT_ID_KEY + String.format("%08d", RandomNum.generateRandomNum());
        int qos = 0;

        MqttClient client = null;
        try {
            log.info("尝试连接");
            client = new MqttClient(BROKER, clientId, new MemoryPersistence());
            // 连接参数
            MqttConnectOptions options = new MqttConnectOptions();
            options.setConnectionTimeout(60);
            options.setKeepAliveInterval(60);

            // 设置回调
            ScheduledExecutorService pool = Executors.newScheduledThreadPool(1);
            MqttClient task = client;

            //每间隔2分钟执行
            pool.scheduleWithFixedDelay(() -> task.setCallback(new MqttCallback() {
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

                    waterQualityService.save(waterQuality);

                    log.info("{}", waterQuality);
                    log.info("已经添加" + count++ + "条数据");
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    log.info("deliveryComplete---------" + token.isComplete());
                }
            }), 0, 2, TimeUnit.MINUTES);

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

}
