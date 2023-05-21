package com.zeb.water_monitor.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zeb
 * @Date 2023-05-19 21:46
 */
@Data
public class MqttDataDTO {

    @ApiModelProperty("ph值")
    private Float PH;

    @ApiModelProperty("温度")
    private Float temper;

    @ApiModelProperty("电导率")
    private Float EC;

    @ApiModelProperty("浊度")
    private Float TU;
}
