package com.kitty.system.game.team.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 4132)
public class ReqAccept extends Message
{
    private int roleId;
    private String name;
    private String type;
    
    public ReqAccept() {
    }
    
    public int getRoleId() {
        return this.roleId;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getType() {
        return this.type;
    }
    
    public void setRoleId(final int roleId) {
        this.roleId = roleId;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public void setType(final String type) {
        this.type = type;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ReqAccept)) {
            return false;
        }
        ReqAccept other = (ReqAccept)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getRoleId() != other.getRoleId()) {
            return false;
        }
        Object this$name = this.getName();
        Object other$name = other.getName();
        if (!((this$name != null) ? this$name.equals(other$name) : (other$name == null))) {
            return false;
        }
        Object this$type = this.getType();
        Object other$type = other.getType();
        return (this$type != null) ? this$type.equals(other$type) : (other$type == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ReqAccept;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getRoleId();
        Object $name = this.getName();
        result = result * 59 + (($name == null) ? 43 : $name.hashCode());
        Object $type = this.getType();
        result = result * 59 + (($type == null) ? 43 : $type.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "ReqAccept(roleId=" + this.getRoleId() + ", name=" + this.getName() + ", type=" + this.getType() + ")";
    }
}
