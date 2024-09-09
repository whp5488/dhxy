package com.kitty.system.game.vo;

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
    
    public Condition(final Map<Integer, Integer> map, final List<Item> list) {
        this.map = map;
        this.list = list;
    }
    
    public Condition() {
    }
}
