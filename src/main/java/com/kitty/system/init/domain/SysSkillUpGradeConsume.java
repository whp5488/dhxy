package com.kitty.system.init.domain;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;
import java.io.Serializable;

@Table("data_skill_up_grade_consume")
public class SysSkillUpGradeConsume implements Serializable
{
    @Comment("熟练度等级")
    @Column("grade")
    private Integer grade;
    @Comment("单目标技能耗费")
    @Column("alone")
    private Integer alone;
    @Comment("多目标技能耗费")
    @Column("more")
    private Integer more;
    
    public SysSkillUpGradeConsume() {
    }
    
    public Integer getGrade() {
        return this.grade;
    }
    
    public Integer getAlone() {
        return this.alone;
    }
    
    public Integer getMore() {
        return this.more;
    }
    
    public void setGrade(final Integer grade) {
        this.grade = grade;
    }
    
    public void setAlone(final Integer alone) {
        this.alone = alone;
    }
    
    public void setMore(final Integer more) {
        this.more = more;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SysSkillUpGradeConsume)) {
            return false;
        }
        SysSkillUpGradeConsume other = (SysSkillUpGradeConsume)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$grade = this.getGrade();
        Object other$grade = other.getGrade();
        if (!((this$grade != null) ? this$grade.equals(other$grade) : (other$grade == null))) {
            return false;
        }
        Object this$alone = this.getAlone();
        Object other$alone = other.getAlone();
        if (!((this$alone != null) ? this$alone.equals(other$alone) : (other$alone == null))) {
            return false;
        }
        Object this$more = this.getMore();
        Object other$more = other.getMore();
        return (this$more != null) ? this$more.equals(other$more) : (other$more == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof SysSkillUpGradeConsume;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $grade = this.getGrade();
        result = result * 59 + (($grade == null) ? 43 : $grade.hashCode());
        Object $alone = this.getAlone();
        result = result * 59 + (($alone == null) ? 43 : $alone.hashCode());
        Object $more = this.getMore();
        result = result * 59 + (($more == null) ? 43 : $more.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "SysSkillUpGradeConsume(grade=" + this.getGrade() + ", alone=" + this.getAlone() + ", more=" + this.getMore() + ")";
    }
}
