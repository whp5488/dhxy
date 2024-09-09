package com.kitty.system.game.party.model;

import java.util.ArrayList;
import java.util.List;

public class ReqJoinMember
{
    private int id;
    private String name;
    private int time;
    private byte rei;
    private short level;
    private short serial;
    private List<Integer> light_effect;
    
    public int getId() {
        return this.id;
    }
    
    public String getName() {
        return this.name;
    }
    
    public int getTime() {
        return this.time;
    }
    
    public byte getRei() {
        return this.rei;
    }
    
    public short getLevel() {
        return this.level;
    }
    
    public short getSerial() {
        return this.serial;
    }
    
    public List<Integer> getLight_effect() {
        return this.light_effect;
    }
    
    public void setId(final int id) {
        this.id = id;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public void setTime(final int time) {
        this.time = time;
    }
    
    public void setRei(final byte rei) {
        this.rei = rei;
    }
    
    public void setLevel(final short level) {
        this.level = level;
    }
    
    public void setSerial(final short serial) {
        this.serial = serial;
    }
    
    public void setLight_effect(final List<Integer> light_effect) {
        this.light_effect = light_effect;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ReqJoinMember)) {
            return false;
        }
        ReqJoinMember other = (ReqJoinMember)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getId() != other.getId()) {
            return false;
        }
        Object this$name = this.getName();
        Object other$name = other.getName();
        if (!((this$name != null) ? this$name.equals(other$name) : (other$name == null))) {
            return false;
        }
        if (this.getTime() != other.getTime()) {
            return false;
        }
        if (this.getRei() != other.getRei()) {
            return false;
        }
        if (this.getLevel() != other.getLevel()) {
            return false;
        }
        if (this.getSerial() != other.getSerial()) {
            return false;
        }
        Object this$light_effect = this.getLight_effect();
        Object other$light_effect = other.getLight_effect();
        return (this$light_effect != null) ? this$light_effect.equals(other$light_effect) : (other$light_effect == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ReqJoinMember;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getId();
        Object $name = this.getName();
        result = result * 59 + (($name == null) ? 43 : $name.hashCode());
        result = result * 59 + this.getTime();
        result = result * 59 + this.getRei();
        result = result * 59 + this.getLevel();
        result = result * 59 + this.getSerial();
        Object $light_effect = this.getLight_effect();
        result = result * 59 + (($light_effect == null) ? 43 : $light_effect.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "ReqJoinMember(id=" + this.getId() + ", name=" + this.getName() + ", time=" + this.getTime() + ", rei=" + (int)this.getRei() + ", level=" + (int)this.getLevel() + ", serial=" + (int)this.getSerial() + ", light_effect=" + this.getLight_effect() + ")";
    }
    
    public ReqJoinMember() {
        this.light_effect = new ArrayList<>();
    }
    
    public ReqJoinMember(final int id, final String name, final int time, final byte rei, final short level, final short serial, final List<Integer> light_effect) {
        this.light_effect = new ArrayList<>();
        this.id = id;
        this.name = name;
        this.time = time;
        this.rei = rei;
        this.level = level;
        this.serial = serial;
        this.light_effect = light_effect;
    }
}
