package com.kitty.common.utils.vo;

public class Approximate
{
    private int id;
    private int num;
    
    public int getId() {
        return this.id;
    }
    
    public int getNum() {
        return this.num;
    }
    
    public void setId(final int id) {
        this.id = id;
    }
    
    public void setNum(final int num) {
        this.num = num;
    }
    
    public Approximate() {
    }
    
    public Approximate(final int id, final int num) {
        this.id = id;
        this.num = num;
    }
}
