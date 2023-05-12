package com.zeb.water_monitor.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author zeb
 * @Date 2023-05-07-21:07
 */
@Data
@ApiModel(value = "添加水质数据实体对象", description = "添加水质数据实体对象")
public class AddWaterQualityDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableField("PH")
    @ApiModelProperty("PH值")
    private Float ph;

    @TableField("temperature")
    @ApiModelProperty("温度")
    private Float temperature;

    @TableField("conductivity")
    @ApiModelProperty("电导率")
    private Float conductivity;

    @TableField("turbidity")
    @ApiModelProperty("浊度")
    private Float turbidity;

    @TableField("station")
    @ApiModelProperty("监测船")
    private Integer station;


}
