package com.zeb.water_monitor.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Collection;
import java.util.Set;

/**
 * @author zeb
 * @Date 2023-05-14 16:27
 */
@Data
@ApiModel("不达标区域")
public class SubstandardAreas {

    @ApiModelProperty("不达标区域")
    private Set<String> areas;

    @ApiModelProperty("不达标天数")
    private Collection<Integer> days;
}
