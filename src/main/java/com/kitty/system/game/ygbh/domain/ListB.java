
package com.kitty.system.game.ygbh.domain;

import java.util.List;

public class ListB
{
    private List<YgbhItem> listB;
    
    public ListB() {
    }
    
    public ListB(final List<YgbhItem> listB) {
        this.listB = listB;
    }
    
    public List<YgbhItem> getListB() {
        return this.listB;
    }
    
    public void setListB(final List<YgbhItem> listB) {
        this.listB = listB;
    }
    
    @Override
    public String toString() {
        return "ListB{listB=" + this.listB + '}';
    }
}
