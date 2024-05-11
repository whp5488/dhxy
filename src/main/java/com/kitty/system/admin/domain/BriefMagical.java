
package com.kitty.system.admin.domain;

import java.io.Serializable;

public class BriefMagical implements Serializable
{
    private int id;
    private int serial;
    private String base;
    private String refining;
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
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof BriefMagical)) {
            return false;
        }
        final BriefMagical other = (BriefMagical)o;
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
        Label_0091: {
            if (this$base == null) {
                if (other$base == null) {
                    break Label_0091;
                }
            }
            else if (this$base.equals(other$base)) {
                break Label_0091;
            }
            return false;
        }
        final Object this$refining = this.getRefining();
        final Object other$refining = other.getRefining();
        if (this$refining == null) {
            if (other$refining == null) {
                return this.getGemCnt() == other.getGemCnt();
            }
        }
        else if (this$refining.equals(other$refining)) {
            return this.getGemCnt() == other.getGemCnt();
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof BriefMagical;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getId();
        result = result * 59 + this.getSerial();
        final Object $base = this.getBase();
        result = result * 59 + (($base == null) ? 43 : $base.hashCode());
        final Object $refining = this.getRefining();
        result = result * 59 + (($refining == null) ? 43 : $refining.hashCode());
        result = result * 59 + this.getGemCnt();
        return result;
    }
    
    @Override
    public String toString() {
        return "BriefMagical(id=" + this.getId() + ", serial=" + this.getSerial() + ", base=" + this.getBase() + ", refining=" + this.getRefining() + ", gemCnt=" + this.getGemCnt() + ")";
    }
}
