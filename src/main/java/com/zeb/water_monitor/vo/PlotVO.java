package com.zeb.water_monitor.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author zeb
 * @Date 2023-05-08-22:30
 */
@Data
@ApiModel("水质数据示意图")
public class PlotVO {
    @ApiModelProperty("数值")
    private List<Float> specWaterQualities;
    @ApiModelProperty("时间轴")
    private List<String> dates;
}
