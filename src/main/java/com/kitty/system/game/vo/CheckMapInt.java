package com.kitty.system.game.vo;

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

    public CheckMapInt() {
    }
    
    public CheckMapInt(final boolean flag, final Map<Integer, Integer> lack, final Map<Integer, Integer> has) {
        this.flag = flag;
        this.lack = lack;
        this.has = has;
    }
}
