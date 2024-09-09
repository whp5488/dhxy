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
    
    public void setName(String name) {
        this.name = name;
    }
    
    public long getTime() {
        return this.time;
    }
    
    public void setTime(long time) {
        this.time = time;
    }
    
    public List<Param> getList() {
        return this.list;
    }
    
    public void setList(List<Param> list) {
        this.list = list;
    }
    
    public BrieGift(String name, long time, List<Param> list) {
        this.name = name;
        this.time = time;
        this.list = list;
    }
}
