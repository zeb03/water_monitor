package com.zeb.water_monitor.mqtt;

import com.alibaba.fastjson.JSON;
import com.zeb.water_monitor.dto.MqttDataDTO;
import com.zeb.water_monitor.entity.WaterQuality;
import com.zeb.water_monitor.service.WaterQualityService;
import com.zeb.water_monitor.utils.RandomNum;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.time.LocalDateTime;

/**
 * @author zeb
 * @Date 2023-05-11 22:14
 */
public class SubscribeSample {

    private static int num = 0;

    public static void insertData(WaterQualityService waterQualityService){
        String broker = "tcp://39.106.34.203:1883";
        String topic = "LeCar214923124s";
        String username = "admin";
        String password = "public";
        String clientid = "waterhouduan" + String.format("%08d", RandomNum.generateRandomNum());
        int qos = 0;

        try {
            MqttClient client = new MqttClient(broker, clientid, new MemoryPersistence());
            // 连接参数
            MqttConnectOptions options = new MqttConnectOptions();
            options.setUserName(username);
            options.setPassword(password.toCharArray());
            options.setConnectionTimeout(60);
            options.setKeepAliveInterval(60);
            // 设置回调
            client.setCallback(new MqttCallback() {

                @Override
                public void connectionLost(Throwable cause) {
                    System.out.println("connectionLost: " + cause.getMessage());
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) {
                    System.out.println("topic: " + topic);
                    System.out.println("Qos: " + message.getQos());
                    String content = new String(message.getPayload());
                    MqttDataDTO mqttDataDTO = JSON.parseObject(content.substring(content.indexOf("{")).replaceAll("TEMP:", ""), MqttDataDTO.class);

                    WaterQuality waterQuality = new WaterQuality();
                    waterQuality.setPh(mqttDataDTO.getPH());
                    waterQuality.setConductivity(mqttDataDTO.getEC());
                    waterQuality.setTemperature(mqttDataDTO.getTemper());
                    waterQuality.setTurbidity(mqttDataDTO.getTU());
                    waterQuality.setDate(LocalDateTime.now());

                    waterQualityService.save(waterQuality);

                    System.out.println("已经添加" + num++ + "条数据");

                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    System.out.println("deliveryComplete---------" + token.isComplete());
                }

            });
            client.connect(options);
            client.subscribe(topic, qos);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
