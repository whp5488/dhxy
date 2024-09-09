package com.kitty.system.game.task.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 53719)
public class ReqSubmitQuestion extends Message
{
    private int type;
    private int serial;
    private String answer;
    
    public ReqSubmitQuestion() {
    }
    
    public int getType() {
        return this.type;
    }
    
    public int getSerial() {
        return this.serial;
    }
    
    public String getAnswer() {
        return this.answer;
    }
    
    public void setType(final int type) {
        this.type = type;
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
        if (!(o instanceof ReqSubmitQuestion)) {
            return false;
        }
        ReqSubmitQuestion other = (ReqSubmitQuestion)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getType() != other.getType()) {
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
        return other instanceof ReqSubmitQuestion;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getType();
        result = result * 59 + this.getSerial();
        Object $answer = this.getAnswer();
        result = result * 59 + (($answer == null) ? 43 : $answer.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "ReqSubmitQuestion(type=" + this.getType() + ", serial=" + this.getSerial() + ", answer=" + this.getAnswer() + ")";
    }
}
