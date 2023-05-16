package com.zeb.water_monitor.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * @author zeb
 * @Date 2023-05-16 16:52
 */
@Data
public class GetAverageDTO {
    @ApiModelProperty("区域")
    private String area;

    @ApiModelProperty("开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startTime;

    @ApiModelProperty("结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endTime;
}
