
package com.kitty.system.game.enter.message;

import java.beans.ConstructorProperties;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 32918)
public class RespDestroySucceed extends Message
{
    private Integer type;
    private String value;
    
    public Integer getType() {
        return this.type;
    }
    
    public String getValue() {
        return this.value;
    }
    
    public void setType(final Integer type) {
        this.type = type;
    }
    
    public void setValue(final String value) {
        this.value = value;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespDestroySucceed)) {
            return false;
        }
        final RespDestroySucceed other = (RespDestroySucceed)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$type = this.getType();
        final Object other$type = other.getType();
        Label_0065: {
            if (this$type == null) {
                if (other$type == null) {
                    break Label_0065;
                }
            }
            else if (this$type.equals(other$type)) {
                break Label_0065;
            }
            return false;
        }
        final Object this$value = this.getValue();
        final Object other$value = other.getValue();
        if (this$value == null) {
            if (other$value == null) {
                return true;
            }
        }
        else if (this$value.equals(other$value)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespDestroySucceed;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $type = this.getType();
        result = result * 59 + (($type == null) ? 43 : $type.hashCode());
        final Object $value = this.getValue();
        result = result * 59 + (($value == null) ? 43 : $value.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "RespDestroySucceed(type=" + this.getType() + ", value=" + this.getValue() + ")";
    }
    
    @ConstructorProperties({ "type", "value" })
    public RespDestroySucceed(final Integer type, final String value) {
        this.type = type;
        this.value = value;
    }
    
    public RespDestroySucceed() {
    }
}
