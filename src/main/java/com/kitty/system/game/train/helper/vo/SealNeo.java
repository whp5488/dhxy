package com.kitty.system.game.train.helper.vo;

import com.kitty.system.game.train.domain.vo.FieldSeal;
import java.util.ArrayList;
import com.kitty.system.game.vo.FieldSealProp;
import java.util.List;
import java.io.Serializable;

public class SealNeo implements Serializable
{
    List<FieldSealProp> list;
    
    public SealNeo() {
        this.list = new ArrayList<>();
    }
    
    public SealNeo(List<FieldSeal> list) {
        this.list = new ArrayList<>();
        int score = 0;
        for (FieldSeal field : list) {
            this.list.add(new FieldSealProp(field));
        }
        if (list.size() > 0) {
            this.list.add(new FieldSealProp(6679, 6679, score));
        }
    }
    
    public List<FieldSealProp> getList() {
        return this.list;
    }
    
    public void setList(final List<FieldSealProp> list) {
        this.list = list;
    }
}
