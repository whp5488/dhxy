package com.kitty.system.game.train.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 20202)
public class ReqChangeSeal extends Message
{
    private byte type;
    private short position;
    private String id;
    
    public ReqChangeSeal() {
        this.type = 0;
        this.position = 0;
    }
    
    public byte getType() {
        return this.type;
    }
    
    public short getPosition() {
        return this.position;
    }
    
    public String getId() {
        return this.id;
    }
    
    public void setType(final byte type) {
        this.type = type;
    }
    
    public void setPosition(final short position) {
        this.position = position;
    }
    
    public void setId(final String id) {
        this.id = id;
    }
}
