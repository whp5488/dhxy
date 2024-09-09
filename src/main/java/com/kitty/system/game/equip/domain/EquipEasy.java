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
        this.bd = Integer.valueOf(0);
        this.field = new ArrayList<>();
        this.artifice = new ArrayList<>();
        this.productOrder = 0;
    }
    
    public EquipEasy(BagEquip equip, int roleId) {
        this.bd = Integer.valueOf(0);
        this.field = new ArrayList<>();
        this.artifice = new ArrayList<>();
        this.productOrder = 0;
        this.id = String.valueOf(equip.getId());
        this.roleId = Integer.valueOf(roleId);
        this.bd = Integer.valueOf(equip.getBd());
        this.type = Integer.valueOf(equip.getType());
        this.pos = Integer.valueOf(equip.getPos());
        this.position = Integer.valueOf(equip.getPosition());
        this.productOrder = equip.getProductOrder();
        for (FiedEquip fiedEquip : equip.getField()) {
            this.field.add(new FieldProp(fiedEquip.getType(), fiedEquip.getValue()));
        }
        for (FiedEquip fiedEquip : equip.getArtifice()) {
            this.artifice.add(new FieldProp(fiedEquip.getType(), fiedEquip.getValue()));
        }
    }
    
    public EquipEasy(WearEquip equip, int roleId) {
        this.bd = Integer.valueOf(0);
        this.field = new ArrayList<>();
        this.artifice = new ArrayList<>();
        this.productOrder = 0;
        this.id = String.valueOf(equip.getId());
        this.roleId = Integer.valueOf(roleId);
        this.bd = Integer.valueOf(equip.getBd());
        this.type = Integer.valueOf(equip.getType());
        this.pos = Integer.valueOf(equip.getPos());
        this.position = Integer.valueOf(equip.getPosition());
        this.productOrder = equip.getProductOrder();
        for (FiedEquip fiedEquip : equip.getField()) {
            this.field.add(new FieldProp(fiedEquip.getType(), fiedEquip.getValue()));
        }
        for (FiedEquip fiedEquip : equip.getArtifice()) {
            this.artifice.add(new FieldProp(fiedEquip.getType(), fiedEquip.getValue()));
        }
    }
    
    public EquipEasy(StoreEquip equip, int roleId) {
        this.bd = Integer.valueOf(0);
        this.field = new ArrayList<>();
        this.artifice = new ArrayList<>();
        this.productOrder = 0;
        this.id = String.valueOf(equip.getId());
        this.roleId = Integer.valueOf(roleId);
        this.bd = Integer.valueOf(equip.getBd());
        this.type = Integer.valueOf(equip.getType());
        this.pos = Integer.valueOf(equip.getPos());
        this.position = Integer.valueOf(equip.getPosition());
        this.productOrder = equip.getProductOrder();
        for (FiedEquip fiedEquip : equip.getField()) {
            this.field.add(new FieldProp(fiedEquip.getType(), fiedEquip.getValue()));
        }
        for (FiedEquip fiedEquip : equip.getArtifice()) {
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
}
