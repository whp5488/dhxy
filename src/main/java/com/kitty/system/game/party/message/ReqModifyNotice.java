
package com.kitty.system.game.party.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 4278)
public class ReqModifyNotice extends Message
{
    private String msg;
    private byte type;
    
    public String getMsg() {
        return this.msg;
    }
    
    public byte getType() {
        return this.type;
    }
    
    public void setMsg(final String msg) {
        this.msg = msg;
    }
    
    public void setType(final byte type) {
        this.type = type;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ReqModifyNotice)) {
            return false;
        }
        final ReqModifyNotice other = (ReqModifyNotice)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$msg = this.getMsg();
        final Object other$msg = other.getMsg();
        if (this$msg == null) {
            if (other$msg == null) {
                return this.getType() == other.getType();
            }
        }
        else if (this$msg.equals(other$msg)) {
            return this.getType() == other.getType();
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ReqModifyNotice;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $msg = this.getMsg();
        result = result * 59 + (($msg == null) ? 43 : $msg.hashCode());
        result = result * 59 + this.getType();
        return result;
    }
    
    @Override
    public String toString() {
        return "ReqModifyNotice(msg=" + this.getMsg() + ", type=" + this.getType() + ")";
    }
}
