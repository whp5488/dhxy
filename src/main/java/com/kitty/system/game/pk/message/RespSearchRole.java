
package com.kitty.system.game.pk.message;

import java.util.ArrayList;
import com.kitty.system.game.friend.message.vo.SearchFriend;
import java.util.List;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 45226)
public class RespSearchRole extends Message
{
    private List<SearchFriend> list;
    
    public RespSearchRole() {
        this.list = new ArrayList<SearchFriend>();
    }
    
    public List<SearchFriend> getList() {
        return this.list;
    }
    
    public void setList(final List<SearchFriend> list) {
        this.list = list;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespSearchRole)) {
            return false;
        }
        final RespSearchRole other = (RespSearchRole)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$list = this.getList();
        final Object other$list = other.getList();
        if (this$list == null) {
            if (other$list == null) {
                return true;
            }
        }
        else if (this$list.equals(other$list)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespSearchRole;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $list = this.getList();
        result = result * 59 + (($list == null) ? 43 : $list.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "RespSearchRole(list=" + this.getList() + ")";
    }
}
