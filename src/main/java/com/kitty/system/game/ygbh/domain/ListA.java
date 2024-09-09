
package com.kitty.system.game.ygbh.domain;

import java.util.List;

public class ListA
{
    private List<YgbhItem> listA;
    
    public ListA() {
    }
    
    public ListA(final List<YgbhItem> listA) {
        this.listA = listA;
    }
    
    public List<YgbhItem> getListA() {
        return this.listA;
    }
    
    public void setListA(final List<YgbhItem> listA) {
        this.listA = listA;
    }
    
    @Override
    public String toString() {
        return "ListA{listA=" + this.listA + '}';
    }
}
