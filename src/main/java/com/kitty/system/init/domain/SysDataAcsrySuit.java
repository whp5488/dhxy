
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
        final SysDataAcsrySuit other = (SysDataAcsrySuit)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$dist = this.getDist();
        final Object other$dist = other.getDist();
        Label_0065: {
            if (this$dist == null) {
                if (other$dist == null) {
                    break Label_0065;
                }
            }
            else if (this$dist.equals(other$dist)) {
                break Label_0065;
            }
            return false;
        }
        final Object this$containList = this.getContainList();
        final Object other$containList = other.getContainList();
        Label_0102: {
            if (this$containList == null) {
                if (other$containList == null) {
                    break Label_0102;
                }
            }
            else if (this$containList.equals(other$containList)) {
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
        if (this.getGrade() != other.getGrade()) {
            return false;
        }
        final Object this$skills = this.getSkills();
        final Object other$skills = other.getSkills();
        if (this$skills == null) {
            if (other$skills == null) {
                return true;
            }
        }
        else if (this$skills.equals(other$skills)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof SysDataAcsrySuit;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $dist = this.getDist();
        result = result * 59 + (($dist == null) ? 43 : $dist.hashCode());
        final Object $containList = this.getContainList();
        result = result * 59 + (($containList == null) ? 43 : $containList.hashCode());
        final Object $name = this.getName();
        result = result * 59 + (($name == null) ? 43 : $name.hashCode());
        result = result * 59 + this.getGrade();
        final Object $skills = this.getSkills();
        result = result * 59 + (($skills == null) ? 43 : $skills.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "SysDataAcsrySuit(dist=" + this.getDist() + ", containList=" + this.getContainList() + ", name=" + this.getName() + ", grade=" + this.getGrade() + ", skills=" + this.getSkills() + ")";
    }
}
