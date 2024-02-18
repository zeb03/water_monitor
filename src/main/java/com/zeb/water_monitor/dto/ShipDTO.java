package com.zeb.water_monitor.dto;

import com.zeb.water_monitor.entity.WaterQuality;
import lombok.Data;

/**
 * @author zeb
 * @Date 2024-02-17 23:01
 */
@Data
public class ShipDTO extends WaterQuality {
    private String name;
    private String lng;
    private String lat;
}
