
package com.kitty.system.game.equip.domain;

import java.util.Iterator;
import com.kitty.system.game.vo.FiedEquip;
import java.util.ArrayList;
import com.kitty.system.game.vo.FieldProp;
import java.util.List;

public class PetEquipEasy
{
    private String id;
    private String petId;
    private Integer bd;
    private Integer type;
    private List<FieldProp> field;
    private short positionNum;
    private byte productOrder;
    private byte lock;
    
    public PetEquipEasy() {
        this.bd = 0;
        this.field = new ArrayList<FieldProp>();
        this.productOrder = 0;
        this.lock = 0;
    }
    
    public PetEquipEasy(final PetEquip equip) {
        this.bd = 0;
        this.field = new ArrayList<FieldProp>();
        this.productOrder = 0;
        this.lock = 0;
        this.id = String.valueOf(equip.getId());
        this.petId = String.valueOf(equip.getPId());
        this.bd = equip.getBd();
        this.type = equip.getSerial();
        this.productOrder = equip.getProductOrder();
        this.positionNum = equip.getPositionNum();
        this.lock = equip.getLock();
        for (final FiedEquip fiedEquip : equip.getField()) {
            this.field.add(new FieldProp(fiedEquip.getType(), fiedEquip.getValue()));
        }
    }
    
    public String getId() {
        return this.id;
    }
    
    public String getPetId() {
        return this.petId;
    }
    
    public Integer getBd() {
        return this.bd;
    }
    
    public Integer getType() {
        return this.type;
    }
    
    public List<FieldProp> getField() {
        return this.field;
    }
    
    public short getPositionNum() {
        return this.positionNum;
    }
    
    public byte getProductOrder() {
        return this.productOrder;
    }
    
    public byte getLock() {
        return this.lock;
    }
    
    public void setId(final String id) {
        this.id = id;
    }
    
    public void setPetId(final String petId) {
        this.petId = petId;
    }
    
    public void setBd(final Integer bd) {
        this.bd = bd;
    }
    
    public void setType(final Integer type) {
        this.type = type;
    }
    
    public void setField(final List<FieldProp> field) {
        this.field = field;
    }
    
    public void setPositionNum(final short positionNum) {
        this.positionNum = positionNum;
    }
    
    public void setProductOrder(final byte productOrder) {
        this.productOrder = productOrder;
    }
    
    public void setLock(final byte lock) {
        this.lock = lock;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof PetEquipEasy)) {
            return false;
        }
        final PetEquipEasy other = (PetEquipEasy)o;
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
        final Object this$petId = this.getPetId();
        final Object other$petId = other.getPetId();
        Label_0102: {
            if (this$petId == null) {
                if (other$petId == null) {
                    break Label_0102;
                }
            }
            else if (this$petId.equals(other$petId)) {
                break Label_0102;
            }
            return false;
        }
        final Object this$bd = this.getBd();
        final Object other$bd = other.getBd();
        Label_0139: {
            if (this$bd == null) {
                if (other$bd == null) {
                    break Label_0139;
                }
            }
            else if (this$bd.equals(other$bd)) {
                break Label_0139;
            }
            return false;
        }
        final Object this$type = this.getType();
        final Object other$type = other.getType();
        Label_0176: {
            if (this$type == null) {
                if (other$type == null) {
                    break Label_0176;
                }
            }
            else if (this$type.equals(other$type)) {
                break Label_0176;
            }
            return false;
        }
        final Object this$field = this.getField();
        final Object other$field = other.getField();
        if (this$field == null) {
            if (other$field == null) {
                return this.getPositionNum() == other.getPositionNum() && this.getProductOrder() == other.getProductOrder() && this.getLock() == other.getLock();
            }
        }
        else if (this$field.equals(other$field)) {
            return this.getPositionNum() == other.getPositionNum() && this.getProductOrder() == other.getProductOrder() && this.getLock() == other.getLock();
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof PetEquipEasy;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * 59 + (($id == null) ? 43 : $id.hashCode());
        final Object $petId = this.getPetId();
        result = result * 59 + (($petId == null) ? 43 : $petId.hashCode());
        final Object $bd = this.getBd();
        result = result * 59 + (($bd == null) ? 43 : $bd.hashCode());
        final Object $type = this.getType();
        result = result * 59 + (($type == null) ? 43 : $type.hashCode());
        final Object $field = this.getField();
        result = result * 59 + (($field == null) ? 43 : $field.hashCode());
        result = result * 59 + this.getPositionNum();
        result = result * 59 + this.getProductOrder();
        result = result * 59 + this.getLock();
        return result;
    }
    
    @Override
    public String toString() {
        return "PetEquipEasy(id=" + this.getId() + ", petId=" + this.getPetId() + ", bd=" + this.getBd() + ", type=" + this.getType() + ", field=" + this.getField() + ", positionNum=" + this.getPositionNum() + ", productOrder=" + this.getProductOrder() + ", lock=" + this.getLock() + ")";
    }
}
