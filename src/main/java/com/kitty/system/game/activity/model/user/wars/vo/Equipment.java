package com.kitty.system.game.activity.model.user.wars.vo;

import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.io.Serializable;

public class Equipment implements Serializable
{
    private ScheduledFuture<?> scheduledFuture;
    private byte stance;
    private int team;
    private String name;
    private List<Long> list;
    private int time;
    
    public ScheduledFuture<?> getScheduledFuture() {
        return this.scheduledFuture;
    }
    
    public byte getStance() {
        return this.stance;
    }
    
    public int getTeam() {
        return this.team;
    }
    
    public String getName() {
        return this.name;
    }
    
    public List<Long> getList() {
        return this.list;
    }
    
    public int getTime() {
        return this.time;
    }
    
    public void setScheduledFuture(final ScheduledFuture<?> scheduledFuture) {
        this.scheduledFuture = scheduledFuture;
    }
    
    public void setStance(final byte stance) {
        this.stance = stance;
    }
    
    public void setTeam(final int team) {
        this.team = team;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public void setList(final List<Long> list) {
        this.list = list;
    }
    
    public void setTime(final int time) {
        this.time = time;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Equipment)) {
            return false;
        }
        Equipment other = (Equipment)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$scheduledFuture = this.getScheduledFuture();
        Object other$scheduledFuture = other.getScheduledFuture();
        if (!((this$scheduledFuture != null) ? this$scheduledFuture.equals(other$scheduledFuture) : (other$scheduledFuture == null))) {
            return false;
        }
        if (this.getStance() != other.getStance()) {
            return false;
        }
        if (this.getTeam() != other.getTeam()) {
            return false;
        }
        Object this$name = this.getName();
        Object other$name = other.getName();
        if (!((this$name != null) ? this$name.equals(other$name) : (other$name == null))) {
            return false;
        }
        Object this$list = this.getList();
        Object other$list = other.getList();
        return ((this$list != null) ? this$list.equals(other$list) : (other$list == null)) && this.getTime() == other.getTime();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof Equipment;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $scheduledFuture = this.getScheduledFuture();
        result = result * 59 + (($scheduledFuture == null) ? 43 : $scheduledFuture.hashCode());
        result = result * 59 + this.getStance();
        result = result * 59 + this.getTeam();
        Object $name = this.getName();
        result = result * 59 + (($name == null) ? 43 : $name.hashCode());
        Object $list = this.getList();
        result = result * 59 + (($list == null) ? 43 : $list.hashCode());
        result = result * 59 + this.getTime();
        return result;
    }
    
    @Override
    public String toString() {
        return "Equipment(scheduledFuture=" + this.getScheduledFuture() + ", stance=" + (int)this.getStance() + ", team=" + this.getTeam() + ", name=" + this.getName() + ", list=" + this.getList() + ", time=" + this.getTime() + ")";
    }
    
    public Equipment(final ScheduledFuture<?> scheduledFuture, final byte stance, final int team, final String name, final List<Long> list, final int time) {
        this.time = 0;
        this.scheduledFuture = scheduledFuture;
        this.stance = stance;
        this.team = team;
        this.name = name;
        this.list = list;
        this.time = time;
    }
    
    public Equipment() {
        this.time = 0;
    }
}
