
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
        final RespUpdateTeamList other = (RespUpdateTeamList)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$target = this.getTarget();
        final Object other$target = other.getTarget();
        Label_0065: {
            if (this$target == null) {
                if (other$target == null) {
                    break Label_0065;
                }
            }
            else if (this$target.equals(other$target)) {
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
        return other instanceof RespUpdateTeamList;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $target = this.getTarget();
        result = result * 59 + (($target == null) ? 43 : $target.hashCode());
        final Object $list = this.getList();
        result = result * 59 + (($list == null) ? 43 : $list.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "RespUpdateTeamList(target=" + this.getTarget() + ", list=" + this.getList() + ")";
    }
}
