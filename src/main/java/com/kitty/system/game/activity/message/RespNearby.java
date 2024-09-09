package com.kitty.system.game.activity.message;

import com.kitty.system.game.activity.message.vo.Nearby;
import java.util.List;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 20466)
public class RespNearby extends Message
{
    public List<Nearby> list;
    
    public List<Nearby> getList() {
        return this.list;
    }
    
    public void setList(final List<Nearby> list) {
        this.list = list;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespNearby)) {
            return false;
        }
        RespNearby other = (RespNearby)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$list = this.getList();
        Object other$list = other.getList();
        return (this$list != null) ? this$list.equals(other$list) : (other$list == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespNearby;
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
        return "RespNearby(list=" + this.getList() + ")";
    }
    
    public RespNearby() {
    }
    
    public RespNearby(final List<Nearby> list) {
        this.list = list;
    }
}
