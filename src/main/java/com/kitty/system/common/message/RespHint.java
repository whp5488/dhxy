
package com.kitty.system.common.message;

import java.beans.ConstructorProperties;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 8866)
public class RespHint extends Message
{
    private int type;
    private short result;
    
    public int getType() {
        return this.type;
    }
    
    public short getResult() {
        return this.result;
    }
    
    public void setType(final int type) {
        this.type = type;
    }
    
    public void setResult(final short result) {
        this.result = result;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespHint)) {
            return false;
        }
        final RespHint other = (RespHint)o;
        return other.canEqual(this) && this.getType() == other.getType() && this.getResult() == other.getResult();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespHint;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getType();
        result = result * 59 + this.getResult();
        return result;
    }
    
    @Override
    public String toString() {
        return "RespHint(type=" + this.getType() + ", result=" + this.getResult() + ")";
    }
    
    public RespHint() {
        this.result = 0;
    }
    
    @ConstructorProperties({ "type", "result" })
    public RespHint(final int type, final short result) {
        this.result = 0;
        this.type = type;
        this.result = result;
    }
}
