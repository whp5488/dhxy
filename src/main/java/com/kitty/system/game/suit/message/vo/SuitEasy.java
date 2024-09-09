package com.kitty.system.game.suit.message.vo;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

public class SuitEasy implements Serializable
{
    private String id;
    private String name;
    private List<String> equip;
    private byte state;
    
    public SuitEasy() {
        this.equip = new ArrayList<>();
    }
    
    public SuitEasy(Long id, String name, List<Long> equip, byte state) {
        this.equip = new ArrayList<>();
        this.id = String.valueOf(id);
        this.name = name;
        List<String> equipList = new ArrayList<>();
        for (Long key : equip) {
            equipList.add(String.valueOf(key));
        }
        this.equip = equipList;
        this.state = state;
    }
    
    public String getId() {
        return this.id;
    }
    
    public String getName() {
        return this.name;
    }
    
    public List<String> getEquip() {
        return this.equip;
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
    
    public void setEquip(final List<String> equip) {
        this.equip = equip;
    }
    
    public void setState(final byte state) {
        this.state = state;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SuitEasy)) {
            return false;
        }
        SuitEasy other = (SuitEasy)o;
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
        if (!((this$name != null) ? this$name.equals(other$name) : (other$name == null))) {
            return false;
        }
        Object this$equip = this.getEquip();
        Object other$equip = other.getEquip();
        return ((this$equip != null) ? this$equip.equals(other$equip) : (other$equip == null)) && this.getState() == other.getState();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof SuitEasy;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $id = this.getId();
        result = result * 59 + (($id == null) ? 43 : $id.hashCode());
        Object $name = this.getName();
        result = result * 59 + (($name == null) ? 43 : $name.hashCode());
        Object $equip = this.getEquip();
        result = result * 59 + (($equip == null) ? 43 : $equip.hashCode());
        result = result * 59 + this.getState();
        return result;
    }
    
    @Override
    public String toString() {
        return "SuitEasy(id=" + this.getId() + ", name=" + this.getName() + ", equip=" + this.getEquip() + ", state=" + (int)this.getState() + ")";
    }
}
