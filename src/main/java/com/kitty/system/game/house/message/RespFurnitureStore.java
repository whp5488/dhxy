
package com.kitty.system.game.house.message;

import java.beans.ConstructorProperties;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 20606)
public class RespFurnitureStore extends Message
{
    private byte is;
    
    public byte getIs() {
        return this.is;
    }
    
    public void setIs(final byte is) {
        this.is = is;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespFurnitureStore)) {
            return false;
        }
        final RespFurnitureStore other = (RespFurnitureStore)o;
        return other.canEqual(this) && this.getIs() == other.getIs();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespFurnitureStore;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getIs();
        return result;
    }
    
    @Override
    public String toString() {
        return "RespFurnitureStore(is=" + this.getIs() + ")";
    }
    
    @ConstructorProperties({ "is" })
    public RespFurnitureStore(final byte is) {
        this.is = is;
    }
    
    public RespFurnitureStore() {
    }
}
