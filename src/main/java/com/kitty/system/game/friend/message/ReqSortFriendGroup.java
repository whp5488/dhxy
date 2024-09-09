package com.kitty.system.game.friend.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 45181)
public class ReqSortFriendGroup extends Message
{
    private String id;
    private short sort;
    
    public ReqSortFriendGroup() {
    }
    
    public String getId() {
        return this.id;
    }
    
    public short getSort() {
        return this.sort;
    }
    
    public void setId(final String id) {
        this.id = id;
    }
    
    public void setSort(final short sort) {
        this.sort = sort;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ReqSortFriendGroup)) {
            return false;
        }
        ReqSortFriendGroup other = (ReqSortFriendGroup)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$id = this.getId();
        Object other$id = other.getId();
        return ((this$id != null) ? this$id.equals(other$id) : (other$id == null)) && this.getSort() == other.getSort();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ReqSortFriendGroup;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $id = this.getId();
        result = result * 59 + (($id == null) ? 43 : $id.hashCode());
        result = result * 59 + this.getSort();
        return result;
    }
    
    @Override
    public String toString() {
        return "ReqSortFriendGroup(id=" + this.getId() + ", sort=" + (int)this.getSort() + ")";
    }
}
