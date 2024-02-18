package com.zeb.water_monitor.dto;

import lombok.Builder;
import lombok.Data;

/**
 * @author zeb
 * @Date 2024-02-17 19:58
 */
@Data
@Builder
public class MessageDTO<T> {
    private String message;
    private T data;
}
