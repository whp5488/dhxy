package com.kitty.system.game.mind.helper.vo;

import java.util.Iterator;
import java.util.ArrayList;
import com.kitty.system.game.vo.FieldProp;
import java.util.List;
import java.io.Serializable;

public class MindNeo implements Serializable
{
    List<FieldProp> list;
    
    public MindNeo() {
        this.list = new ArrayList<>();
    }
    
    public MindNeo(List<FieldProp> list) {
        this.list = new ArrayList<>();
        int score = 0;
        for (FieldProp obj : list) {
            score += obj.getValue();
            this.list.add(new FieldProp(obj.getType(), obj.getValue()));
        }
        this.list.add(new FieldProp(6679, score));
    }
    
    public List<FieldProp> getList() {
        return this.list;
    }
    
    public void setList(final List<FieldProp> list) {
        this.list = list;
    }
}
