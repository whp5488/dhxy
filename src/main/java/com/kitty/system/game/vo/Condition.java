
package com.kitty.system.game.vo;

import java.beans.ConstructorProperties;
import com.kitty.system.game.vo.init.Item;
import java.util.List;
import java.util.Map;

public class Condition
{
    private Map<Integer, Integer> map;
    private List<Item> list;
    
    public Map<Integer, Integer> getMap() {
        return this.map;
    }
    
    public List<Item> getList() {
        return this.list;
    }
    
    public void setMap(final Map<Integer, Integer> map) {
        this.map = map;
    }
    
    public void setList(final List<Item> list) {
        this.list = list;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Condition)) {
            return false;
        }
        final Condition other = (Condition)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$map = this.getMap();
        final Object other$map = other.getMap();
        Label_0065: {
            if (this$map == null) {
                if (other$map == null) {
                    break Label_0065;
                }
            }
            else if (this$map.equals(other$map)) {
                break Label_0065;
            }
            return false;
        }
        final Object this$list = this.getList();
        final Object other$list = other.getList();
        if (this$list == null) {
            if (other$list == null) {
                return true;
            }
        }
        else if (this$list.equals(other$list)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof Condition;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $map = this.getMap();
        result = result * 59 + (($map == null) ? 43 : $map.hashCode());
        final Object $list = this.getList();
        result = result * 59 + (($list == null) ? 43 : $list.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "Condition(map=" + this.getMap() + ", list=" + this.getList() + ")";
    }
    
    @ConstructorProperties({ "map", "list" })
    public Condition(final Map<Integer, Integer> map, final List<Item> list) {
        this.map = map;
        this.list = list;
    }
    
    public Condition() {
    }
}
