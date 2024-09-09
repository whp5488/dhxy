package com.kitty.system.game.activity.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 49999)
public class RespOffRoleState extends Message
{
    private String type;
    private byte state;
    
    public String getType() {
        return this.type;
    }
    
    public byte getState() {
        return this.state;
    }
    
    public void setType(final String type) {
        this.type = type;
    }
    
    public void setState(final byte state) {
        this.state = state;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespOffRoleState)) {
            return false;
        }
        RespOffRoleState other = (RespOffRoleState)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$type = this.getType();
        Object other$type = other.getType();
        return ((this$type != null) ? this$type.equals(other$type) : (other$type == null)) && this.getState() == other.getState();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespOffRoleState;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $type = this.getType();
        result = result * 59 + (($type == null) ? 43 : $type.hashCode());
        result = result * 59 + this.getState();
        return result;
    }
    
    @Override
    public String toString() {
        return "RespOffRoleState(type=" + this.getType() + ", state=" + (int)this.getState() + ")";
    }
    
    public RespOffRoleState(final String type, final byte state) {
        this.type = type;
        this.state = state;
    }
    
    public RespOffRoleState() {
    }
}
