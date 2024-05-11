
package com.kitty.system.game.general.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 63752)
public class ReqGeneralNotify extends Message
{
    private int type;
    private String status;
    private String string;
    
    public int getType() {
        return this.type;
    }
    
    public String getStatus() {
        return this.status;
    }
    
    public String getString() {
        return this.string;
    }
    
    public void setType(final int type) {
        this.type = type;
    }
    
    public void setStatus(final String status) {
        this.status = status;
    }
    
    public void setString(final String string) {
        this.string = string;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ReqGeneralNotify)) {
            return false;
        }
        final ReqGeneralNotify other = (ReqGeneralNotify)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getType() != other.getType()) {
            return false;
        }
        final Object this$status = this.getStatus();
        final Object other$status = other.getStatus();
        Label_0078: {
            if (this$status == null) {
                if (other$status == null) {
                    break Label_0078;
                }
            }
            else if (this$status.equals(other$status)) {
                break Label_0078;
            }
            return false;
        }
        final Object this$string = this.getString();
        final Object other$string = other.getString();
        if (this$string == null) {
            if (other$string == null) {
                return true;
            }
        }
        else if (this$string.equals(other$string)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ReqGeneralNotify;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getType();
        final Object $status = this.getStatus();
        result = result * 59 + (($status == null) ? 43 : $status.hashCode());
        final Object $string = this.getString();
        result = result * 59 + (($string == null) ? 43 : $string.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "ReqGeneralNotify(type=" + this.getType() + ", status=" + this.getStatus() + ", string=" + this.getString() + ")";
    }
}
