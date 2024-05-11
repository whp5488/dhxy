
package com.kitty.system.game.friend.message.vo;

import com.kitty.system.game.friend.domain.PalFriendGroup;

public class FriendGroup
{
    private String serial;
    private String groupName;
    private short sort;
    
    public FriendGroup() {
    }
    
    public FriendGroup(final PalFriendGroup friendGroup) {
        this.serial = String.valueOf(friendGroup.getId());
        this.groupName = friendGroup.getGroupName();
        this.sort = friendGroup.getSort();
    }
    
    public FriendGroup(final String serial, final String groupName, final short sort) {
        this.serial = serial;
        this.groupName = groupName;
        this.sort = sort;
    }
    
    public String getSerial() {
        return this.serial;
    }
    
    public String getGroupName() {
        return this.groupName;
    }
    
    public short getSort() {
        return this.sort;
    }
    
    public void setSerial(final String serial) {
        this.serial = serial;
    }
    
    public void setGroupName(final String groupName) {
        this.groupName = groupName;
    }
    
    public void setSort(final short sort) {
        this.sort = sort;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof FriendGroup)) {
            return false;
        }
        final FriendGroup other = (FriendGroup)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$serial = this.getSerial();
        final Object other$serial = other.getSerial();
        Label_0065: {
            if (this$serial == null) {
                if (other$serial == null) {
                    break Label_0065;
                }
            }
            else if (this$serial.equals(other$serial)) {
                break Label_0065;
            }
            return false;
        }
        final Object this$groupName = this.getGroupName();
        final Object other$groupName = other.getGroupName();
        if (this$groupName == null) {
            if (other$groupName == null) {
                return this.getSort() == other.getSort();
            }
        }
        else if (this$groupName.equals(other$groupName)) {
            return this.getSort() == other.getSort();
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof FriendGroup;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $serial = this.getSerial();
        result = result * 59 + (($serial == null) ? 43 : $serial.hashCode());
        final Object $groupName = this.getGroupName();
        result = result * 59 + (($groupName == null) ? 43 : $groupName.hashCode());
        result = result * 59 + this.getSort();
        return result;
    }
    
    @Override
    public String toString() {
        return "FriendGroup(serial=" + this.getSerial() + ", groupName=" + this.getGroupName() + ", sort=" + this.getSort() + ")";
    }
}
