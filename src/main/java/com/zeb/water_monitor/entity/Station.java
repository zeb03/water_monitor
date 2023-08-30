package com.zeb.water_monitor.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author zeb
 * @since 2023-05-21
 */
@Getter
@Setter
@TableName("station")
@ApiModel(value = "Station对象", description = "")
public class Station implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("监测船名称")
    @TableField("name")
    private String name;

    @ApiModelProperty("监测区域")
    @TableField("area")
    private String area;
}
