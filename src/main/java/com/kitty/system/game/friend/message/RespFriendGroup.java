package com.kitty.system.game.friend.message;

import com.kitty.system.game.friend.message.vo.FriendGroup;
import java.util.List;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 45193)
public class RespFriendGroup extends Message
{
    private byte type;
    private List<FriendGroup> list;
    
    public RespFriendGroup() {
        this.type = 0;
    }
    
    public byte getType() {
        return this.type;
    }
    
    public List<FriendGroup> getList() {
        return this.list;
    }
    
    public void setType(final byte type) {
        this.type = type;
    }
    
    public void setList(final List<FriendGroup> list) {
        this.list = list;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespFriendGroup)) {
            return false;
        }
        RespFriendGroup other = (RespFriendGroup)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getType() != other.getType()) {
            return false;
        }
        Object this$list = this.getList();
        Object other$list = other.getList();
        return (this$list != null) ? this$list.equals(other$list) : (other$list == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespFriendGroup;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getType();
        Object $list = this.getList();
        result = result * 59 + (($list == null) ? 43 : $list.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "RespFriendGroup(type=" + (int)this.getType() + ", list=" + this.getList() + ")";
    }
}
