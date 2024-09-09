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
        this.bd = Integer.valueOf(0);
        this.field = new ArrayList<>();
        this.productOrder = 0;
        this.lock = 0;
    }
    
    public PetEquipEasy(PetEquip equip) {
        this.bd = Integer.valueOf(0);
        this.field = new ArrayList<>();
        this.productOrder = 0;
        this.lock = 0;
        this.id = String.valueOf(equip.getId());
        this.petId = String.valueOf(equip.getPId());
        this.bd = Integer.valueOf(equip.getBd());
        this.type = Integer.valueOf(equip.getSerial());
        this.productOrder = equip.getProductOrder();
        this.positionNum = equip.getPositionNum();
        this.lock = equip.getLock();
        for (FiedEquip fiedEquip : equip.getField()) {
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
}
