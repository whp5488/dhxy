
package com.kitty.system.game.equip.helper.vo;

import java.util.Iterator;
import com.kitty.system.game.vo.FiedEquip;
import java.util.ArrayList;
import com.kitty.system.game.vo.FieldProp;
import java.util.List;
import java.io.Serializable;

public class EquipNeo implements Serializable
{
    List<FieldProp> list;
    
    public EquipNeo() {
        this.list = new ArrayList<FieldProp>();
    }
    
    public EquipNeo(final List<FiedEquip> list) {
        this.list = new ArrayList<FieldProp>();
        for (final FiedEquip fiedEquip : list) {
            if (!fiedEquip.getType().equals(6681)) {
                this.list.add(new FieldProp(fiedEquip.getType(), fiedEquip.getValue()));
            }
        }
    }
    
    public List<FieldProp> getList() {
        return this.list;
    }
    
    public void setList(final List<FieldProp> list) {
        this.list = list;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof EquipNeo)) {
            return false;
        }
        final EquipNeo other = (EquipNeo)o;
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
        return other instanceof EquipNeo;
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
        return "EquipNeo(list=" + this.getList() + ")";
    }
}
