package com.zeb.water_monitor.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * @author zeb
 * @Date 2023-05-07-14:08
 */

@Data
public class WaterQualityQuery extends PageInfo {
    private Integer station;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
}
