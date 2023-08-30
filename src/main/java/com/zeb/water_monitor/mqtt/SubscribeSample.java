package com.zeb.water_monitor.mqtt;

import com.alibaba.fastjson.JSON;
import com.zeb.water_monitor.dto.MqttDataDTO;
import com.zeb.water_monitor.entity.WaterQuality;
import com.zeb.water_monitor.service.Impl.WaterQualityServiceImpl;
import com.zeb.water_monitor.service.WaterQualityService;
import com.zeb.water_monitor.utils.RandomNum;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @author zeb
 * @Date 2023-05-11 22:14
 */
@Slf4j
@Component
public class SubscribeSample {

    private static int num = 0;

    public static void insertData(WaterQualityService waterQualityService) {
        String broker = "tcp://39.106.34.203:1883";
        String topic = "LeCar214923124s";
        String clientid = "waterhouduan" + String.format("%08d", RandomNum.generateRandomNum());
        int qos = 0;

        MqttClient client = null;
        try {
            client = new MqttClient(broker, clientid, new MemoryPersistence());
            // 连接参数
            MqttConnectOptions options = new MqttConnectOptions();
            options.setConnectionTimeout(60);
            options.setKeepAliveInterval(60);
            // 设置回调
            client.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                    log.debug("connectionLost: " + cause.getMessage());
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) {
                    log.debug("topic: " + topic);
                    log.debug("Qos: " + message.getQos());
                    String content = new String(message.getPayload());
                    MqttDataDTO mqttDataDTO = JSON.parseObject(content.substring(content.indexOf("{")).replaceAll("TEMP:", ""), MqttDataDTO.class);

                    WaterQuality waterQuality = new WaterQuality();
                    waterQuality.setPh(mqttDataDTO.getPH());
                    waterQuality.setConductivity(mqttDataDTO.getEC());
                    waterQuality.setTemperature(mqttDataDTO.getTemper());
                    waterQuality.setTurbidity(mqttDataDTO.getTU());
                    waterQuality.setDate(LocalDateTime.now());

                    waterQualityService.save(waterQuality);

                    log.debug("{}", waterQuality);
                    log.debug("已经添加" + num++ + "条数据");

                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    System.out.println("deliveryComplete---------" + token.isComplete());
                }
            });
            client.connect(options);
            client.subscribe(topic, qos);
            System.out.println("接收数据中...");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                client.close();
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        SubscribeSample.insertData(new WaterQualityServiceImpl());
    }
}
