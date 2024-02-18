package com.zeb.water_monitor.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author zeb
 * @since 2023-05-06
 */
@Data
@TableName("water_quality")
@ApiModel(value = "水质数据对象")
public class WaterQuality implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty("id")
    private Integer id;

    @TableField("ph")
    @ApiModelProperty("ph值")
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

    @TableField("tds")
    @ApiModelProperty("溶氧")
    private Float tds;

    @TableField("date")
    @ApiModelProperty("日期")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime date;

    @TableField("station")
    @ApiModelProperty("监测船")
    private String station;

    @TableField("area")
    @ApiModelProperty("监测区域")
    private String area;

    @TableField("lon")
    @ApiModelProperty("经度")
    private String lng;

    @TableField("lat")
    @ApiModelProperty("维度")
    private String lat;
}
