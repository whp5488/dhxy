package com.kitty.system.game.team.domain;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 61593)
public class RespClearnAllRequest extends Message
{
    private String type;
    
    public RespClearnAllRequest() {
    }
    
    public String getType() {
        return this.type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
}
