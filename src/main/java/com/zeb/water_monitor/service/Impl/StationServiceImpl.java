package com.zeb.water_monitor.service.Impl;

import com.zeb.water_monitor.entity.Station;
import com.zeb.water_monitor.mapper.StationMapper;
import com.zeb.water_monitor.service.StationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zeb
 * @since 2023-05-21
 */
@Service
public class StationServiceImpl extends ServiceImpl<StationMapper, Station> implements StationService {

}
