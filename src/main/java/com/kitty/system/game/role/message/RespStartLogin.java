package com.kitty.system.game.role.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 45555)
public class RespStartLogin extends Message
{
    private String type;
    private String cookie;
    
    public RespStartLogin() {
        this.type = "normal";
    }
    
    public String getCookie() {
        return this.cookie;
    }
    
    public String getType() {
        return this.type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public void setCookie(String cookie) {
        this.cookie = cookie;
    }
}
