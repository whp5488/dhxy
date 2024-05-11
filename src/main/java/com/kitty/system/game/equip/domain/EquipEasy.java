
package com.kitty.system.game.equip.domain;

import com.kitty.system.game.store.domain.StoreEquip;
import java.util.Iterator;
import com.kitty.system.game.vo.FiedEquip;
import com.kitty.system.game.bag.domain.BagEquip;
import java.util.ArrayList;
import com.kitty.system.game.vo.FieldProp;
import java.util.List;

public class EquipEasy
{
    private String id;
    private Integer roleId;
    private Integer bd;
    private Integer type;
    private Integer pos;
    private Integer position;
    private List<FieldProp> field;
    private List<FieldProp> artifice;
    private byte productOrder;
    
    public EquipEasy() {
        this.bd = 0;
        this.field = new ArrayList<FieldProp>();
        this.artifice = new ArrayList<FieldProp>();
        this.productOrder = 0;
    }
    
    public EquipEasy(final BagEquip equip, final int roleId) {
        this.bd = 0;
        this.field = new ArrayList<FieldProp>();
        this.artifice = new ArrayList<FieldProp>();
        this.productOrder = 0;
        this.id = String.valueOf(equip.getId());
        this.roleId = roleId;
        this.bd = equip.getBd();
        this.type = equip.getType();
        this.pos = equip.getPos();
        this.position = equip.getPosition();
        this.productOrder = equip.getProductOrder();
        for (final FiedEquip fiedEquip : equip.getField()) {
            this.field.add(new FieldProp(fiedEquip.getType(), fiedEquip.getValue()));
        }
        for (final FiedEquip fiedEquip : equip.getArtifice()) {
            this.artifice.add(new FieldProp(fiedEquip.getType(), fiedEquip.getValue()));
        }
    }
    
    public EquipEasy(final WearEquip equip, final int roleId) {
        this.bd = 0;
        this.field = new ArrayList<FieldProp>();
        this.artifice = new ArrayList<FieldProp>();
        this.productOrder = 0;
        this.id = String.valueOf(equip.getId());
        this.roleId = roleId;
        this.bd = equip.getBd();
        this.type = equip.getType();
        this.pos = equip.getPos();
        this.position = equip.getPosition();
        this.productOrder = equip.getProductOrder();
        for (final FiedEquip fiedEquip : equip.getField()) {
            this.field.add(new FieldProp(fiedEquip.getType(), fiedEquip.getValue()));
        }
        for (final FiedEquip fiedEquip : equip.getArtifice()) {
            this.artifice.add(new FieldProp(fiedEquip.getType(), fiedEquip.getValue()));
        }
    }
    
    public EquipEasy(final StoreEquip equip, final int roleId) {
        this.bd = 0;
        this.field = new ArrayList<FieldProp>();
        this.artifice = new ArrayList<FieldProp>();
        this.productOrder = 0;
        this.id = String.valueOf(equip.getId());
        this.roleId = roleId;
        this.bd = equip.getBd();
        this.type = equip.getType();
        this.pos = equip.getPos();
        this.position = equip.getPosition();
        this.productOrder = equip.getProductOrder();
        for (final FiedEquip fiedEquip : equip.getField()) {
            this.field.add(new FieldProp(fiedEquip.getType(), fiedEquip.getValue()));
        }
        for (final FiedEquip fiedEquip : equip.getArtifice()) {
            this.artifice.add(new FieldProp(fiedEquip.getType(), fiedEquip.getValue()));
        }
    }
    
    public String getId() {
        return this.id;
    }
    
    public Integer getRoleId() {
        return this.roleId;
    }
    
    public Integer getBd() {
        return this.bd;
    }
    
    public Integer getType() {
        return this.type;
    }
    
    public Integer getPos() {
        return this.pos;
    }
    
    public Integer getPosition() {
        return this.position;
    }
    
    public List<FieldProp> getField() {
        return this.field;
    }
    
