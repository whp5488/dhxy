
package com.kitty.system.game.bag.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 8236)
public class ReqItemUse extends Message
{
    private short pos;
    private short num;
    
    public short getPos() {
        return this.pos;
    }
    
    public short getNum() {
        return this.num;
    }
    
    public void setPos(final short pos) {
        this.pos = pos;
    }
    
    public void setNum(final short num) {
        this.num = num;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ReqItemUse)) {
            return false;
        }
        final ReqItemUse other = (ReqItemUse)o;
        return other.canEqual(this) && this.getPos() == other.getPos() && this.getNum() == other.getNum();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ReqItemUse;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getPos();
        result = result * 59 + this.getNum();
        return result;
    }
    
    @Override
    public String toString() {
        return "ReqItemUse(pos=" + this.getPos() + ", num=" + this.getNum() + ")";
    }
}
