
package com.kitty.system.admin.domain;

import com.kitty.system.init.domain.vo.Param;
import java.util.List;
import java.io.Serializable;

public class BrieGift implements Serializable
{
    private String name;
    private long time;
    private List<Param> list;
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public long getTime() {
        return this.time;
    }
    
    public void setTime(final long time) {
        this.time = time;
    }
    
    public List<Param> getList() {
        return this.list;
    }
    
    public void setList(final List<Param> list) {
        this.list = list;
    }
    
    public BrieGift(final String name, final long time, final List<Param> list) {
        this.name = name;
        this.time = time;
        this.list = list;
    }
}
