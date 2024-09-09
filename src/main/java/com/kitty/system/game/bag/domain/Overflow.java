package com.kitty.system.game.bag.domain;

import java.util.ArrayList;
import com.kitty.system.game.bag.domain.vo.Temporary;
import java.util.List;
import java.io.Serializable;

public class Overflow implements Serializable
{
    private List<Temporary> list;
    private boolean flag;
    
    public Overflow() {
        this.list = new ArrayList<>();
        this.flag = false;
    }
    
    public List<Temporary> getList() {
        return this.list;
    }
    
    public boolean isFlag() {
        return this.flag;
    }
    
    public void setList(final List<Temporary> list) {
        this.list = list;
    }
    
    public void setFlag(final boolean flag) {
        this.flag = flag;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Overflow)) {
            return false;
        }
        Overflow other = (Overflow)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$list = this.getList();
        Object other$list = other.getList();
        return ((this$list != null) ? this$list.equals(other$list) : (other$list == null)) && this.isFlag() == other.isFlag();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof Overflow;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $list = this.getList();
        result = result * 59 + (($list == null) ? 43 : $list.hashCode());
        result = result * 59 + (this.isFlag() ? 79 : 97);
        return result;
    }
    
    @Override
    public String toString() {
        return "Overflow(list=" + this.getList() + ", flag=" + this.isFlag() + ")";
    }
}
