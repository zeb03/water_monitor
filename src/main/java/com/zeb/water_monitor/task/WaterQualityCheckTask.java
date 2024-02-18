package com.zeb.water_monitor.task;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zeb.water_monitor.entity.WaterQuality;
import com.zeb.water_monitor.enums.IndicatorEnum;
import com.zeb.water_monitor.service.WaterQualityService;
import com.zeb.water_monitor.utils.DataCalculateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author zeb
 * @Date 2024-02-16 20:37
 */
@Service
@Slf4j
public class WaterQualityCheckTask {
    private final WaterQualityService waterQualityService;

    public WaterQualityCheckTask(WaterQualityService waterQualityService) {
        this.waterQualityService = waterQualityService;
    }

    @Scheduled(fixedRate = 600000) // 每十分钟执行一次
    public void checkWaterQuality() {
        //TODO：获取每只监测船的最新监测数据
        LambdaQueryWrapper<WaterQuality> wrapper = Wrappers.lambdaQuery(WaterQuality.class).orderByDesc(WaterQuality::getDate).last("limit 5");
        List<WaterQuality> waterQualityList = waterQualityService.list(wrapper);
        //计算最近五条数据的平均值
        Map<IndicatorEnum, Double> allAverage = DataCalculateUtil.calAllAverage(waterQualityList);
        //判断是否达标
    }

}
