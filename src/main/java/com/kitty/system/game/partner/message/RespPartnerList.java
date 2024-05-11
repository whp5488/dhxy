
package com.kitty.system.game.partner.message;

import java.beans.ConstructorProperties;
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
        final RespPartnerList other = (RespPartnerList)o;
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
        return other instanceof RespPartnerList;
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
        return "RespPartnerList(list=" + this.getList() + ")";
    }
    
    public RespPartnerList() {
    }
    
    @ConstructorProperties({ "list" })
    public RespPartnerList(final List<PartnerFiedValue> list) {
        this.list = list;
    }
}
