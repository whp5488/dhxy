package com.kitty.system.game.team.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 4120)
public class ReqKickOutTeam extends Message
{
    private String name;
    
    public ReqKickOutTeam() {
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ReqKickOutTeam)) {
            return false;
        }
        ReqKickOutTeam other = (ReqKickOutTeam)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$name = this.getName();
        Object other$name = other.getName();
        return (this$name != null) ? this$name.equals(other$name) : (other$name == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ReqKickOutTeam;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $name = this.getName();
        result = result * 59 + (($name == null) ? 43 : $name.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "ReqKickOutTeam(name=" + this.getName() + ")";
    }
}
