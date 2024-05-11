
package com.kitty.system.game.fightnew.bean;

import java.util.ArrayList;
import java.util.List;

public class DayInCeFight
{
    private short level;
    List<TianFuObject> tianFuList;
    
    public DayInCeFight() {
        this.tianFuList = new ArrayList<TianFuObject>();
    }
    
    public short getLevel() {
        return this.level;
    }
    
    public List<TianFuObject> getTianFuList() {
        return this.tianFuList;
    }
    
    public void setLevel(final short level) {
        this.level = level;
    }
    
    public void setTianFuList(final List<TianFuObject> tianFuList) {
        this.tianFuList = tianFuList;
    }
}
