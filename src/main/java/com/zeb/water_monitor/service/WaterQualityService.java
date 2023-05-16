package com.zeb.water_monitor.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zeb.water_monitor.entity.WaterQuality;
import com.zeb.water_monitor.vo.PlotVO;
import com.zeb.water_monitor.vo.Prediction;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zeb
 * @since 2023-05-06
 */
public interface WaterQualityService extends IService<WaterQuality> {
    /**
     * 获取数据
     * @param area
     * @param period
     * @param indicator
     * @return
     */
    PlotVO getDataForPlot(String area,Integer period,String indicator);


    Prediction getDateForPrediction(String indicator);

}
