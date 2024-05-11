
package com.kitty.system.game.suit.message;

import java.util.ArrayList;
import com.kitty.system.game.suit.message.vo.SuitEasy;
import java.util.List;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 4758)
public class RespSuit extends Message
{
    private List<SuitEasy> list;
    
    public RespSuit() {
        this.list = new ArrayList<SuitEasy>();
    }
    
    public List<SuitEasy> getList() {
        return this.list;
    }
    
    public void setList(final List<SuitEasy> list) {
        this.list = list;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespSuit)) {
            return false;
        }
        final RespSuit other = (RespSuit)o;
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
        return other instanceof RespSuit;
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
        return "RespSuit(list=" + this.getList() + ")";
    }
}
