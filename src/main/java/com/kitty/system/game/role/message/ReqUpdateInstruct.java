package com.kitty.system.game.role.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 45557)
public class ReqUpdateInstruct extends Message
{
    private byte type;
    private short serial;
    private byte index;
    private String string;
    
    public ReqUpdateInstruct() {
    }
    
    public byte getType() {
        return this.type;
    }
    
    public short getSerial() {
        return this.serial;
    }
    
    public byte getIndex() {
        return this.index;
    }
    
    public String getString() {
        return this.string;
    }
    
    public void setType(final byte type) {
        this.type = type;
    }
    
    public void setSerial(final short serial) {
        this.serial = serial;
    }
    
    public void setIndex(final byte index) {
        this.index = index;
    }
    
    public void setString(final String string) {
        this.string = string;
    }
}
