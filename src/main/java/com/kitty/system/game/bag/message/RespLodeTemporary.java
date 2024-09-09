package com.kitty.system.game.bag.message;

import com.kitty.system.game.bag.message.vo.TemporaryVo;
import java.util.List;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 45797)
public class RespLodeTemporary extends Message
{
    private List<TemporaryVo> list;
    
    public List<TemporaryVo> getList() {
        return this.list;
    }
    
    public void setList(final List<TemporaryVo> list) {
        this.list = list;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespLodeTemporary)) {
            return false;
        }
        RespLodeTemporary other = (RespLodeTemporary)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$list = this.getList();
        Object other$list = other.getList();
        return (this$list != null) ? this$list.equals(other$list) : (other$list == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespLodeTemporary;
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
        return "RespLodeTemporary(list=" + this.getList() + ")";
    }
    
    public RespLodeTemporary(final List<TemporaryVo> list) {
        this.list = list;
    }
}
