package com.zeb.water_monitor.service;

import com.zeb.water_monitor.entity.Model;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zeb.water_monitor.vo.Prediction;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zeb
 * @since 2023-05-09
 */
public interface ModelService extends IService<Model> {
    /**
     *
     * @param modelId
     * @param indicator
     * @return
     */
    Prediction predictNextMonth(int modelId, String indicator);
}
