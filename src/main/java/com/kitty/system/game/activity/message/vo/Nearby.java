package com.kitty.system.game.activity.message.vo;

import com.kitty.system.game.role.domain.Role;
import java.io.Serializable;

public class Nearby implements Serializable
{
    private int id;
    private int serial;
    private String name;
    private byte rei;
    private short level;
    
    public Nearby() {
    }
    
    public Nearby(Role role) {
        this.id = (int)role.getRoleId();
        this.serial = (int)role.getSerial();
        this.name = role.getName();
        this.rei = role.getRelive().byteValue();
        this.level = role.getLevel().shortValue();
    }
    
    public Nearby(int id, int serial, String name, byte rei, short level) {
        this.id = id;
        this.serial = serial;
        this.name = name;
        this.rei = rei;
        this.level = level;
    }
    
    public int getId() {
        return this.id;
    }
    
    public int getSerial() {
        return this.serial;
    }
    
    public String getName() {
        return this.name;
    }
    
    public byte getRei() {
        return this.rei;
    }
    
    public short getLevel() {
        return this.level;
    }
    
    public void setId(final int id) {
        this.id = id;
    }
    
    public void setSerial(final int serial) {
        this.serial = serial;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public void setRei(final byte rei) {
        this.rei = rei;
    }
    
    public void setLevel(final short level) {
        this.level = level;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Nearby)) {
            return false;
        }
        Nearby other = (Nearby)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getId() != other.getId()) {
            return false;
        }
        if (this.getSerial() != other.getSerial()) {
            return false;
        }
        Object this$name = this.getName();
        Object other$name = other.getName();
        return ((this$name != null) ? this$name.equals(other$name) : (other$name == null)) && this.getRei() == other.getRei() && this.getLevel() == other.getLevel();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof Nearby;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getId();
        result = result * 59 + this.getSerial();
        Object $name = this.getName();
        result = result * 59 + (($name == null) ? 43 : $name.hashCode());
        result = result * 59 + this.getRei();
        result = result * 59 + this.getLevel();
        return result;
    }
    
    @Override
    public String toString() {
        return "Nearby(id=" + this.getId() + ", serial=" + this.getSerial() + ", name=" + this.getName() + ", rei=" + (int)this.getRei() + ", level=" + (int)this.getLevel() + ")";
    }
}
