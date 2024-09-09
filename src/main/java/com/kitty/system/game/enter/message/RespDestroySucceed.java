package com.kitty.system.game.enter.message;

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
        RespDestroySucceed other = (RespDestroySucceed)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$type = this.getType();
        Object other$type = other.getType();
        if (!((this$type != null) ? this$type.equals(other$type) : (other$type == null))) {
            return false;
        }
        Object this$value = this.getValue();
        Object other$value = other.getValue();
        return (this$value != null) ? this$value.equals(other$value) : (other$value == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespDestroySucceed;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $type = this.getType();
        result = result * 59 + (($type == null) ? 43 : $type.hashCode());
        Object $value = this.getValue();
        result = result * 59 + (($value == null) ? 43 : $value.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "RespDestroySucceed(type=" + this.getType() + ", value=" + this.getValue() + ")";
    }
    
    public RespDestroySucceed(final Integer type, final String value) {
        this.type = type;
        this.value = value;
    }
    
    public RespDestroySucceed() {
    }
}
