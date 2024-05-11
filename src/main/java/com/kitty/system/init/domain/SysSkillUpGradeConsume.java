
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
        final SysSkillUpGradeConsume other = (SysSkillUpGradeConsume)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$grade = this.getGrade();
        final Object other$grade = other.getGrade();
        Label_0065: {
            if (this$grade == null) {
                if (other$grade == null) {
                    break Label_0065;
                }
            }
            else if (this$grade.equals(other$grade)) {
                break Label_0065;
            }
            return false;
        }
        final Object this$alone = this.getAlone();
        final Object other$alone = other.getAlone();
        Label_0102: {
            if (this$alone == null) {
                if (other$alone == null) {
                    break Label_0102;
                }
            }
            else if (this$alone.equals(other$alone)) {
                break Label_0102;
            }
            return false;
        }
        final Object this$more = this.getMore();
        final Object other$more = other.getMore();
        if (this$more == null) {
            if (other$more == null) {
                return true;
            }
        }
        else if (this$more.equals(other$more)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof SysSkillUpGradeConsume;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $grade = this.getGrade();
        result = result * 59 + (($grade == null) ? 43 : $grade.hashCode());
        final Object $alone = this.getAlone();
        result = result * 59 + (($alone == null) ? 43 : $alone.hashCode());
        final Object $more = this.getMore();
        result = result * 59 + (($more == null) ? 43 : $more.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "SysSkillUpGradeConsume(grade=" + this.getGrade() + ", alone=" + this.getAlone() + ", more=" + this.getMore() + ")";
    }
}
