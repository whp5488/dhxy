
package com.kitty.system.game.equip.message;

import java.util.ArrayList;
import com.kitty.system.game.equip.message.vo.EffectInfo;
import java.util.List;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 41492)
public class RespEffect extends Message
{
    private List<EffectInfo> list;
    
    public RespEffect() {
        this.list = new ArrayList<EffectInfo>();
    }
    
    public List<EffectInfo> getList() {
        return this.list;
    }
    
    public void setList(final List<EffectInfo> list) {
        this.list = list;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespEffect)) {
            return false;
        }
        final RespEffect other = (RespEffect)o;
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
        return other instanceof RespEffect;
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
        return "RespEffect(list=" + this.getList() + ")";
    }
}
