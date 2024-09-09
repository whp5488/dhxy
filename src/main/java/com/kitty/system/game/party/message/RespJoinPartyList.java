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
        RespJoinPartyList other = (RespJoinPartyList)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$type = this.getType();
        Object other$type = other.getType();
        if (!((this$type != null) ? this$type.equals(other$type) : (other$type == null))) {
            return false;
        }
        Object this$list = this.getList();
        Object other$list = other.getList();
        return (this$list != null) ? this$list.equals(other$list) : (other$list == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespJoinPartyList;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $type = this.getType();
        result = result * 59 + (($type == null) ? 43 : $type.hashCode());
        Object $list = this.getList();
        result = result * 59 + (($list == null) ? 43 : $list.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "RespJoinPartyList(type=" + this.getType() + ", list=" + this.getList() + ")";
    }
}
