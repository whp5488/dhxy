package com.kitty.system.game.equip.message;

import java.util.Iterator;
import com.kitty.system.game.vo.FiedEquip;
import com.kitty.system.game.bag.domain.BagEquip;
import java.util.ArrayList;
import com.kitty.system.game.vo.FieldProp;
import java.util.List;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 41241)
public class RespAcsryEquip extends Message
{
    private int serial;
    private List<FieldProp> list;
    private byte productOrder;
    
    public RespAcsryEquip() {
        this.serial = 0;
        this.list = new ArrayList<>();
        this.productOrder = 0;
    }
    
    public RespAcsryEquip(BagEquip equip) {
        this.serial = 0;
        this.list = new ArrayList<>();
        this.productOrder = 0;
        for (FiedEquip fiedEquip : equip.getField()) {
            this.list.add(new FieldProp(fiedEquip.getType(), fiedEquip.getValue()));
        }
        this.serial = equip.getType();
        this.productOrder = equip.getProductOrder();
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
