package com.kitty.system.game.equip.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 41490)
public class ReqOperateEffect extends Message
{
    private int serial;
    private byte type;
    
    public ReqOperateEffect() {
    }
    
    public int getSerial() {
        return this.serial;
    }
    
    public byte getType() {
        return this.type;
    }
    
    public void setSerial(final int serial) {
        this.serial = serial;
    }
    
    public void setType(final byte type) {
        this.type = type;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ReqOperateEffect)) {
            return false;
        }
        ReqOperateEffect other = (ReqOperateEffect)o;
        return other.canEqual(this) && this.getSerial() == other.getSerial() && this.getType() == other.getType();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ReqOperateEffect;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getSerial();
        result = result * 59 + this.getType();
        return result;
    }
    
    @Override
    public String toString() {
        return "ReqOperateEffect(serial=" + this.getSerial() + ", type=" + (int)this.getType() + ")";
    }
}
