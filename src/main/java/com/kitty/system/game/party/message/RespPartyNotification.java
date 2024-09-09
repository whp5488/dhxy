package com.kitty.system.game.party.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 4115)
public class RespPartyNotification extends Message
{
    private short type;
    
    public RespPartyNotification() {
    }
    
    public RespPartyNotification(short type) {
        this.type = type;
    }
    
    public void setType(final short type) {
        this.type = type;
    }
    
    public short getType() {
        return this.type;
    }
}
