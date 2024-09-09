
package com.kitty.system.game.ygbh.domain;

public class YgbhItem
{
    private int id;
    private int type;
    private int item;
    private int num;
    
    public YgbhItem() {
    }
    
    public YgbhItem(final int num, final int type, final int item, final int id) {
        this.id = id;
        this.type = type;
        this.item = item;
        this.num = num;
    }
    
    public int getNum() {
        return this.num;
    }
    
    public void setNum(final int num) {
        this.num = num;
    }
    
    public int getType() {
        return this.type;
    }
    
    public void setType(final int type) {
        this.type = type;
    }
    
    public int getItem() {
        return this.item;
    }
    
    public void setItem(final int item) {
        this.item = item;
    }
    
    public int getId() {
        return this.id;
    }
    
    public void setId(final int id) {
        this.id = id;
    }
}
