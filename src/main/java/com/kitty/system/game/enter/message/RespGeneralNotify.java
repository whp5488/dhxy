package com.kitty.system.game.enter.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 9129)
public class RespGeneralNotify extends Message
{
    private int notify;
    private String value;
    
    public RespGeneralNotify() {
    }
    
    public int getNotify() {
        return this.notify;
    }
    
    public String getValue() {
        return this.value;
    }
    
    public void setNotify(final int notify) {
        this.notify = notify;
    }
    
    public void setValue(final String value) {
        this.value = value;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespGeneralNotify)) {
            return false;
        }
        RespGeneralNotify other = (RespGeneralNotify)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getNotify() != other.getNotify()) {
            return false;
        }
        Object this$value = this.getValue();
        Object other$value = other.getValue();
        return (this$value != null) ? this$value.equals(other$value) : (other$value == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespGeneralNotify;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getNotify();
        Object $value = this.getValue();
        result = result * 59 + (($value == null) ? 43 : $value.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "RespGeneralNotify(notify=" + this.getNotify() + ", value=" + this.getValue() + ")";
    }
}
