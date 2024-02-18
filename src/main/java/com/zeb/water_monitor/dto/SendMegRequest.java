package com.zeb.water_monitor.dto;

/**
 * @author zeb
 * @Date 2024-02-17 19:32
 */
public class SendMegRequest {

    private String userId;

    private String msg;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getSendMsg(){
        return userId + " send send to you : " + msg;
    }

}