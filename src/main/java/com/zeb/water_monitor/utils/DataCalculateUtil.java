package com.zeb.water_monitor.utils;

import com.zeb.water_monitor.entity.WaterQuality;
import com.zeb.water_monitor.enums.IndicatorEnum;
import com.zeb.water_monitor.enums.WaterStatusEnum;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zeb
 * @Date 2024-02-16 21:21
 */
@Slf4j
public class DataCalculateUtil {
    /**
     * 计算指标平均数
     *
     * @param indicator
     * @param waterQualities
     * @return
     */
    public static Double calAverage(IndicatorEnum indicator, List<WaterQuality> waterQualities) {
        return calAllAverage(waterQualities).get(indicator);
    }

    /**
     * 计算所有指标平均数
     *
     * @param waterQualities
     * @return
     */
    public static Map<IndicatorEnum, Double> calAllAverage(List<WaterQuality> waterQualities) {
        log.info("waterQualities: " + waterQualities);
        Map<IndicatorEnum, Double> resultMap = new HashMap<>();
        resultMap.put(IndicatorEnum.PH, waterQualities.stream().mapToDouble(WaterQuality::getPh).average().orElse(0));
        resultMap.put(IndicatorEnum.TEMPERATURE, waterQualities.stream().mapToDouble(WaterQuality::getTemperature).average().orElse(0));
        resultMap.put(IndicatorEnum.CONDUCTIVITY, waterQualities.stream().mapToDouble(WaterQuality::getConductivity).average().orElse(0));
        resultMap.put(IndicatorEnum.TURBIDITY, waterQualities.stream().mapToDouble(WaterQuality::getTurbidity).average().orElse(0));
        resultMap.put(IndicatorEnum.TDS, waterQualities.stream().mapToDouble(WaterQuality::getTds).average().orElse(0));
        return resultMap;
    }

    public static HashMap<IndicatorEnum, WaterStatusEnum> calSubstandardIndicator(Map<IndicatorEnum, Double> map) {
        HashMap<IndicatorEnum, WaterStatusEnum> result = new HashMap<>();
        map.forEach((key, value) -> {
            if (key == IndicatorEnum.PH) {
                result.put(key, value > 6 && value < 9 ? WaterStatusEnum.NORMAL : WaterStatusEnum.SUBSTANDARD);
            }
            if (key == IndicatorEnum.TDS) {
                result.put(key, value > 100 && value < 1000 ? WaterStatusEnum.NORMAL : WaterStatusEnum.SUBSTANDARD);
            }
            if (key == IndicatorEnum.CONDUCTIVITY) {
                result.put(key, value > 0.05 && value < 3 ? WaterStatusEnum.NORMAL : WaterStatusEnum.SUBSTANDARD);
            }
            if (key == IndicatorEnum.TURBIDITY) {
                result.put(key, value > 1 && value < 50 ? WaterStatusEnum.NORMAL : WaterStatusEnum.SUBSTANDARD);
            }
        });
        return result;
    }
}
