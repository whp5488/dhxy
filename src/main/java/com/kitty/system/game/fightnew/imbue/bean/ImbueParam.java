
package com.kitty.system.game.fightnew.imbue.bean;

import java.beans.ConstructorProperties;

public class ImbueParam
{
    private int sr;
    private double value;
    private String others;
    
    public ImbueParam(final int sr, final double value) {
        this.sr = sr;
        this.value = value;
    }
    
    public int getSr() {
        return this.sr;
    }
    
    public double getValue() {
        return this.value;
    }
    
    public String getOthers() {
        return this.others;
    }
    
    public void setSr(final int sr) {
        this.sr = sr;
    }
    
    public void setValue(final double value) {
        this.value = value;
    }
    
    public void setOthers(final String others) {
        this.others = others;
    }
    
    @ConstructorProperties({ "sr", "value", "others" })
    public ImbueParam(final int sr, final double value, final String others) {
        this.sr = sr;
        this.value = value;
        this.others = others;
    }
    
    public ImbueParam() {
    }
}
