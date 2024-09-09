package com.kitty.system.init.domain;

import org.nutz.dao.entity.annotation.Default;
import com.kitty.system.init.domain.vo.Param;
import java.util.List;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;
import java.io.Serializable;

@Table("data_grade_award")
public class SysDataGradeAward implements Serializable
{
    private static final long serialVersionUID = 1L;
    @Comment("等级")
    @Column("grade")
    private int grade;
    @Comment("奖励")
    @Column("award")
    @Default("[]")
    private List<Param> award;
    
    public SysDataGradeAward() {
    }
    
    public int getGrade() {
        return this.grade;
    }
    
    public List<Param> getAward() {
        return this.award;
    }
    
    public void setGrade(final int grade) {
        this.grade = grade;
    }
    
    public void setAward(final List<Param> award) {
        this.award = award;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SysDataGradeAward)) {
            return false;
        }
        SysDataGradeAward other = (SysDataGradeAward)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getGrade() != other.getGrade()) {
            return false;
        }
        Object this$award = this.getAward();
        Object other$award = other.getAward();
        return (this$award != null) ? this$award.equals(other$award) : (other$award == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof SysDataGradeAward;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getGrade();
        Object $award = this.getAward();
        result = result * 59 + (($award == null) ? 43 : $award.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "SysDataGradeAward(grade=" + this.getGrade() + ", award=" + this.getAward() + ")";
    }
}
