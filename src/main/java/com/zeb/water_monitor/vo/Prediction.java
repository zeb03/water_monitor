package com.zeb.water_monitor.vo;

import lombok.Data;

import java.util.List;

/**
 * @author zeb
 * @Date 2023-05-09-21:24
 */
@Data
public class Prediction {
    private List<String> dateStrs;
    private List<Float> forPlot;
    private List<Float> forPrediction;
    private Double predValue;
}
