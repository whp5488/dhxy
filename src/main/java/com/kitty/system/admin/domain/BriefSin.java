package com.kitty.system.admin.domain;

import java.io.Serializable;

public class BriefSin implements Serializable
{
    private int id;
    private int serial;
    private String base;
    private int gemCnt;
    
    public BriefSin() {
    }
    
    public int getId() {
        return this.id;
    }
    
    public int getSerial() {
        return this.serial;
    }
    
    public String getBase() {
        return this.base;
    }
    
    public int getGemCnt() {
        return this.gemCnt;
    }
    
    public void setId(final int id) {
        this.id = id;
    }
    
    public void setSerial(final int serial) {
        this.serial = serial;
    }
    
    public void setBase(final String base) {
        this.base = base;
    }
    
    public void setGemCnt(final int gemCnt) {
        this.gemCnt = gemCnt;
    }
}
