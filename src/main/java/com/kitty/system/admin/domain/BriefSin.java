
package com.kitty.system.admin.domain;

import java.io.Serializable;

public class BriefSin implements Serializable
{
    private int id;
    private int serial;
    private String base;
    private int gemCnt;
    
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
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof BriefSin)) {
            return false;
        }
        final BriefSin other = (BriefSin)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getId() != other.getId()) {
            return false;
        }
        if (this.getSerial() != other.getSerial()) {
            return false;
        }
        final Object this$base = this.getBase();
        final Object other$base = other.getBase();
        if (this$base == null) {
            if (other$base == null) {
                return this.getGemCnt() == other.getGemCnt();
            }
        }
        else if (this$base.equals(other$base)) {
            return this.getGemCnt() == other.getGemCnt();
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof BriefSin;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getId();
        result = result * 59 + this.getSerial();
        final Object $base = this.getBase();
        result = result * 59 + (($base == null) ? 43 : $base.hashCode());
        result = result * 59 + this.getGemCnt();
        return result;
    }
    
    @Override
    public String toString() {
        return "BriefSin(id=" + this.getId() + ", serial=" + this.getSerial() + ", base=" + this.getBase() + ", gemCnt=" + this.getGemCnt() + ")";
    }
}
