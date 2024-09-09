package com.kitty.system.game.activity.model.user.ca.vo;

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
        CaCache other = (CaCache)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$id = this.getId();
        Object other$id = other.getId();
        if (!((this$id != null) ? this$id.equals(other$id) : (other$id == null))) {
            return false;
        }
        if (this.getRoleId() != other.getRoleId()) {
            return false;
        }
        if (this.getSerial() != other.getSerial()) {
            return false;
        }
        Object this$name = this.getName();
        Object other$name = other.getName();
        if (!((this$name != null) ? this$name.equals(other$name) : (other$name == null))) {
            return false;
        }
        if (this.getScore() != other.getScore()) {
            return false;
        }
        if (this.getGrade() != other.getGrade()) {
            return false;
        }
        Object this$factionName = this.getFactionName();
        Object other$factionName = other.getFactionName();
        if (!((this$factionName != null) ? this$factionName.equals(other$factionName) : (other$factionName == null))) {
            return false;
        }
        Object this$marking = this.getMarking();
        Object other$marking = other.getMarking();
        return (this$marking != null) ? this$marking.equals(other$marking) : (other$marking == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof CaCache;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $id = this.getId();
        result = result * 59 + (($id == null) ? 43 : $id.hashCode());
        result = result * 59 + this.getRoleId();
        result = result * 59 + this.getSerial();
        Object $name = this.getName();
        result = result * 59 + (($name == null) ? 43 : $name.hashCode());
        result = result * 59 + this.getScore();
        result = result * 59 + this.getGrade();
        Object $factionName = this.getFactionName();
        result = result * 59 + (($factionName == null) ? 43 : $factionName.hashCode());
        Object $marking = this.getMarking();
        result = result * 59 + (($marking == null) ? 43 : $marking.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "CaCache(id=" + this.getId() + ", roleId=" + this.getRoleId() + ", serial=" + this.getSerial() + ", name=" + this.getName() + ", score=" + this.getScore() + ", grade=" + this.getGrade() + ", factionName=" + this.getFactionName() + ", marking=" + this.getMarking() + ")";
    }
    
    public CaCache() {
    }
    
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
