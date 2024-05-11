
package com.kitty.system.init.domain;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;
import java.io.Serializable;

@Table("data_province_question")
public class SysProvinceQuestion implements Serializable
{
    private static final long serialVersionUID = 1L;
    @Comment("问题编号")
    @Column("serial")
    private int serial;
    @Comment("答案")
    @Column("answer")
    private String answer;
    @Comment("类型：1.常识，2.地理，3.历史，4.文学，5.饮食，6.游戏")
    @Column("type")
    private byte type;
    
    public int getSerial() {
        return this.serial;
    }
    
    public String getAnswer() {
        return this.answer;
    }
    
    public byte getType() {
        return this.type;
    }
    
    public void setSerial(final int serial) {
        this.serial = serial;
    }
    
    public void setAnswer(final String answer) {
        this.answer = answer;
    }
    
    public void setType(final byte type) {
        this.type = type;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SysProvinceQuestion)) {
            return false;
        }
        final SysProvinceQuestion other = (SysProvinceQuestion)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getSerial() != other.getSerial()) {
            return false;
        }
        final Object this$answer = this.getAnswer();
        final Object other$answer = other.getAnswer();
        if (this$answer == null) {
            if (other$answer == null) {
                return this.getType() == other.getType();
            }
        }
        else if (this$answer.equals(other$answer)) {
            return this.getType() == other.getType();
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof SysProvinceQuestion;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getSerial();
        final Object $answer = this.getAnswer();
        result = result * 59 + (($answer == null) ? 43 : $answer.hashCode());
        result = result * 59 + this.getType();
        return result;
    }
    
    @Override
    public String toString() {
        return "SysProvinceQuestion(serial=" + this.getSerial() + ", answer=" + this.getAnswer() + ", type=" + this.getType() + ")";
    }
}
