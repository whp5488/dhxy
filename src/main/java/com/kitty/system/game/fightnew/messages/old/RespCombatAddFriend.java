package com.kitty.system.game.fightnew.messages.old;

import com.kitty.system.game.fightnew.messages.vo.CombatAddObject;
import java.util.List;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 64995)
public class RespCombatAddFriend extends Message
{
    private List<CombatAddObject> list;
    
    public RespCombatAddFriend() {
    }
    
    public List<CombatAddObject> getList() {
        return this.list;
    }
    
    public void setList(final List<CombatAddObject> list) {
        this.list = list;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespCombatAddFriend)) {
            return false;
        }
        RespCombatAddFriend other = (RespCombatAddFriend)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$list = this.getList();
        Object other$list = other.getList();
        return (this$list != null) ? this$list.equals(other$list) : (other$list == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespCombatAddFriend;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $list = this.getList();
        result = result * 59 + (($list == null) ? 43 : $list.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "RespCombatAddFriend(list=" + this.getList() + ")";
    }
}
