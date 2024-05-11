
package com.kitty.system.common.message;

import com.kitty.mina.annotation.MessageMeta;
import org.nutz.json.ToJson;
import com.kitty.mina.message.Message;

@ToJson
@MessageMeta(module = 8165)
public class RespMsg extends Message
{
    private String msg;
    private short active;
    
    public RespMsg() {
        this.active = 0;
    }
    
    public RespMsg(final String msg) {
        this.active = 0;
        this.msg = msg;
    }
    
    public RespMsg(final String msg, final short active) {
        this.active = 0;
        this.msg = msg;
        this.active = active;
    }
    
    public String getMsg() {
        return this.msg;
    }
    
    public short getActive() {
        return this.active;
    }
    
    public void setMsg(final String msg) {
        this.msg = msg;
    }
    
    public void setActive(final short active) {
        this.active = active;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespMsg)) {
            return false;
        }
        final RespMsg other = (RespMsg)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$msg = this.getMsg();
        final Object other$msg = other.getMsg();
        if (this$msg == null) {
            if (other$msg == null) {
                return this.getActive() == other.getActive();
            }
        }
        else if (this$msg.equals(other$msg)) {
            return this.getActive() == other.getActive();
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespMsg;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $msg = this.getMsg();
        result = result * 59 + (($msg == null) ? 43 : $msg.hashCode());
        result = result * 59 + this.getActive();
        return result;
    }
    
    @Override
    public String toString() {
        return "RespMsg(msg=" + this.getMsg() + ", active=" + this.getActive() + ")";
    }
}
