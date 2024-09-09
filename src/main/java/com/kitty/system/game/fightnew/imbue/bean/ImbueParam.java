package com.kitty.system.game.fightnew.imbue.bean;

import org.nutz.lang.util.NutMap;
import java.io.Serializable;

public class ImbueParam implements Serializable
{
    private int sr;
    private double value;
    private String others;
    private NutMap recordMap;
    
    public ImbueParam(int sr, double value) {
        this.recordMap = new NutMap();
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
    
    public NutMap getRecordMap() {
        return this.recordMap;
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
    
    public void setRecordMap(final NutMap recordMap) {
        this.recordMap = recordMap;
    }
    
    public ImbueParam(final int sr, final double value, final String others, final NutMap recordMap) {
        this.recordMap = new NutMap();
        this.sr = sr;
        this.value = value;
        this.others = others;
        this.recordMap = recordMap;
    }
    
    public ImbueParam() {
        this.recordMap = new NutMap();
    }
}
