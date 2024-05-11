
package com.kitty.system.admin.domain;

import java.io.Serializable;

public class BriePetfAcsry implements Serializable
{
    private int id;
    private byte grade;
    private byte pos;
    private byte pj;
    private String base;
    
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
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof BriePetfAcsry)) {
            return false;
        }
        final BriePetfAcsry other = (BriePetfAcsry)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getId() != other.getId()) {
            return false;
        }
        if (this.getGrade() != other.getGrade()) {
            return false;
        }
        if (this.getPos() != other.getPos()) {
            return false;
        }
        if (this.getPj() != other.getPj()) {
            return false;
        }
        final Object this$base = this.getBase();
        final Object other$base = other.getBase();
        if (this$base == null) {
            if (other$base == null) {
                return true;
            }
        }
        else if (this$base.equals(other$base)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof BriePetfAcsry;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getId();
        result = result * 59 + this.getGrade();
        result = result * 59 + this.getPos();
        result = result * 59 + this.getPj();
        final Object $base = this.getBase();
        result = result * 59 + (($base == null) ? 43 : $base.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "BriePetfAcsry(id=" + this.getId() + ", grade=" + this.getGrade() + ", pos=" + this.getPos() + ", pj=" + this.getPj() + ", base=" + this.getBase() + ")";
    }
}
