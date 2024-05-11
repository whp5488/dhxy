
package com.kitty.system.game.bag.message;

import java.beans.ConstructorProperties;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 65461)
public class RespDeleteItem extends Message
{
    private byte type;
    private int pos;
    
    public byte getType() {
        return this.type;
    }
    
    public int getPos() {
        return this.pos;
    }
    
    public void setType(final byte type) {
        this.type = type;
    }
    
    public void setPos(final int pos) {
        this.pos = pos;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespDeleteItem)) {
            return false;
        }
        final RespDeleteItem other = (RespDeleteItem)o;
        return other.canEqual(this) && this.getType() == other.getType() && this.getPos() == other.getPos();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespDeleteItem;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getType();
        result = result * 59 + this.getPos();
        return result;
    }
    
    @Override
    public String toString() {
        return "RespDeleteItem(type=" + this.getType() + ", pos=" + this.getPos() + ")";
    }
    
    @ConstructorProperties({ "type", "pos" })
    public RespDeleteItem(final byte type, final int pos) {
        this.type = type;
        this.pos = pos;
    }
}
