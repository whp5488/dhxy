package com.kitty.system.admin.domain;

import java.io.Serializable;

public class BriefAcsry implements Serializable
{
    private int id;
    private int serial;
    private String base;
    private byte pj;
    
    public BriefAcsry() {
    }
    
    public int getId() {
        return this.id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public int getSerial() {
        return this.serial;
    }
    
    public void setSerial(int serial) {
        this.serial = serial;
    }
    
    public String getBase() {
        return this.base;
    }
    
    public void setBase(String base) {
        this.base = base;
    }
    
    public byte getPj() {
        return this.pj;
    }
    
    public void setPj(byte pj) {
        this.pj = pj;
    }

}
