
package com.kitty.system.game.bag.message;

import java.beans.ConstructorProperties;
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
        final RespLodeTemporary other = (RespLodeTemporary)o;
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
        return other instanceof RespLodeTemporary;
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
        return "RespLodeTemporary(list=" + this.getList() + ")";
    }
    
    @ConstructorProperties({ "list" })
    public RespLodeTemporary(final List<TemporaryVo> list) {
        this.list = list;
    }
}
