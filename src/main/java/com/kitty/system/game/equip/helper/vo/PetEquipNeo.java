
package com.kitty.system.game.equip.helper.vo;

import java.util.Iterator;
import com.kitty.system.game.vo.FiedEquip;
import java.util.ArrayList;
import com.kitty.system.game.vo.FieldProp;
import java.util.List;
import java.io.Serializable;

public class PetEquipNeo implements Serializable
{
    private String id;
    private int serial;
    private List<FieldProp> list;
    private byte productOrder;
    
    public PetEquipNeo() {
        this.serial = 0;
        this.list = new ArrayList<FieldProp>();
        this.productOrder = 0;
    }
    
    public PetEquipNeo(final long id, final int serial, final byte productOrder, final List<FiedEquip> list) {
        this.serial = 0;
        this.list = new ArrayList<FieldProp>();
        this.productOrder = 0;
        this.id = String.valueOf(id);
        this.serial = serial;
        this.productOrder = productOrder;
        for (final FiedEquip fiedEquip : list) {
            this.list.add(new FieldProp(fiedEquip.getType(), fiedEquip.getValue()));
        }
    }
    
    public String getId() {
        return this.id;
    }
    
    public int getSerial() {
        return this.serial;
    }
    
    public List<FieldProp> getList() {
        return this.list;
    }
    
    public byte getProductOrder() {
        return this.productOrder;
    }
    
    public void setId(final String id) {
        this.id = id;
    }
    
    public void setSerial(final int serial) {
        this.serial = serial;
    }
    
    public void setList(final List<FieldProp> list) {
        this.list = list;
    }
    
    public void setProductOrder(final byte productOrder) {
        this.productOrder = productOrder;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof PetEquipNeo)) {
            return false;
        }
        final PetEquipNeo other = (PetEquipNeo)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$id = this.getId();
        final Object other$id = other.getId();
        Label_0065: {
            if (this$id == null) {
                if (other$id == null) {
                    break Label_0065;
                }
            }
            else if (this$id.equals(other$id)) {
                break Label_0065;
            }
            return false;
        }
        if (this.getSerial() != other.getSerial()) {
            return false;
        }
        final Object this$list = this.getList();
        final Object other$list = other.getList();
        if (this$list == null) {
            if (other$list == null) {
                return this.getProductOrder() == other.getProductOrder();
            }
        }
        else if (this$list.equals(other$list)) {
            return this.getProductOrder() == other.getProductOrder();
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof PetEquipNeo;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * 59 + (($id == null) ? 43 : $id.hashCode());
        result = result * 59 + this.getSerial();
        final Object $list = this.getList();
        result = result * 59 + (($list == null) ? 43 : $list.hashCode());
        result = result * 59 + this.getProductOrder();
        return result;
    }
    
    @Override
    public String toString() {
        return "PetEquipNeo(id=" + this.getId() + ", serial=" + this.getSerial() + ", list=" + this.getList() + ", productOrder=" + this.getProductOrder() + ")";
    }
}
