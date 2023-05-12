package com.zeb.water_monitor.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * @author zeb
 * @Date 2023-05-09-16:59
 */
@Getter
public enum RoleEnum {

    /**
     *
     */
    ADMIN(1, "admin"),
    USER(2, "user");

    /**
     * 权限id
     */

    @EnumValue
    private Integer roleId;

    /**
     * 权限名称
     */
    @JsonValue
    private String roleName;

    RoleEnum(Integer id, String name) {
        this.roleId = id;
        this.roleName = name;
    }
}
