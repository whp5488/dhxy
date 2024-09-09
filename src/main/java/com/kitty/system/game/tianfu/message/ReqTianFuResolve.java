package com.kitty.system.game.tianfu.message;

import java.util.List;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 4402)
public class ReqTianFuResolve extends Message
{
    private byte type;
    private List<String> ids;
    
    public ReqTianFuResolve() {
    }
    
    public byte getType() {
        return this.type;
    }
    
    public List<String> getIds() {
        return this.ids;
    }
    
    public void setType(final byte type) {
        this.type = type;
    }
    
    public void setIds(final List<String> ids) {
        this.ids = ids;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ReqTianFuResolve)) {
            return false;
        }
        ReqTianFuResolve other = (ReqTianFuResolve)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getType() != other.getType()) {
            return false;
        }
        Object this$ids = this.getIds();
        Object other$ids = other.getIds();
        return (this$ids != null) ? this$ids.equals(other$ids) : (other$ids == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ReqTianFuResolve;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getType();
        Object $ids = this.getIds();
        result = result * 59 + (($ids == null) ? 43 : $ids.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "ReqTianFuResolve(type=" + (int)this.getType() + ", ids=" + this.getIds() + ")";
    }
}
