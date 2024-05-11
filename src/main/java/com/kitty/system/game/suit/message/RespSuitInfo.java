
package com.kitty.system.game.suit.message;

import java.beans.ConstructorProperties;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 4755)
public class RespSuitInfo extends Message
{
    private byte is;
    private int num;
    
    public byte getIs() {
        return this.is;
    }
    
    public int getNum() {
        return this.num;
    }
    
    public void setIs(final byte is) {
        this.is = is;
    }
    
    public void setNum(final int num) {
        this.num = num;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespSuitInfo)) {
            return false;
        }
        final RespSuitInfo other = (RespSuitInfo)o;
        return other.canEqual(this) && this.getIs() == other.getIs() && this.getNum() == other.getNum();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespSuitInfo;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getIs();
        result = result * 59 + this.getNum();
        return result;
    }
    
    @Override
    public String toString() {
        return "RespSuitInfo(is=" + this.getIs() + ", num=" + this.getNum() + ")";
    }
    
    @ConstructorProperties({ "is", "num" })
    public RespSuitInfo(final byte is, final int num) {
        this.is = 0;
        this.num = 0;
        this.is = is;
        this.num = num;
    }
    
    public RespSuitInfo() {
        this.is = 0;
        this.num = 0;
    }
}
