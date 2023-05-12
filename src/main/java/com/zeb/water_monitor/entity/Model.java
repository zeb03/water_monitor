package com.zeb.water_monitor.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author zeb
 * @since 2023-05-09
 */
@Getter
@Setter
@TableName("model")
@ApiModel(value = "Model对象", description = "")
public class Model implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("name")
    private String name;

    @TableField("target")
    private String target;

    @TableField("method")
    private String method;

    @TableField("rmse")
    private Float rmse;

    @TableField("uid")
    private Integer uid;

    @TableField("date")
    private LocalDateTime date;


}
