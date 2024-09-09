package com.kitty.system.game.horse.message;

import com.kitty.system.game.horse.message.vo.Mount;
import java.util.List;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 41041)
public class RespMount extends Message
{
    private List<Mount> list;
    
    public List<Mount> getList() {
        return this.list;
    }
    
    public void setList(final List<Mount> list) {
        this.list = list;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespMount)) {
            return false;
        }
        RespMount other = (RespMount)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$list = this.getList();
        Object other$list = other.getList();
        return (this$list != null) ? this$list.equals(other$list) : (other$list == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespMount;
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
        return "RespMount(list=" + this.getList() + ")";
    }
    
    public RespMount(final List<Mount> list) {
        this.list = list;
    }
    
    public RespMount() {
    }
}
