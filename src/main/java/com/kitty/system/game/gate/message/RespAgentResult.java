package com.kitty.system.game.gate.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 13143)
public class RespAgentResult extends Message
{
    private int auth_key;
    private int result;
    private String msg;
    
    public RespAgentResult() {
        this.result = 1;
    }
    
    public int getAuth_key() {
        return this.auth_key;
    }
    
    public int getResult() {
        return this.result;
    }
    
    public String getMsg() {
        return this.msg;
    }
    
    public void setAuth_key(final int auth_key) {
        this.auth_key = auth_key;
    }
    
    public void setResult(final int result) {
        this.result = result;
    }
    
    public void setMsg(final String msg) {
        this.msg = msg;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespAgentResult)) {
            return false;
        }
        RespAgentResult other = (RespAgentResult)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getAuth_key() != other.getAuth_key()) {
            return false;
        }
        if (this.getResult() != other.getResult()) {
            return false;
        }
        Object this$msg = this.getMsg();
        Object other$msg = other.getMsg();
        return (this$msg != null) ? this$msg.equals(other$msg) : (other$msg == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespAgentResult;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getAuth_key();
        result = result * 59 + this.getResult();
        Object $msg = this.getMsg();
        result = result * 59 + (($msg == null) ? 43 : $msg.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "RespAgentResult(auth_key=" + this.getAuth_key() + ", result=" + this.getResult() + ", msg=" + this.getMsg() + ")";
    }
}
