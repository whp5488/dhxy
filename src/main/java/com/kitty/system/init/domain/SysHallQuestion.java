package com.kitty.system.init.domain;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;
import java.io.Serializable;

@Table("data_hall_question")
public class SysHallQuestion implements Serializable
{
    private static final long serialVersionUID = 1L;
    @Comment("问题编号")
    @Column("serial")
    private int serial;
    @Comment("答案")
    @Column("answer")
    private String answer;
    
    public SysHallQuestion() {
    }
    
    public int getSerial() {
        return this.serial;
    }
    
    public String getAnswer() {
        return this.answer;
    }
    
    public void setSerial(final int serial) {
        this.serial = serial;
    }
    
    public void setAnswer(final String answer) {
        this.answer = answer;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SysHallQuestion)) {
            return false;
        }
        SysHallQuestion other = (SysHallQuestion)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getSerial() != other.getSerial()) {
            return false;
        }
        Object this$answer = this.getAnswer();
        Object other$answer = other.getAnswer();
        return (this$answer != null) ? this$answer.equals(other$answer) : (other$answer == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof SysHallQuestion;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getSerial();
        Object $answer = this.getAnswer();
        result = result * 59 + (($answer == null) ? 43 : $answer.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "SysHallQuestion(serial=" + this.getSerial() + ", answer=" + this.getAnswer() + ")";
    }
}
