package com.kitty.system.init.domain;

import org.nutz.dao.entity.annotation.Default;
import java.util.List;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;

@Table("data_acsry_suit")
public class SysDataAcsrySuit
{
    @Column("dist")
    private String dist;
    @Column("contain")
    @Default("[]")
    private List<List<Integer>> containList;
    @Column("name")
    private String name;
    @Column("grade")
    private int grade;
    @Column("skill")
    @Default("[]")
    private List<Integer> skills;
    
    public SysDataAcsrySuit() {
        this.grade = 1;
    }
    
    public String getDist() {
        return this.dist;
    }
    
    public List<List<Integer>> getContainList() {
        return this.containList;
    }
    
    public String getName() {
        return this.name;
    }
    
    public int getGrade() {
        return this.grade;
    }
    
    public List<Integer> getSkills() {
        return this.skills;
    }
    
    public void setDist(final String dist) {
        this.dist = dist;
    }
    
    public void setContainList(final List<List<Integer>> containList) {
        this.containList = containList;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public void setGrade(final int grade) {
        this.grade = grade;
    }
    
    public void setSkills(final List<Integer> skills) {
        this.skills = skills;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SysDataAcsrySuit)) {
            return false;
        }
        SysDataAcsrySuit other = (SysDataAcsrySuit)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$dist = this.getDist();
        Object other$dist = other.getDist();
        if (!((this$dist != null) ? this$dist.equals(other$dist) : (other$dist == null))) {
            return false;
        }
        Object this$containList = this.getContainList();
        Object other$containList = other.getContainList();
        if (!((this$containList != null) ? this$containList.equals(other$containList) : (other$containList == null))) {
            return false;
        }
        Object this$name = this.getName();
        Object other$name = other.getName();
        if (!((this$name != null) ? this$name.equals(other$name) : (other$name == null))) {
            return false;
        }
        if (this.getGrade() != other.getGrade()) {
            return false;
        }
        Object this$skills = this.getSkills();
        Object other$skills = other.getSkills();
        return (this$skills != null) ? this$skills.equals(other$skills) : (other$skills == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof SysDataAcsrySuit;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $dist = this.getDist();
        result = result * 59 + (($dist == null) ? 43 : $dist.hashCode());
        Object $containList = this.getContainList();
        result = result * 59 + (($containList == null) ? 43 : $containList.hashCode());
        Object $name = this.getName();
        result = result * 59 + (($name == null) ? 43 : $name.hashCode());
        result = result * 59 + this.getGrade();
        Object $skills = this.getSkills();
        result = result * 59 + (($skills == null) ? 43 : $skills.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "SysDataAcsrySuit(dist=" + this.getDist() + ", containList=" + this.getContainList() + ", name=" + this.getName() + ", grade=" + this.getGrade() + ", skills=" + this.getSkills() + ")";
    }
}
