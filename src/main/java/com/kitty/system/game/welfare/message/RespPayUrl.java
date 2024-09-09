package com.kitty.system.game.welfare.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 32853)
public class RespPayUrl extends Message
{
    private String front;
    private String back;
    
    public RespPayUrl() {
    }
    
    public RespPayUrl(String front, String back) {
        this.front = front;
        this.back = back;
    }
    
    public String getFront() {
        return this.front;
    }
    
    public void setFront(String front) {
        this.front = front;
    }
    
    public String getBack() {
        return this.back;
    }
    
    public void setBack(String back) {
        this.back = back;
    }
}
