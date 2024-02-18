package com.zeb.water_monitor.controller;

import com.zeb.water_monitor.dto.SendMegRequest;
import com.zeb.water_monitor.server.SseServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * @author zeb
 * @Date 2024-02-17 12:07
 */
@Controller
@RequestMapping(value = "sse")
@ResponseBody
@CrossOrigin
public class SseController {


    @Autowired
    private SseServer sseServer;


    @GetMapping(value = "/subscribe/{userId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(@PathVariable("userId") String userId){
        return sseServer.conect(userId);
    }


    @PostMapping(value = "/send/{userId}")
    public String sendMessage(@PathVariable("userId") String userId ,@RequestBody SendMegRequest sendMegRequest){
        if(sseServer.send(sendMegRequest.getUserId(), sendMegRequest.getSendMsg())){
            return "Success";
        }
        return "Faild";
    }

    @GetMapping(value = "/close/{userId}")
    public void close(@PathVariable("userId") String userId){
        sseServer.close(userId);
    }

}