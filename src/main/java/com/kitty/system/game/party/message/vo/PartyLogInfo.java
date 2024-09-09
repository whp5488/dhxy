package com.kitty.system.game.party.message.vo;

import java.util.List;
import java.io.Serializable;

public class PartyLogInfo implements Serializable
{
    private int time;
    private List<LogInfo> list;
    
    public PartyLogInfo() {
    }
    
    public int getTime() {
        return this.time;
    }
    
    public List<LogInfo> getList() {
        return this.list;
    }
    
    public void setTime(final int time) {
        this.time = time;
    }
    
    public void setList(final List<LogInfo> list) {
        this.list = list;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof PartyLogInfo)) {
            return false;
        }
        PartyLogInfo other = (PartyLogInfo)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getTime() != other.getTime()) {
            return false;
        }
        Object this$list = this.getList();
        Object other$list = other.getList();
        return (this$list != null) ? this$list.equals(other$list) : (other$list == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof PartyLogInfo;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getTime();
        Object $list = this.getList();
        result = result * 59 + (($list == null) ? 43 : $list.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "PartyLogInfo(time=" + this.getTime() + ", list=" + this.getList() + ")";
    }
}
