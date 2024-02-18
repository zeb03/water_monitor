package com.zeb.water_monitor.server;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * @author zeb
 * @Date 2024-02-17 12:08
 */
public interface SseServer {

    public SseEmitter conect(String userId);

    public boolean send(String userId, String content);

    public boolean close(String userId);

}
