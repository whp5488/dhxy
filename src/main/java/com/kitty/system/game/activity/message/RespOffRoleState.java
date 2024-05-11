
package com.kitty.system.game.activity.message;

import java.beans.ConstructorProperties;
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
        final RespOffRoleState other = (RespOffRoleState)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$type = this.getType();
        final Object other$type = other.getType();
        if (this$type == null) {
            if (other$type == null) {
                return this.getState() == other.getState();
            }
        }
        else if (this$type.equals(other$type)) {
            return this.getState() == other.getState();
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespOffRoleState;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $type = this.getType();
        result = result * 59 + (($type == null) ? 43 : $type.hashCode());
        result = result * 59 + this.getState();
        return result;
    }
    
    @Override
    public String toString() {
        return "RespOffRoleState(type=" + this.getType() + ", state=" + this.getState() + ")";
    }
    
    @ConstructorProperties({ "type", "state" })
    public RespOffRoleState(final String type, final byte state) {
        this.type = type;
        this.state = state;
    }
    
    public RespOffRoleState() {
    }
}
