package com.kitty.system.game.partner.message;

import com.kitty.system.game.partner.enter.PartnerFiedValue;
import java.util.List;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 42100)
public class RespPartnerList extends Message
{
    private List<PartnerFiedValue> list;
    
    public List<PartnerFiedValue> getList() {
        return this.list;
    }
    
    public void setList(final List<PartnerFiedValue> list) {
        this.list = list;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespPartnerList)) {
            return false;
        }
        RespPartnerList other = (RespPartnerList)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$list = this.getList();
        Object other$list = other.getList();
        return (this$list != null) ? this$list.equals(other$list) : (other$list == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespPartnerList;
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
        return "RespPartnerList(list=" + this.getList() + ")";
    }
    
    public RespPartnerList() {
    }
    
    public RespPartnerList(final List<PartnerFiedValue> list) {
        this.list = list;
    }
}
