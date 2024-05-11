
package com.kitty.system.game.party.message;

import com.kitty.system.game.party.model.JoinMemberInfo;
import java.util.List;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 20468)
public class RespJoinPartyList extends Message
{
    private String type;
    private List<JoinMemberInfo> list;
    
    public RespJoinPartyList() {
        this.type = "party";
    }
    
    public String getType() {
        return this.type;
    }
    
    public List<JoinMemberInfo> getList() {
        return this.list;
    }
    
    public void setType(final String type) {
        this.type = type;
    }
    
    public void setList(final List<JoinMemberInfo> list) {
        this.list = list;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespJoinPartyList)) {
            return false;
        }
        final RespJoinPartyList other = (RespJoinPartyList)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$type = this.getType();
        final Object other$type = other.getType();
        Label_0065: {
            if (this$type == null) {
                if (other$type == null) {
                    break Label_0065;
                }
            }
            else if (this$type.equals(other$type)) {
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
        return other instanceof RespJoinPartyList;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $type = this.getType();
        result = result * 59 + (($type == null) ? 43 : $type.hashCode());
        final Object $list = this.getList();
        result = result * 59 + (($list == null) ? 43 : $list.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "RespJoinPartyList(type=" + this.getType() + ", list=" + this.getList() + ")";
    }
}
