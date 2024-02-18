package com.zeb.water_monitor.server;

import com.zeb.water_monitor.common.CustomException;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zeb
 * @Date 2024-02-17 12:10
 */
public class SseSession {

    private static final Map<String, SseEmitter> sessionMap = new ConcurrentHashMap<>();

    public static void add(String sessionKey, SseEmitter sseEmitter) {
        if (sessionMap.get(sessionKey) != null) {
            throw new CustomException("User exists!");
        }
        sessionMap.put(sessionKey, sseEmitter);
    }

    public static boolean exists(String sessionKey) {
        return sessionMap.get(sessionKey) != null;
    }

    public static boolean remove(String sessionKey) {
        SseEmitter sseEmitter = sessionMap.get(sessionKey);
        if (sseEmitter != null) {
            sseEmitter.complete();
        }
        return false;
    }

    public static void onError(String sessionKey, Throwable throwable) {
        SseEmitter sseEmitter = sessionMap.get(sessionKey);
        if (sseEmitter != null) {
            sseEmitter.completeWithError(throwable);
        }
    }

    public static void send(String sessionKey, String content) throws IOException {
        sessionMap.get(sessionKey).send(content);
    }

}