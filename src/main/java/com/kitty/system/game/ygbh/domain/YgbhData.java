
package com.kitty.system.game.ygbh.domain;

public class YgbhData
{
    private int priceA;
    private int priceB;
    private ListA listA;
    private ListB listB;
    
    public YgbhData() {
    }
    
    public YgbhData(final int priceA, final int priceB, final ListA listA, final ListB listB) {
        this.priceA = priceA;
        this.priceB = priceB;
        this.listA = listA;
        this.listB = listB;
    }
    
    public int getPriceA() {
        return this.priceA;
    }
    
    public void setPriceA(final int priceA) {
        this.priceA = priceA;
    }
    
    public int getPriceB() {
        return this.priceB;
    }
    
    public void setPriceB(final int priceB) {
        this.priceB = priceB;
    }
    
    public ListA getListA() {
        return this.listA;
    }
    
    public void setListA(final ListA listA) {
        this.listA = listA;
    }
    
    public ListB getListB() {
        return this.listB;
    }
    
    public void setListB(final ListB listB) {
        this.listB = listB;
    }
    
    @Override
    public String toString() {
        return "Data{priceA=" + this.priceA + ", priceB=" + this.priceB + ", listA=" + this.listA + ", listB=" + this.listB + '}';
    }
}
