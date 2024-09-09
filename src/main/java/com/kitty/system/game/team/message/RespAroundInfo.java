package com.kitty.system.game.team.message;

import com.kitty.system.common.enter.FiedValue;
import java.util.List;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 20467)
public class RespAroundInfo extends Message
{
    private int roleId;
    private String inviteName;
    private String type;
    private int serial;
    private int teamMembersCount;
    private String time;
    private List<FiedValue> list;
    
    public RespAroundInfo() {
        this.teamMembersCount = 1;
    }
    
    public int getRoleId() {
        return this.roleId;
    }
    
    public String getInviteName() {
        return this.inviteName;
    }
    
    public String getType() {
        return this.type;
    }
    
    public int getSerial() {
        return this.serial;
    }
    
    public int getTeamMembersCount() {
        return this.teamMembersCount;
    }
    
    public String getTime() {
        return this.time;
    }
    
    public List<FiedValue> getList() {
        return this.list;
    }
    
    public void setRoleId(final int roleId) {
        this.roleId = roleId;
    }
    
    public void setInviteName(final String inviteName) {
        this.inviteName = inviteName;
    }
    
    public void setType(final String type) {
        this.type = type;
    }
    
    public void setSerial(final int serial) {
        this.serial = serial;
    }
    
    public void setTeamMembersCount(final int teamMembersCount) {
        this.teamMembersCount = teamMembersCount;
    }
    
    public void setTime(final String time) {
        this.time = time;
    }
    
    public void setList(final List<FiedValue> list) {
        this.list = list;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespAroundInfo)) {
            return false;
        }
        RespAroundInfo other = (RespAroundInfo)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getRoleId() != other.getRoleId()) {
            return false;
        }
        Object this$inviteName = this.getInviteName();
        Object other$inviteName = other.getInviteName();
        if (!((this$inviteName != null) ? this$inviteName.equals(other$inviteName) : (other$inviteName == null))) {
            return false;
        }
        Object this$type = this.getType();
        Object other$type = other.getType();
        if (!((this$type != null) ? this$type.equals(other$type) : (other$type == null))) {
            return false;
        }
        if (this.getSerial() != other.getSerial()) {
            return false;
        }
        if (this.getTeamMembersCount() != other.getTeamMembersCount()) {
            return false;
        }
        Object this$time = this.getTime();
        Object other$time = other.getTime();
        if (!((this$time != null) ? this$time.equals(other$time) : (other$time == null))) {
            return false;
        }
        Object this$list = this.getList();
        Object other$list = other.getList();
        return (this$list != null) ? this$list.equals(other$list) : (other$list == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespAroundInfo;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getRoleId();
        Object $inviteName = this.getInviteName();
        result = result * 59 + (($inviteName == null) ? 43 : $inviteName.hashCode());
        Object $type = this.getType();
        result = result * 59 + (($type == null) ? 43 : $type.hashCode());
        result = result * 59 + this.getSerial();
        result = result * 59 + this.getTeamMembersCount();
        Object $time = this.getTime();
        result = result * 59 + (($time == null) ? 43 : $time.hashCode());
        Object $list = this.getList();
        result = result * 59 + (($list == null) ? 43 : $list.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "RespAroundInfo(roleId=" + this.getRoleId() + ", inviteName=" + this.getInviteName() + ", type=" + this.getType() + ", serial=" + this.getSerial() + ", teamMembersCount=" + this.getTeamMembersCount() + ", time=" + this.getTime() + ", list=" + this.getList() + ")";
    }
}
