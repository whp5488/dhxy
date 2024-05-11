
package com.kitty.system.game.activity.message.vo;

import java.util.List;
import java.io.Serializable;

public class WaterMatchResultInfo implements Serializable
{
    private List<Integer> list;
    private byte state;
    
    public WaterMatchResultInfo() {
        this.state = 0;
    }
    
    public WaterMatchResultInfo(final List<Integer> list, final byte state) {
        this.state = 0;
        this.list = list;
        this.state = state;
    }
    
    public List<Integer> getList() {
        return this.list;
    }
    
    public byte getState() {
        return this.state;
    }
    
    public void setList(final List<Integer> list) {
        this.list = list;
    }
    
    public void setState(final byte state) {
        this.state = state;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof WaterMatchResultInfo)) {
            return false;
        }
        final WaterMatchResultInfo other = (WaterMatchResultInfo)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$list = this.getList();
        final Object other$list = other.getList();
        if (this$list == null) {
            if (other$list == null) {
                return this.getState() == other.getState();
            }
        }
        else if (this$list.equals(other$list)) {
            return this.getState() == other.getState();
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof WaterMatchResultInfo;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $list = this.getList();
        result = result * 59 + (($list == null) ? 43 : $list.hashCode());
        result = result * 59 + this.getState();
        return result;
    }
    
    @Override
    public String toString() {
        return "WaterMatchResultInfo(list=" + this.getList() + ", state=" + this.getState() + ")";
    }
}
