package com.zeb.water_monitor.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zeb.water_monitor.enums.RoleEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author zeb
 * @since 2023-05-06
 */
@Data
@TableName("user")
@ApiModel(value = "User对象")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty("用户id")
    private Integer id;

    @TableField("username")
    @ApiModelProperty("用户名")
    private String username;

    @TableField("password")
    @ApiModelProperty("密码")
    @JsonIgnore
    private String password;

    @ApiModelProperty("角色")
    private RoleEnum role;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
