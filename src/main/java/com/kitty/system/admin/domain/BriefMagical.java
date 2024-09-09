package com.kitty.system.admin.domain;

import java.io.Serializable;

public class BriefMagical implements Serializable
{
    private int id;
    private int serial;
    private String base;
    private String refining;
    private int gemCnt;
    
    public BriefMagical() {
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
    
    public String getRefining() {
        return this.refining;
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
    
    public void setRefining(final String refining) {
        this.refining = refining;
    }
    
    public void setGemCnt(final int gemCnt) {
        this.gemCnt = gemCnt;
    }
}
