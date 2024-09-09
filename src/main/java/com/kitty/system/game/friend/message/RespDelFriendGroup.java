package com.kitty.system.game.friend.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 45317)
public class RespDelFriendGroup extends Message
{
    private String id;
    
    public String getId() {
        return this.id;
    }
    
    public void setId(final String id) {
        this.id = id;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespDelFriendGroup)) {
            return false;
        }
        RespDelFriendGroup other = (RespDelFriendGroup)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$id = this.getId();
        Object other$id = other.getId();
        return (this$id != null) ? this$id.equals(other$id) : (other$id == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespDelFriendGroup;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $id = this.getId();
        result = result * 59 + (($id == null) ? 43 : $id.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "RespDelFriendGroup(id=" + this.getId() + ")";
    }
    
    public RespDelFriendGroup(final String id) {
        this.id = id;
    }
    
    public RespDelFriendGroup() {
    }
}
