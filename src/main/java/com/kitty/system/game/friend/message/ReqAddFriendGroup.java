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
        this.list = new ArrayList<>();
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
        ReqAddFriendGroup other = (ReqAddFriendGroup)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$groupName = this.getGroupName();
        Object other$groupName = other.getGroupName();
        if (!((this$groupName != null) ? this$groupName.equals(other$groupName) : (other$groupName == null))) {
            return false;
        }
        Object this$list = this.getList();
        Object other$list = other.getList();
        return (this$list != null) ? this$list.equals(other$list) : (other$list == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ReqAddFriendGroup;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $groupName = this.getGroupName();
        result = result * 59 + (($groupName == null) ? 43 : $groupName.hashCode());
        Object $list = this.getList();
        result = result * 59 + (($list == null) ? 43 : $list.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "ReqAddFriendGroup(groupName=" + this.getGroupName() + ", list=" + this.getList() + ")";
    }
}
