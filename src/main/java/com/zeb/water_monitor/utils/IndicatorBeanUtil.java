package com.zeb.water_monitor.utils;

import com.zeb.water_monitor.entity.WaterQuality;
import com.zeb.water_monitor.enums.IndicatorEnum;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zeb
 * @Date 2024-02-17 20:39
 */
public class IndicatorBeanUtil {

    public static Map<IndicatorEnum, Double> IndicatorToMap(WaterQuality waterQuality) {
        return new HashMap<IndicatorEnum, Double>() {{
            put(IndicatorEnum.TDS, Double.valueOf(waterQuality.getTds()));
            put(IndicatorEnum.TURBIDITY, Double.valueOf(waterQuality.getTurbidity()));
            put(IndicatorEnum.CONDUCTIVITY, Double.valueOf(waterQuality.getConductivity()));
            put(IndicatorEnum.PH, Double.valueOf(waterQuality.getPh()));
        }};
    }
}
