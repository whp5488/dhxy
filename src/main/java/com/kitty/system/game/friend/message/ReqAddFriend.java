
package com.kitty.system.game.friend.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 8294)
public class ReqAddFriend extends Message
{
    private int roleId;
    private String groupId;
    private String remark;
    
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
        final ReqAddFriend other = (ReqAddFriend)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getRoleId() != other.getRoleId()) {
            return false;
        }
        final Object this$groupId = this.getGroupId();
        final Object other$groupId = other.getGroupId();
        Label_0078: {
            if (this$groupId == null) {
                if (other$groupId == null) {
                    break Label_0078;
                }
            }
            else if (this$groupId.equals(other$groupId)) {
                break Label_0078;
            }
            return false;
        }
        final Object this$remark = this.getRemark();
        final Object other$remark = other.getRemark();
        if (this$remark == null) {
            if (other$remark == null) {
                return true;
            }
        }
        else if (this$remark.equals(other$remark)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ReqAddFriend;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getRoleId();
        final Object $groupId = this.getGroupId();
        result = result * 59 + (($groupId == null) ? 43 : $groupId.hashCode());
        final Object $remark = this.getRemark();
        result = result * 59 + (($remark == null) ? 43 : $remark.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "ReqAddFriend(roleId=" + this.getRoleId() + ", groupId=" + this.getGroupId() + ", remark=" + this.getRemark() + ")";
    }
}
