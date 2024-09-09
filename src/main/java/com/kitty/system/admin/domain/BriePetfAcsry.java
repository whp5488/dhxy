package com.kitty.system.admin.domain;

import java.io.Serializable;

public class BriePetfAcsry implements Serializable
{
    private int id;
    private byte grade;
    private byte pos;
    private byte pj;
    private String base;
    
    public BriePetfAcsry() {
    }
    
    public int getId() {
        return this.id;
    }
    
    public byte getGrade() {
        return this.grade;
    }
    
    public byte getPos() {
        return this.pos;
    }
    
    public byte getPj() {
        return this.pj;
    }
    
    public String getBase() {
        return this.base;
    }
    
    public void setId(final int id) {
        this.id = id;
    }
    
    public void setGrade(final byte grade) {
        this.grade = grade;
    }
    
    public void setPos(final byte pos) {
        this.pos = pos;
    }
    
    public void setPj(final byte pj) {
        this.pj = pj;
    }
    
    public void setBase(final String base) {
        this.base = base;
    }
}
