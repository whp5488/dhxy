package com.kitty.system.game.friend.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 8294)
public class ReqAddFriend extends Message
{
    private int roleId;
    private String groupId;
    private String remark;
    
    public ReqAddFriend() {
    }
    
    public int getRoleId() {
        return this.roleId;
    }
    
    public String getGroupId() {
        return this.groupId;
    }
    
    public String getRemark() {
        return this.remark;
    }
    
    public void setRoleId(final int roleId) {
        this.roleId = roleId;
    }
    
    public void setGroupId(final String groupId) {
        this.groupId = groupId;
    }
    
    public void setRemark(final String remark) {
        this.remark = remark;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ReqAddFriend)) {
            return false;
        }
        ReqAddFriend other = (ReqAddFriend)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getRoleId() != other.getRoleId()) {
            return false;
        }
        Object this$groupId = this.getGroupId();
        Object other$groupId = other.getGroupId();
        if (!((this$groupId != null) ? this$groupId.equals(other$groupId) : (other$groupId == null))) {
            return false;
        }
        Object this$remark = this.getRemark();
        Object other$remark = other.getRemark();
        return (this$remark != null) ? this$remark.equals(other$remark) : (other$remark == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ReqAddFriend;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getRoleId();
        Object $groupId = this.getGroupId();
        result = result * 59 + (($groupId == null) ? 43 : $groupId.hashCode());
        Object $remark = this.getRemark();
        result = result * 59 + (($remark == null) ? 43 : $remark.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "ReqAddFriend(roleId=" + this.getRoleId() + ", groupId=" + this.getGroupId() + ", remark=" + this.getRemark() + ")";
    }
}
