package com.kitty.system.game.vo.init;

import java.io.Serializable;

public class Item implements Serializable
{
    private Integer item;
    private Integer num;
    private Integer others;
    
    public Item(Integer item, Integer num) {
        this.others = Integer.valueOf(0);
        this.item = item;
        this.num = num;
    }
    
    public Item(Integer item, Integer num, Integer others) {
        this.others = Integer.valueOf(0);
        this.item = item;
        this.num = num;
        this.others = others;
    }
    
    public Item() {
        this.others = Integer.valueOf(0);
    }
    
    public Integer getItem() {
        return this.item;
    }
    
    public Integer getNum() {
        return this.num;
    }
    
    public Integer getOthers() {
        return this.others;
    }
    
    public void setItem(final Integer item) {
        this.item = item;
    }
    
    public void setNum(final Integer num) {
        this.num = num;
    }
    
    public void setOthers(final Integer others) {
        this.others = others;
    }
}
