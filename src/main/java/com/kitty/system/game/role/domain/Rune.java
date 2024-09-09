package com.kitty.system.game.role.domain;

import com.kitty.system.common.enter.FiedValue;
import java.util.List;
import java.io.Serializable;

public class Rune implements Serializable
{
    private Long id;
    private Integer roleId;
    private String name;
    private Integer type;
    private List<FiedValue> position;
    
    public Rune() {
    }
    
    public Long getId() {
        return this.id;
    }
    
    public Integer getRoleId() {
        return this.roleId;
    }
    
    public String getName() {
        return this.name;
    }
    
    public Integer getType() {
        return this.type;
    }
    
    public List<FiedValue> getPosition() {
        return this.position;
    }
    
    public void setId(final Long id) {
        this.id = id;
    }
    
    public void setRoleId(final Integer roleId) {
        this.roleId = roleId;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public void setType(final Integer type) {
        this.type = type;
    }
    
    public void setPosition(final List<FiedValue> position) {
        this.position = position;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Rune)) {
            return false;
        }
        Rune other = (Rune)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$id = this.getId();
        Object other$id = other.getId();
        if (!((this$id != null) ? this$id.equals(other$id) : (other$id == null))) {
            return false;
        }
        Object this$roleId = this.getRoleId();
        Object other$roleId = other.getRoleId();
        if (!((this$roleId != null) ? this$roleId.equals(other$roleId) : (other$roleId == null))) {
            return false;
        }
        Object this$name = this.getName();
        Object other$name = other.getName();
        if (!((this$name != null) ? this$name.equals(other$name) : (other$name == null))) {
            return false;
        }
        Object this$type = this.getType();
        Object other$type = other.getType();
        if (!((this$type != null) ? this$type.equals(other$type) : (other$type == null))) {
            return false;
        }
        Object this$position = this.getPosition();
        Object other$position = other.getPosition();
        return (this$position != null) ? this$position.equals(other$position) : (other$position == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof Rune;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $id = this.getId();
        result = result * 59 + (($id == null) ? 43 : $id.hashCode());
        Object $roleId = this.getRoleId();
        result = result * 59 + (($roleId == null) ? 43 : $roleId.hashCode());
        Object $name = this.getName();
        result = result * 59 + (($name == null) ? 43 : $name.hashCode());
        Object $type = this.getType();
        result = result * 59 + (($type == null) ? 43 : $type.hashCode());
        Object $position = this.getPosition();
        result = result * 59 + (($position == null) ? 43 : $position.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "Rune(id=" + this.getId() + ", roleId=" + this.getRoleId() + ", name=" + this.getName() + ", type=" + this.getType() + ", position=" + this.getPosition() + ")";
    }
}