    public List<FieldProp> getArtifice() {
        return this.artifice;
    }
    
    public byte getProductOrder() {
        return this.productOrder;
    }
    
    public void setId(final String id) {
        this.id = id;
    }
    
    public void setRoleId(final Integer roleId) {
        this.roleId = roleId;
    }
    
    public void setBd(final Integer bd) {
        this.bd = bd;
    }
    
    public void setType(final Integer type) {
        this.type = type;
    }
    
    public void setPos(final Integer pos) {
        this.pos = pos;
    }
    
    public void setPosition(final Integer position) {
        this.position = position;
    }
    
    public void setField(final List<FieldProp> field) {
        this.field = field;
    }
    
    public void setArtifice(final List<FieldProp> artifice) {
        this.artifice = artifice;
    }
    
    public void setProductOrder(final byte productOrder) {
        this.productOrder = productOrder;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof EquipEasy)) {
            return false;
        }
        final EquipEasy other = (EquipEasy)o;
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
        final Object this$roleId = this.getRoleId();
        final Object other$roleId = other.getRoleId();
        Label_0102: {
            if (this$roleId == null) {
                if (other$roleId == null) {
                    break Label_0102;
                }
            }
            else if (this$roleId.equals(other$roleId)) {
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
        final Object this$pos = this.getPos();
        final Object other$pos = other.getPos();
        Label_0213: {
            if (this$pos == null) {
                if (other$pos == null) {
                    break Label_0213;
                }
            }
            else if (this$pos.equals(other$pos)) {
                break Label_0213;
            }
            return false;
        }
        final Object this$position = this.getPosition();
        final Object other$position = other.getPosition();
        Label_0250: {
            if (this$position == null) {
                if (other$position == null) {
                    break Label_0250;
                }
            }
            else if (this$position.equals(other$position)) {
                break Label_0250;
            }
            return false;
        }
        final Object this$field = this.getField();
        final Object other$field = other.getField();
        Label_0287: {
            if (this$field == null) {
                if (other$field == null) {
                    break Label_0287;
                }
            }
            else if (this$field.equals(other$field)) {
                break Label_0287;
            }
            return false;
        }
        final Object this$artifice = this.getArtifice();
        final Object other$artifice = other.getArtifice();
        if (this$artifice == null) {
            if (other$artifice == null) {
                return this.getProductOrder() == other.getProductOrder();
            }
        }
        else if (this$artifice.equals(other$artifice)) {
            return this.getProductOrder() == other.getProductOrder();
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof EquipEasy;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * 59 + (($id == null) ? 43 : $id.hashCode());
        final Object $roleId = this.getRoleId();
        result = result * 59 + (($roleId == null) ? 43 : $roleId.hashCode());
        final Object $bd = this.getBd();
        result = result * 59 + (($bd == null) ? 43 : $bd.hashCode());
        final Object $type = this.getType();
        result = result * 59 + (($type == null) ? 43 : $type.hashCode());
        final Object $pos = this.getPos();
        result = result * 59 + (($pos == null) ? 43 : $pos.hashCode());
        final Object $position = this.getPosition();
        result = result * 59 + (($position == null) ? 43 : $position.hashCode());
        final Object $field = this.getField();
        result = result * 59 + (($field == null) ? 43 : $field.hashCode());
        final Object $artifice = this.getArtifice();
        result = result * 59 + (($artifice == null) ? 43 : $artifice.hashCode());
        result = result * 59 + this.getProductOrder();
        return result;
    }
    
    @Override
    public String toString() {
        return "EquipEasy(id=" + this.getId() + ", roleId=" + this.getRoleId() + ", bd=" + this.getBd() + ", type=" + this.getType() + ", pos=" + this.getPos() + ", position=" + this.getPosition() + ", field=" + this.getField() + ", artifice=" + this.getArtifice() + ", productOrder=" + this.getProductOrder() + ")";
    }
}
