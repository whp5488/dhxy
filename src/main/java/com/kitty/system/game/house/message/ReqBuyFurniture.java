
package com.kitty.system.game.house.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 20577)
public class ReqBuyFurniture extends Message
{
    private int serial;
    
    public int getSerial() {
        return this.serial;
    }
    
    public void setSerial(final int serial) {
        this.serial = serial;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ReqBuyFurniture)) {
            return false;
        }
        final ReqBuyFurniture other = (ReqBuyFurniture)o;
        return other.canEqual(this) && this.getSerial() == other.getSerial();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ReqBuyFurniture;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getSerial();
        return result;
    }
    
    @Override
    public String toString() {
        return "ReqBuyFurniture(serial=" + this.getSerial() + ")";
    }
}
