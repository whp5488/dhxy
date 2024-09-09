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
        this.list = new ArrayList<>();
        this.productOrder = 0;
    }
    
    public PetEquipNeo(long id, int serial, byte productOrder, List<FiedEquip> list) {
        this.serial = 0;
        this.list = new ArrayList<>();
        this.productOrder = 0;
        this.id = String.valueOf(id);
        this.serial = serial;
        this.productOrder = productOrder;
        for (FiedEquip fiedEquip : list) {
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
}
