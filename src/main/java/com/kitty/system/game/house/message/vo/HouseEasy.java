package com.kitty.system.game.house.message.vo;

import com.kitty.system.game.house.model.House;
import java.io.Serializable;

public class HouseEasy implements Serializable
{
    private String id;
    private String name;
    private int motif;
    private byte state;
    
    public HouseEasy(House house) {
        this.id = String.valueOf(house.getId());
        this.name = house.getName();
        this.motif = house.getMotif();
        this.state = house.getState();
    }
    
    public String getId() {
        return this.id;
    }
    
    public String getName() {
        return this.name;
    }
    
    public int getMotif() {
        return this.motif;
    }
    
    public byte getState() {
        return this.state;
    }
    
    public void setId(final String id) {
        this.id = id;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public void setMotif(final int motif) {
        this.motif = motif;
    }
    
    public void setState(final byte state) {
        this.state = state;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof HouseEasy)) {
            return false;
        }
        HouseEasy other = (HouseEasy)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$id = this.getId();
        Object other$id = other.getId();
        if (!((this$id != null) ? this$id.equals(other$id) : (other$id == null))) {
            return false;
        }
        Object this$name = this.getName();
        Object other$name = other.getName();
        return ((this$name != null) ? this$name.equals(other$name) : (other$name == null)) && this.getMotif() == other.getMotif() && this.getState() == other.getState();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof HouseEasy;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $id = this.getId();
        result = result * 59 + (($id == null) ? 43 : $id.hashCode());
        Object $name = this.getName();
        result = result * 59 + (($name == null) ? 43 : $name.hashCode());
        result = result * 59 + this.getMotif();
        result = result * 59 + this.getState();
        return result;
    }
    
    @Override
    public String toString() {
        return "HouseEasy(id=" + this.getId() + ", name=" + this.getName() + ", motif=" + this.getMotif() + ", state=" + (int)this.getState() + ")";
    }
    
    public HouseEasy() {
    }
    
    public HouseEasy(final String id, final String name, final int motif, final byte state) {
        this.id = id;
        this.name = name;
        this.motif = motif;
        this.state = state;
    }
}
