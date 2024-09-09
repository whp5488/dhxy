package com.kitty.system.init.domain.vo;

import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

public class Move implements Serializable
{
    private List<String> list;
    
    public Move() {
        this.list = new ArrayList<>();
    }
    
    public List<String> getList() {
        return this.list;
    }
    
    public void setList(final List<String> list) {
        this.list = list;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Move)) {
            return false;
        }
        Move other = (Move)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$list = this.getList();
        Object other$list = other.getList();
        return (this$list != null) ? this$list.equals(other$list) : (other$list == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof Move;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $list = this.getList();
        result = result * 59 + (($list == null) ? 43 : $list.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "Move(list=" + this.getList() + ")";
    }
}
