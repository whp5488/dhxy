
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
        final Rune other = (Rune)o;
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
        final Object this$roleId = this.getRoleId();
        final Object other$roleId = other.getRoleId();
        Label_0102: {
            if (this$roleId == null) {
                if (other$roleId == null) {
                    break Label_0102;
                }
            }
            else if (this$roleId.equals(other$roleId)) {
                break Label_0102;
            }
            return false;
        }
        final Object this$name = this.getName();
        final Object other$name = other.getName();
        Label_0139: {
            if (this$name == null) {
                if (other$name == null) {
                    break Label_0139;
                }
            }
            else if (this$name.equals(other$name)) {
                break Label_0139;
            }
            return false;
        }
        final Object this$type = this.getType();
        final Object other$type = other.getType();
        Label_0176: {
            if (this$type == null) {
                if (other$type == null) {
                    break Label_0176;
                }
            }
            else if (this$type.equals(other$type)) {
                break Label_0176;
            }
            return false;
        }
        final Object this$position = this.getPosition();
        final Object other$position = other.getPosition();
        if (this$position == null) {
            if (other$position == null) {
                return true;
            }
        }
        else if (this$position.equals(other$position)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof Rune;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * 59 + (($id == null) ? 43 : $id.hashCode());
        final Object $roleId = this.getRoleId();
        result = result * 59 + (($roleId == null) ? 43 : $roleId.hashCode());
        final Object $name = this.getName();
        result = result * 59 + (($name == null) ? 43 : $name.hashCode());
        final Object $type = this.getType();
        result = result * 59 + (($type == null) ? 43 : $type.hashCode());
        final Object $position = this.getPosition();
        result = result * 59 + (($position == null) ? 43 : $position.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "Rune(id=" + this.getId() + ", roleId=" + this.getRoleId() + ", name=" + this.getName() + ", type=" + this.getType() + ", position=" + this.getPosition() + ")";
    }
}
