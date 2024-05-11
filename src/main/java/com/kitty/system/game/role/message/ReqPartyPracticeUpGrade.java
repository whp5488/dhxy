
package com.kitty.system.game.role.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 28840)
public class ReqPartyPracticeUpGrade extends Message
{
    private int grade;
    
    public int getGrade() {
        return this.grade;
    }
    
    public void setGrade(final int grade) {
        this.grade = grade;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ReqPartyPracticeUpGrade)) {
            return false;
        }
        final ReqPartyPracticeUpGrade other = (ReqPartyPracticeUpGrade)o;
        return other.canEqual(this) && this.getGrade() == other.getGrade();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ReqPartyPracticeUpGrade;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getGrade();
        return result;
    }
    
    @Override
    public String toString() {
        return "ReqPartyPracticeUpGrade(grade=" + this.getGrade() + ")";
    }
}
