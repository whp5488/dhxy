package com.kitty.system.game.welfare.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 40971)
public class RespGiftState extends Message
{
    private short type;
    private byte state;
    private byte is;
    
    public RespGiftState() {
        this.type = 1;
        this.state = 1;
        this.is = 1;
    }
    
    public short getType() {
        return this.type;
    }
    
    public byte getState() {
        return this.state;
    }
    
    public byte getIs() {
        return this.is;
    }
    
    public void setType(final short type) {
        this.type = type;
    }
    
    public void setState(final byte state) {
        this.state = state;
    }
    
    public void setIs(final byte is) {
        this.is = is;
    }
}
