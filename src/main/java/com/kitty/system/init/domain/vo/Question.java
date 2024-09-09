package com.kitty.system.init.domain.vo;

import java.io.Serializable;

public class Question implements Serializable
{
    private int serial;
    private String answer;
    private byte type;
    
    public Question(int serial, String answer) {
        this.type = -1;
        this.serial = serial;
        this.answer = answer;
    }
    
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
        if (!(o instanceof Question)) {
            return false;
        }
        Question other = (Question)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getSerial() != other.getSerial()) {
            return false;
        }
        Object this$answer = this.getAnswer();
        Object other$answer = other.getAnswer();
        return ((this$answer != null) ? this$answer.equals(other$answer) : (other$answer == null)) && this.getType() == other.getType();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof Question;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getSerial();
        Object $answer = this.getAnswer();
        result = result * 59 + (($answer == null) ? 43 : $answer.hashCode());
        result = result * 59 + this.getType();
        return result;
    }
    
    @Override
    public String toString() {
        return "Question(serial=" + this.getSerial() + ", answer=" + this.getAnswer() + ", type=" + (int)this.getType() + ")";
    }
    
    public Question(final int serial, final String answer, final byte type) {
        this.type = -1;
        this.serial = serial;
        this.answer = answer;
        this.type = type;
    }
    
    public Question() {
        this.type = -1;
    }
}
