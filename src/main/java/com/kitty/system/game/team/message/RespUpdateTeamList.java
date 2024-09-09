package com.kitty.system.game.team.message;

import com.kitty.system.game.team.domain.TeamMember;
import java.util.List;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 4119)
public class RespUpdateTeamList extends Message
{
    private String target;
    private List<TeamMember> list;
    
    public RespUpdateTeamList() {
    }
    
    public String getTarget() {
        return this.target;
    }
    
    public List<TeamMember> getList() {
        return this.list;
    }
    
    public void setTarget(final String target) {
        this.target = target;
    }
    
    public void setList(final List<TeamMember> list) {
        this.list = list;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespUpdateTeamList)) {
            return false;
        }
        RespUpdateTeamList other = (RespUpdateTeamList)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$target = this.getTarget();
        Object other$target = other.getTarget();
        if (!((this$target != null) ? this$target.equals(other$target) : (other$target == null))) {
            return false;
        }
        Object this$list = this.getList();
        Object other$list = other.getList();
        return (this$list != null) ? this$list.equals(other$list) : (other$list == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespUpdateTeamList;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $target = this.getTarget();
        result = result * 59 + (($target == null) ? 43 : $target.hashCode());
        Object $list = this.getList();
        result = result * 59 + (($list == null) ? 43 : $list.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "RespUpdateTeamList(target=" + this.getTarget() + ", list=" + this.getList() + ")";
    }
}
