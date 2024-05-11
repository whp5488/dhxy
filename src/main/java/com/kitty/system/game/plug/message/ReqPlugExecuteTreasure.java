
package com.kitty.system.game.plug.message;

import java.util.List;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 45300)
public class ReqPlugExecuteTreasure extends Message
{
    private List<Integer> list;
    
    public List<Integer> getList() {
        return this.list;
    }
    
    public void setList(final List<Integer> list) {
        this.list = list;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ReqPlugExecuteTreasure)) {
            return false;
        }
        final ReqPlugExecuteTreasure other = (ReqPlugExecuteTreasure)o;
        if (!other.canEqual(this)) {
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
        return other instanceof ReqPlugExecuteTreasure;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $list = this.getList();
        result = result * 59 + (($list == null) ? 43 : $list.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "ReqPlugExecuteTreasure(list=" + this.getList() + ")";
    }
}
