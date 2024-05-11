
package com.kitty.system.game.activity.model.user.ca.vo;

import java.beans.ConstructorProperties;
import java.io.Serializable;

public class CaCache implements Serializable
{
    private Long id;
    private int roleId;
    private int serial;
    private String name;
    private int score;
    private int grade;
    private String factionName;
    private String marking;
    
    public Long getId() {
        return this.id;
    }
    
    public int getRoleId() {
        return this.roleId;
    }
    
    public int getSerial() {
        return this.serial;
    }
    
    public String getName() {
        return this.name;
    }
    
    public int getScore() {
        return this.score;
    }
    
    public int getGrade() {
        return this.grade;
    }
    
    public String getFactionName() {
        return this.factionName;
    }
    
    public String getMarking() {
        return this.marking;
    }
    
    public void setId(final Long id) {
        this.id = id;
    }
    
    public void setRoleId(final int roleId) {
        this.roleId = roleId;
    }
    
    public void setSerial(final int serial) {
        this.serial = serial;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public void setScore(final int score) {
        this.score = score;
    }
    
    public void setGrade(final int grade) {
        this.grade = grade;
    }
    
    public void setFactionName(final String factionName) {
        this.factionName = factionName;
    }
    
    public void setMarking(final String marking) {
        this.marking = marking;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CaCache)) {
            return false;
        }
        final CaCache other = (CaCache)o;
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
        if (this.getRoleId() != other.getRoleId()) {
            return false;
        }
        if (this.getSerial() != other.getSerial()) {
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
        if (this.getScore() != other.getScore()) {
            return false;
        }
        if (this.getGrade() != other.getGrade()) {
            return false;
        }
        final Object this$factionName = this.getFactionName();
        final Object other$factionName = other.getFactionName();
        Label_0191: {
            if (this$factionName == null) {
                if (other$factionName == null) {
                    break Label_0191;
                }
            }
            else if (this$factionName.equals(other$factionName)) {
                break Label_0191;
            }
            return false;
        }
        final Object this$marking = this.getMarking();
        final Object other$marking = other.getMarking();
        if (this$marking == null) {
            if (other$marking == null) {
                return true;
            }
        }
        else if (this$marking.equals(other$marking)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof CaCache;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * 59 + (($id == null) ? 43 : $id.hashCode());
        result = result * 59 + this.getRoleId();
        result = result * 59 + this.getSerial();
        final Object $name = this.getName();
        result = result * 59 + (($name == null) ? 43 : $name.hashCode());
        result = result * 59 + this.getScore();
        result = result * 59 + this.getGrade();
        final Object $factionName = this.getFactionName();
        result = result * 59 + (($factionName == null) ? 43 : $factionName.hashCode());
        final Object $marking = this.getMarking();
        result = result * 59 + (($marking == null) ? 43 : $marking.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "CaCache(id=" + this.getId() + ", roleId=" + this.getRoleId() + ", serial=" + this.getSerial() + ", name=" + this.getName() + ", score=" + this.getScore() + ", grade=" + this.getGrade() + ", factionName=" + this.getFactionName() + ", marking=" + this.getMarking() + ")";
    }
    
    public CaCache() {
    }
    
    @ConstructorProperties({ "id", "roleId", "serial", "name", "score", "grade", "factionName", "marking" })
    public CaCache(final Long id, final int roleId, final int serial, final String name, final int score, final int grade, final String factionName, final String marking) {
        this.id = id;
        this.roleId = roleId;
        this.serial = serial;
        this.name = name;
        this.score = score;
        this.grade = grade;
        this.factionName = factionName;
        this.marking = marking;
    }
}
