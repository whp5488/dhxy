
package com.kitty.system.game.activity.model.user.wars.vo;

import java.beans.ConstructorProperties;
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
        final Equipment other = (Equipment)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$scheduledFuture = this.getScheduledFuture();
        final Object other$scheduledFuture = other.getScheduledFuture();
        Label_0065: {
            if (this$scheduledFuture == null) {
                if (other$scheduledFuture == null) {
                    break Label_0065;
                }
            }
            else if (this$scheduledFuture.equals(other$scheduledFuture)) {
                break Label_0065;
            }
            return false;
        }
        if (this.getStance() != other.getStance()) {
            return false;
        }
        if (this.getTeam() != other.getTeam()) {
            return false;
        }
        final Object this$name = this.getName();
        final Object other$name = other.getName();
        Label_0128: {
            if (this$name == null) {
                if (other$name == null) {
                    break Label_0128;
                }
            }
            else if (this$name.equals(other$name)) {
                break Label_0128;
            }
            return false;
        }
        final Object this$list = this.getList();
        final Object other$list = other.getList();
        if (this$list == null) {
            if (other$list == null) {
                return this.getTime() == other.getTime();
            }
        }
        else if (this$list.equals(other$list)) {
            return this.getTime() == other.getTime();
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof Equipment;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $scheduledFuture = this.getScheduledFuture();
        result = result * 59 + (($scheduledFuture == null) ? 43 : $scheduledFuture.hashCode());
        result = result * 59 + this.getStance();
        result = result * 59 + this.getTeam();
        final Object $name = this.getName();
        result = result * 59 + (($name == null) ? 43 : $name.hashCode());
        final Object $list = this.getList();
        result = result * 59 + (($list == null) ? 43 : $list.hashCode());
        result = result * 59 + this.getTime();
        return result;
    }
    
    @Override
    public String toString() {
        return "Equipment(scheduledFuture=" + this.getScheduledFuture() + ", stance=" + this.getStance() + ", team=" + this.getTeam() + ", name=" + this.getName() + ", list=" + this.getList() + ", time=" + this.getTime() + ")";
    }
    
    @ConstructorProperties({ "scheduledFuture", "stance", "team", "name", "list", "time" })
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
