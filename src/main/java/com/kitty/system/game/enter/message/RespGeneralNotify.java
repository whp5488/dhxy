
package com.kitty.system.game.enter.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 9129)
public class RespGeneralNotify extends Message
{
    private int notify;
    private String value;
    
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
        final RespGeneralNotify other = (RespGeneralNotify)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getNotify() != other.getNotify()) {
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
        return other instanceof RespGeneralNotify;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getNotify();
        final Object $value = this.getValue();
        result = result * 59 + (($value == null) ? 43 : $value.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "RespGeneralNotify(notify=" + this.getNotify() + ", value=" + this.getValue() + ")";
    }
}
