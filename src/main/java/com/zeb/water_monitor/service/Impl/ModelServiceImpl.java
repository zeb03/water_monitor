package com.zeb.water_monitor.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zeb.water_monitor.common.CustomException;
import com.zeb.water_monitor.entity.Model;
import com.zeb.water_monitor.mapper.ModelMapper;
import com.zeb.water_monitor.service.ModelService;
import com.zeb.water_monitor.service.WaterQualityService;
import com.zeb.water_monitor.vo.Prediction;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author zeb
 * @since 2023-05-09
 */
@Service
@Slf4j
public class ModelServiceImpl extends ServiceImpl<ModelMapper, Model> implements ModelService {

    @Autowired
    private WaterQualityService waterQualityService;

    @Override
    public Prediction predictNextMonth(int modelId, String indicator) {
        return sendPredictionRequest(modelId, indicator);
    }

    private Prediction sendPredictionRequest(int modelId, String indicator) {
        Prediction prediction = waterQualityService.getDateForPrediction(indicator);

        List<Float> forPlot = prediction.getForPlot();
        List<Float> forPrediction = prediction.getForPrediction();
        List<String> dates = prediction.getDateStrs();

        //转换日期格式
        String startDate = dates.get(2);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = null;
        try {
            date = dateFormat.parse(startDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        assert date != null;
        calendar.setTime(date);
        int startMonth = calendar.get(Calendar.MONTH) + 1;

        //定义url
        String url = "http://localhost:8000/api/prediction?id=" + modelId;
        url = url + "&one=" + forPrediction.get(0) + "&two=" +
                forPrediction.get(1) + "&three=" + forPrediction.get(2) + "&startMonth=" + startMonth;
        log.info("url: " + url);

        //发送请求
        String jsonResp = sendRequest(url);

        //转换响应参数
        Double response = null;
        try {
            response = parsePredictionResponse(jsonResp);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new CustomException("服务器异常");
        }

        forPlot.add(response.floatValue());
        calendar.add(Calendar.MONTH, 3);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        dates.add(df.format(calendar.getTime()));

        Prediction resp = new Prediction();
        resp.setPredValue(response);
        resp.setForPlot(forPlot);
        resp.setDateStrs(dates);

        return resp;
    }

    private String sendRequest(String url) {
        HttpClient httpclient = HttpClients.createDefault();
        HttpGet httpget = new HttpGet(url);
        String resp = null;
        try {
            HttpResponse response = httpclient.execute(httpget);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                resp = EntityUtils.toString(entity, "UTF-8").trim();

            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            httpget.abort();
        }
        return resp;
    }

    private Double parsePredictionResponse(String response) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode rootNode = objectMapper.readTree(response);
        JsonNode dataNode = rootNode.path("data");
        Double prediction = dataNode.path("pred").asDouble();

        return prediction;
    }
}
