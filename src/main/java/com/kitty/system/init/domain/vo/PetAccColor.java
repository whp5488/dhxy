package com.kitty.system.init.domain.vo;

import java.util.HashMap;
import java.util.Map;
import java.io.Serializable;

public class PetAccColor implements Serializable
{
    private int silver;
    private Map<Integer, Integer> mat;
    private Map<Integer, Integer> switchMat;
    
    public PetAccColor() {
        this.silver = 0;
        this.mat = new HashMap();
        this.switchMat = new HashMap();
    }
    
    public int getSilver() {
        return this.silver;
    }
    
    public Map<Integer, Integer> getMat() {
        return this.mat;
    }
    
    public Map<Integer, Integer> getSwitchMat() {
        return this.switchMat;
    }
    
    public void setSilver(final int silver) {
        this.silver = silver;
    }
    
    public void setMat(final Map<Integer, Integer> mat) {
        this.mat = mat;
    }
    
    public void setSwitchMat(final Map<Integer, Integer> switchMat) {
        this.switchMat = switchMat;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof PetAccColor)) {
            return false;
        }
        PetAccColor other = (PetAccColor)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getSilver() != other.getSilver()) {
            return false;
        }
        Object this$mat = this.getMat();
        Object other$mat = other.getMat();
        if (!((this$mat != null) ? this$mat.equals(other$mat) : (other$mat == null))) {
            return false;
        }
        Object this$switchMat = this.getSwitchMat();
        Object other$switchMat = other.getSwitchMat();
        return (this$switchMat != null) ? this$switchMat.equals(other$switchMat) : (other$switchMat == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof PetAccColor;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getSilver();
        Object $mat = this.getMat();
        result = result * 59 + (($mat == null) ? 43 : $mat.hashCode());
        Object $switchMat = this.getSwitchMat();
        result = result * 59 + (($switchMat == null) ? 43 : $switchMat.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "PetAccColor(silver=" + this.getSilver() + ", mat=" + this.getMat() + ", switchMat=" + this.getSwitchMat() + ")";
    }
}
