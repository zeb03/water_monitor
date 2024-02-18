package com.zeb.water_monitor.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zeb
 * @Date 2024-02-17 20:36
 */
@Data
public class IndicatorDTO {
    @ApiModelProperty("ph值")
    private Float ph;

    @ApiModelProperty("温度")
    private Float temperature;

    @ApiModelProperty("电导率")
    private Float conductivity;

    @ApiModelProperty("浊度")
    private Float turbidity;

    @ApiModelProperty("溶氧")
    private Float tds;
}
