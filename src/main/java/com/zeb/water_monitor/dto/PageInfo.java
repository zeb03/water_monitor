package com.zeb.water_monitor.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zeb
 * @Date 2023-05-07-13:51
 */
@Data
public class PageInfo {

    /**
     * 页码
     */
    @ApiModelProperty("页码")
    private Integer pageNum;

    /**
     * 每页显示条数
     */
    @ApiModelProperty("每页显示条数")
    private Integer pageSize;

}
