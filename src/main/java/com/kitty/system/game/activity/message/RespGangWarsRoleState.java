
package com.kitty.system.game.activity.message;

import java.beans.ConstructorProperties;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 49167)
public class RespGangWarsRoleState extends Message
{
    private byte state;
    private int time;
    
    public byte getState() {
        return this.state;
    }
    
    public int getTime() {
        return this.time;
    }
    
    public void setState(final byte state) {
        this.state = state;
    }
    
    public void setTime(final int time) {
        this.time = time;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespGangWarsRoleState)) {
            return false;
        }
        final RespGangWarsRoleState other = (RespGangWarsRoleState)o;
        return other.canEqual(this) && this.getState() == other.getState() && this.getTime() == other.getTime();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespGangWarsRoleState;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getState();
        result = result * 59 + this.getTime();
        return result;
    }
    
    @Override
    public String toString() {
        return "RespGangWarsRoleState(state=" + this.getState() + ", time=" + this.getTime() + ")";
    }
    
    @ConstructorProperties({ "state", "time" })
    public RespGangWarsRoleState(final byte state, final int time) {
        this.state = 0;
        this.time = 0;
        this.state = state;
        this.time = time;
    }
    
    public RespGangWarsRoleState() {
        this.state = 0;
        this.time = 0;
    }
}
