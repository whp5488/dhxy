
package com.kitty.system.admin.domain;

import java.io.Serializable;

public class BriefAcsry implements Serializable
{
    private int id;
    private int serial;
    private String base;
    private byte pj;
    
    public int getId() {
        return this.id;
    }
    
    public void setId(final int id) {
        this.id = id;
    }
    
    public int getSerial() {
        return this.serial;
    }
    
    public void setSerial(final int serial) {
        this.serial = serial;
    }
    
    public String getBase() {
        return this.base;
    }
    
    public void setBase(final String base) {
        this.base = base;
    }
    
    public byte getPj() {
        return this.pj;
    }
    
    public void setPj(final byte pj) {
        this.pj = pj;
    }
    
    @Override
    public String toString() {
        return "BriefAcsry{id=" + this.id + ", serial=" + this.serial + ", base='" + this.base + '\'' + ", pj=" + this.pj + '}';
    }
}
