
package com.kitty.system.game.friend.message;

import java.util.ArrayList;
import java.util.List;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 45178)
public class ReqAddFriendGroup extends Message
{
    private String groupName;
    private List<Integer> list;
    
    public ReqAddFriendGroup() {
        this.list = new ArrayList<Integer>();
    }
    
    public String getGroupName() {
        return this.groupName;
    }
    
    public List<Integer> getList() {
        return this.list;
    }
    
    public void setGroupName(final String groupName) {
        this.groupName = groupName;
    }
    
    public void setList(final List<Integer> list) {
        this.list = list;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ReqAddFriendGroup)) {
            return false;
        }
        final ReqAddFriendGroup other = (ReqAddFriendGroup)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$groupName = this.getGroupName();
        final Object other$groupName = other.getGroupName();
        Label_0065: {
            if (this$groupName == null) {
                if (other$groupName == null) {
                    break Label_0065;
                }
            }
            else if (this$groupName.equals(other$groupName)) {
                break Label_0065;
            }
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
        return other instanceof ReqAddFriendGroup;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $groupName = this.getGroupName();
        result = result * 59 + (($groupName == null) ? 43 : $groupName.hashCode());
        final Object $list = this.getList();
        result = result * 59 + (($list == null) ? 43 : $list.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "ReqAddFriendGroup(groupName=" + this.getGroupName() + ", list=" + this.getList() + ")";
    }
}
