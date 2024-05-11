
package com.kitty.system.game.partner.message;

import java.beans.ConstructorProperties;
import com.kitty.system.common.enter.FiedValue;
import java.util.List;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 65526)
public class RespUpdatePartner extends Message
{
    private int id;
    private List<FiedValue> list;
    
    public int getId() {
        return this.id;
    }
    
    public List<FiedValue> getList() {
        return this.list;
    }
    
    public void setId(final int id) {
        this.id = id;
    }
    
    public void setList(final List<FiedValue> list) {
        this.list = list;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespUpdatePartner)) {
            return false;
        }
        final RespUpdatePartner other = (RespUpdatePartner)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getId() != other.getId()) {
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
        return other instanceof RespUpdatePartner;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getId();
        final Object $list = this.getList();
        result = result * 59 + (($list == null) ? 43 : $list.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "RespUpdatePartner(id=" + this.getId() + ", list=" + this.getList() + ")";
    }
    
    public RespUpdatePartner() {
    }
    
    @ConstructorProperties({ "id", "list" })
    public RespUpdatePartner(final int id, final List<FiedValue> list) {
        this.id = id;
        this.list = list;
    }
}
