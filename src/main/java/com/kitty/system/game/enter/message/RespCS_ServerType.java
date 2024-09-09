package com.kitty.system.game.enter.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 45277)
public class RespCS_ServerType extends Message
{
    private int server_type;
    
    public RespCS_ServerType() {
        this.server_type = 0;
    }
    
    public int getServer_type() {
        return this.server_type;
    }
    
    public void setServer_type(int server_type) {
        this.server_type = server_type;
    }
}
