package com.kitty.system.game.friend.message.vo;

import com.kitty.system.game.friend.domain.PalFriendGroup;

public class FriendGroup
{
    private String serial;
    private String groupName;
    private short sort;
    
    public FriendGroup() {
    }
    
    public FriendGroup(PalFriendGroup friendGroup) {
        this.serial = String.valueOf(friendGroup.getId());
        this.groupName = friendGroup.getGroupName();
        this.sort = friendGroup.getSort();
    }
    
    public FriendGroup(String serial, String groupName, short sort) {
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
        FriendGroup other = (FriendGroup)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$serial = this.getSerial();
        Object other$serial = other.getSerial();
        if (!((this$serial != null) ? this$serial.equals(other$serial) : (other$serial == null))) {
            return false;
        }
        Object this$groupName = this.getGroupName();
        Object other$groupName = other.getGroupName();
        return ((this$groupName != null) ? this$groupName.equals(other$groupName) : (other$groupName == null)) && this.getSort() == other.getSort();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof FriendGroup;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $serial = this.getSerial();
        result = result * 59 + (($serial == null) ? 43 : $serial.hashCode());
        Object $groupName = this.getGroupName();
        result = result * 59 + (($groupName == null) ? 43 : $groupName.hashCode());
        result = result * 59 + this.getSort();
        return result;
    }
    
    @Override
    public String toString() {
        return "FriendGroup(serial=" + this.getSerial() + ", groupName=" + this.getGroupName() + ", sort=" + (int)this.getSort() + ")";
    }
}
