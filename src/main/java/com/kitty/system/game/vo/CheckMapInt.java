
package com.kitty.system.game.vo;

import java.beans.ConstructorProperties;
import java.util.Map;
import java.io.Serializable;

public class CheckMapInt implements Serializable
{
    private boolean flag;
    private Map<Integer, Integer> lack;
    private Map<Integer, Integer> has;
    
    public boolean isFlag() {
        return this.flag;
    }
    
    public Map<Integer, Integer> getLack() {
        return this.lack;
    }
    
    public Map<Integer, Integer> getHas() {
        return this.has;
    }
    
    public void setFlag(final boolean flag) {
        this.flag = flag;
    }
    
    public void setLack(final Map<Integer, Integer> lack) {
        this.lack = lack;
    }
    
    public void setHas(final Map<Integer, Integer> has) {
        this.has = has;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CheckMapInt)) {
            return false;
        }
        final CheckMapInt other = (CheckMapInt)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.isFlag() != other.isFlag()) {
            return false;
        }
        final Object this$lack = this.getLack();
        final Object other$lack = other.getLack();
        Label_0078: {
            if (this$lack == null) {
                if (other$lack == null) {
                    break Label_0078;
                }
            }
            else if (this$lack.equals(other$lack)) {
                break Label_0078;
            }
            return false;
        }
        final Object this$has = this.getHas();
        final Object other$has = other.getHas();
        if (this$has == null) {
            if (other$has == null) {
                return true;
            }
        }
        else if (this$has.equals(other$has)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof CheckMapInt;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + (this.isFlag() ? 79 : 97);
        final Object $lack = this.getLack();
        result = result * 59 + (($lack == null) ? 43 : $lack.hashCode());
        final Object $has = this.getHas();
        result = result * 59 + (($has == null) ? 43 : $has.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "CheckMapInt(flag=" + this.isFlag() + ", lack=" + this.getLack() + ", has=" + this.getHas() + ")";
    }
    
    public CheckMapInt() {
    }
    
    @ConstructorProperties({ "flag", "lack", "has" })
    public CheckMapInt(final boolean flag, final Map<Integer, Integer> lack, final Map<Integer, Integer> has) {
        this.flag = flag;
        this.lack = lack;
        this.has = has;
    }
}
