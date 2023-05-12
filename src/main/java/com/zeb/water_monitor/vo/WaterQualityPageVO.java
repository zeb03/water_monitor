package com.zeb.water_monitor.vo;

import com.zeb.water_monitor.entity.WaterQuality;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author zeb
 * @Date 2023-05-07-21:54
 */
@Data
@ApiModel("返回分页对象")
public class WaterQualityPageVO{

    @ApiModelProperty("返回分页数据")
    private List<WaterQuality> records;

    @ApiModelProperty("总数")
    private Long total;
}
