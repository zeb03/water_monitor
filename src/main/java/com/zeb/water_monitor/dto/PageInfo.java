package com.zeb.water_monitor.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author zeb
 * @Date 2023-05-07-13:51
 */
@Data
public class PageInfo {

    /**
     * 页码
     */
    @NotNull
    @ApiModelProperty("页码")
    private Integer pageNum;

    /**
     * 每页显示条数
     */
    @NotNull
    @ApiModelProperty("每页显示条数")
    private Integer pageSize;

}
