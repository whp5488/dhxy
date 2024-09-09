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
        this.list = new ArrayList<>();
    }
    
    public EquipNeo(List<FiedEquip> list) {
        this.list = new ArrayList<>();
        for (FiedEquip fiedEquip : list) {
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
}
