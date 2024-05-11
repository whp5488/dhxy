
package com.kitty.system.game.friend.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 45181)
public class ReqSortFriendGroup extends Message
{
    private String id;
    private short sort;
    
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
        final ReqSortFriendGroup other = (ReqSortFriendGroup)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$id = this.getId();
        final Object other$id = other.getId();
        if (this$id == null) {
            if (other$id == null) {
                return this.getSort() == other.getSort();
            }
        }
        else if (this$id.equals(other$id)) {
            return this.getSort() == other.getSort();
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ReqSortFriendGroup;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * 59 + (($id == null) ? 43 : $id.hashCode());
        result = result * 59 + this.getSort();
        return result;
    }
    
    @Override
    public String toString() {
        return "ReqSortFriendGroup(id=" + this.getId() + ", sort=" + this.getSort() + ")";
    }
}
