
package com.kitty.system.game.chat.message;

import java.util.Arrays;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 64977)
public class RespSyncMessage extends Message
{
    private int code;
    private byte[] msg;
    
    public int getCode() {
        return this.code;
    }
    
    public byte[] getMsg() {
        return this.msg;
    }
    
    public void setCode(final int code) {
        this.code = code;
    }
    
    public void setMsg(final byte[] msg) {
        this.msg = msg;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespSyncMessage)) {
            return false;
        }
        final RespSyncMessage other = (RespSyncMessage)o;
        return other.canEqual(this) && this.getCode() == other.getCode() && Arrays.equals(this.getMsg(), other.getMsg());
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespSyncMessage;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getCode();
        result = result * 59 + Arrays.hashCode(this.getMsg());
        return result;
    }
    
    @Override
    public String toString() {
        return "RespSyncMessage(code=" + this.getCode() + ", msg=" + Arrays.toString(this.getMsg()) + ")";
    }
}
