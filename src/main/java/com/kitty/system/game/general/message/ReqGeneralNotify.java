package com.kitty.system.game.general.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 63752)
public class ReqGeneralNotify extends Message
{
    private int type;
    private String status;
    private String string;
    
    public ReqGeneralNotify() {
    }
    
    public int getType() {
        return this.type;
    }
    
    public String getStatus() {
        return this.status;
    }
    
    public String getString() {
        return this.string;
    }
    
    public void setType(final int type) {
        this.type = type;
    }
    
    public void setStatus(final String status) {
        this.status = status;
    }
    
    public void setString(final String string) {
        this.string = string;
    }
}
