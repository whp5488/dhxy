
package com.kitty.system.game.equip.message;

import java.beans.ConstructorProperties;
import java.util.ArrayList;
import java.util.List;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 32778)
public class RespPetEquip extends Message
{
    private List<String> list;
    
    public List<String> getList() {
        return this.list;
    }
    
    public void setList(final List<String> list) {
        this.list = list;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespPetEquip)) {
            return false;
        }
        final RespPetEquip other = (RespPetEquip)o;
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
        return other instanceof RespPetEquip;
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
        return "RespPetEquip(list=" + this.getList() + ")";
    }
    
    @ConstructorProperties({ "list" })
    public RespPetEquip(final List<String> list) {
        this.list = new ArrayList<String>();
        this.list = list;
    }
    
    public RespPetEquip() {
        this.list = new ArrayList<String>();
    }
}
