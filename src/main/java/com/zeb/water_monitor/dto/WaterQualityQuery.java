package com.zeb.water_monitor.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * @author zeb
 * @Date 2023-05-07-14:08
 */

@Data
public class WaterQualityQuery extends PageInfo {
    @ApiModelProperty("监测船")
    private String station;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty("起始时间")
    private LocalDate startDate;

    @ApiModelProperty("结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @ApiModelProperty("监测区域")
    private String area;
}
