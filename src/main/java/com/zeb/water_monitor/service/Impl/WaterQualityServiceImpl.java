package com.zeb.water_monitor.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zeb.water_monitor.common.CustomException;
import com.zeb.water_monitor.entity.WaterQuality;
import com.zeb.water_monitor.enums.IndicatorEnum;
import com.zeb.water_monitor.mapper.WaterQualityMapper;
import com.zeb.water_monitor.service.WaterQualityService;
import com.zeb.water_monitor.vo.PlotVO;
import com.zeb.water_monitor.vo.Prediction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.zeb.water_monitor.utils.DataCalculateUtil.calAverage;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author zeb
 * @since 2023-05-06
 */
@Slf4j
@Service
public class WaterQualityServiceImpl extends ServiceImpl<WaterQualityMapper, WaterQuality> implements WaterQualityService {

    @Override
    public PlotVO getDataForPlot(String area, Integer period, String indicator) {

        //以月为单位，计算时间
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start = now.minusMonths(period);

        log.info("startTime:" + start);
        log.info("endTime:" + now);

        LambdaQueryWrapper<WaterQuality> wrapper = new LambdaQueryWrapper<>();
        wrapper.ge(WaterQuality::getDate, start);
        wrapper.le(WaterQuality::getDate, now);
        wrapper.orderByAsc(WaterQuality::getDate);
        wrapper.eq(area != null, WaterQuality::getArea, area);

        List<WaterQuality> list = this.list(wrapper);
        List<Float> specWaterQualities = new ArrayList<>();
        List<String> dates = new ArrayList<>();

        list.stream().map(item -> {
            String dateStr = item.getDate().getYear() + "-" + item.getDate().getMonthValue() + "-" + item.getDate().getDayOfMonth();
            dates.add(dateStr);
            //可以使用策略模式优化if语句
            if ("ph".equalsIgnoreCase(indicator)) {
                specWaterQualities.add(item.getPh());
            } else if ("temperature".equalsIgnoreCase(indicator)) {
                specWaterQualities.add(item.getTemperature());
            } else if ("conductivity".equalsIgnoreCase(indicator)) {
                specWaterQualities.add(item.getConductivity());
            } else if ("turbidity".equalsIgnoreCase(indicator)) {
                specWaterQualities.add(item.getTurbidity());
            } else if ("tds".equalsIgnoreCase(indicator)) {
                specWaterQualities.add(item.getTds());
            } else {
                throw new CustomException("参数传输错误，请联系管理员");
            }
            return item;
        }).collect(Collectors.toList());

        PlotVO plotVO = new PlotVO();
        plotVO.setDates(dates);
        plotVO.setSpecWaterQualities(specWaterQualities);

        return plotVO;
    }

    @Override
    public Prediction getDateForPrediction(IndicatorEnum indicator) {

        QueryWrapper<WaterQuality> wrapper = new QueryWrapper<>();

        //获取最近五天数据
        wrapper.select("distinct date").lambda().orderByDesc(WaterQuality::getDate).last("limit 5");

        List<WaterQuality> list = this.list(wrapper);

        List<String> dateStrs = new ArrayList<>();
        List<Float> forPlot = new ArrayList<>();
        List<Float> forPrediction = new ArrayList<>();

        for (WaterQuality waterQuality : list) {
            //获取日期
            String dateStr = waterQuality.getDate().getYear() + "-" + waterQuality.getDate().getMonthValue() + "-" + waterQuality.getDate().getDayOfMonth();
            dateStrs.add(dateStr);

            //获取当天时间的水质
            List<WaterQuality> waterQualityList = this.getWaterQualitiesBySpecificDate(dateStr);

            //求平均值
            double average = calAverage(indicator, waterQualityList);
            BigDecimal bigDecimal = BigDecimal.valueOf(average);
            forPlot.add(bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
        }

        for (int i = 0; i < 3; i++) {
            forPrediction.add(forPlot.get(i));
        }

        Collections.reverse(dateStrs);
        Collections.reverse(forPlot);
        Collections.reverse(forPrediction);

        Prediction predictionVO = new Prediction();
        predictionVO.setDateStrs(dateStrs);
        predictionVO.setForPlot(forPlot);
        predictionVO.setForPrediction(forPrediction);

        return predictionVO;
    }

    /**
     * 根据日期获取当天内的水质数据
     *
     * @param dateStr
     * @return
     */
    private List<WaterQuality> getWaterQualitiesBySpecificDate(String dateStr) {
        String startDateStr = dateStr + " 00:00:00";
        String endDateStr = dateStr + " 23:59:59";

        LocalDateTime startDate = LocalDateTime.parse(startDateStr);
        LocalDateTime endDate = LocalDateTime.parse(endDateStr);

        LambdaQueryWrapper<WaterQuality> wrapper = new LambdaQueryWrapper<>();
        wrapper.between(WaterQuality::getDate, startDate, endDate).orderByAsc(WaterQuality::getDate);

        List<WaterQuality> list = this.list(wrapper);

        log.info("当天水质数据量: " + list.size());
        return list;
    }
}
