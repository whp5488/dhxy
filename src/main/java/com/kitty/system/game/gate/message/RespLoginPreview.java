package com.kitty.system.game.gate.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 24113)
public class RespLoginPreview extends Message
{
    private String sid;
    private int time;
    private String cookie;
    
    public RespLoginPreview() {
        this.time = 0;
    }
    
    public void setSid(final String sid) {
        this.sid = sid;
    }
    
    public void setTime(final int time) {
        this.time = time;
    }
    
    public void setCookie(final String cookie) {
        this.cookie = cookie;
    }
    
    public String getSid() {
        return this.sid;
    }
    
    public int getTime() {
        return this.time;
    }
    
    public String getCookie() {
        return this.cookie;
    }
}
