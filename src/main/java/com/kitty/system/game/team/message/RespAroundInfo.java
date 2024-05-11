
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
        final RespAroundInfo other = (RespAroundInfo)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getRoleId() != other.getRoleId()) {
            return false;
        }
        final Object this$inviteName = this.getInviteName();
        final Object other$inviteName = other.getInviteName();
        Label_0078: {
            if (this$inviteName == null) {
                if (other$inviteName == null) {
                    break Label_0078;
                }
            }
            else if (this$inviteName.equals(other$inviteName)) {
                break Label_0078;
            }
            return false;
        }
        final Object this$type = this.getType();
        final Object other$type = other.getType();
        Label_0115: {
            if (this$type == null) {
                if (other$type == null) {
                    break Label_0115;
                }
            }
            else if (this$type.equals(other$type)) {
                break Label_0115;
            }
            return false;
        }
        if (this.getSerial() != other.getSerial()) {
            return false;
        }
        if (this.getTeamMembersCount() != other.getTeamMembersCount()) {
            return false;
        }
        final Object this$time = this.getTime();
        final Object other$time = other.getTime();
        Label_0178: {
            if (this$time == null) {
                if (other$time == null) {
                    break Label_0178;
                }
            }
            else if (this$time.equals(other$time)) {
                break Label_0178;
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
        return other instanceof RespAroundInfo;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getRoleId();
        final Object $inviteName = this.getInviteName();
        result = result * 59 + (($inviteName == null) ? 43 : $inviteName.hashCode());
        final Object $type = this.getType();
        result = result * 59 + (($type == null) ? 43 : $type.hashCode());
        result = result * 59 + this.getSerial();
        result = result * 59 + this.getTeamMembersCount();
        final Object $time = this.getTime();
        result = result * 59 + (($time == null) ? 43 : $time.hashCode());
        final Object $list = this.getList();
        result = result * 59 + (($list == null) ? 43 : $list.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "RespAroundInfo(roleId=" + this.getRoleId() + ", inviteName=" + this.getInviteName() + ", type=" + this.getType() + ", serial=" + this.getSerial() + ", teamMembersCount=" + this.getTeamMembersCount() + ", time=" + this.getTime() + ", list=" + this.getList() + ")";
    }
}
