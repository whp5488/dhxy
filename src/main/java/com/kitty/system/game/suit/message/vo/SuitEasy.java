
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
        this.equip = new ArrayList<String>();
    }
    
    public SuitEasy(final Long id, final String name, final List<Long> equip, final byte state) {
        this.equip = new ArrayList<String>();
        this.id = String.valueOf(id);
        this.name = name;
        final List<String> equipList = new ArrayList<String>();
        for (final Long key : equip) {
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
        final SuitEasy other = (SuitEasy)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$id = this.getId();
        final Object other$id = other.getId();
        Label_0065: {
            if (this$id == null) {
                if (other$id == null) {
                    break Label_0065;
                }
            }
            else if (this$id.equals(other$id)) {
                break Label_0065;
            }
            return false;
        }
        final Object this$name = this.getName();
        final Object other$name = other.getName();
        Label_0102: {
            if (this$name == null) {
                if (other$name == null) {
                    break Label_0102;
                }
            }
            else if (this$name.equals(other$name)) {
                break Label_0102;
            }
            return false;
        }
        final Object this$equip = this.getEquip();
        final Object other$equip = other.getEquip();
        if (this$equip == null) {
            if (other$equip == null) {
                return this.getState() == other.getState();
            }
        }
        else if (this$equip.equals(other$equip)) {
            return this.getState() == other.getState();
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof SuitEasy;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * 59 + (($id == null) ? 43 : $id.hashCode());
        final Object $name = this.getName();
        result = result * 59 + (($name == null) ? 43 : $name.hashCode());
        final Object $equip = this.getEquip();
        result = result * 59 + (($equip == null) ? 43 : $equip.hashCode());
        result = result * 59 + this.getState();
        return result;
    }
    
    @Override
    public String toString() {
        return "SuitEasy(id=" + this.getId() + ", name=" + this.getName() + ", equip=" + this.getEquip() + ", state=" + this.getState() + ")";
    }
}
