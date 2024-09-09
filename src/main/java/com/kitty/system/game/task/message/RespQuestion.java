package com.kitty.system.game.task.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 53718)
public class RespQuestion extends Message
{
    private int type;
    private int serial;
    private int num;
    
    public int getType() {
        return this.type;
    }
    
    public int getSerial() {
        return this.serial;
    }
    
    public int getNum() {
        return this.num;
    }
    
    public void setType(final int type) {
        this.type = type;
    }
    
    public void setSerial(final int serial) {
        this.serial = serial;
    }
    
    public void setNum(final int num) {
        this.num = num;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespQuestion)) {
            return false;
        }
        RespQuestion other = (RespQuestion)o;
        return other.canEqual(this) && this.getType() == other.getType() && this.getSerial() == other.getSerial() && this.getNum() == other.getNum();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespQuestion;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getType();
        result = result * 59 + this.getSerial();
        result = result * 59 + this.getNum();
        return result;
    }
    
    @Override
    public String toString() {
        return "RespQuestion(type=" + this.getType() + ", serial=" + this.getSerial() + ", num=" + this.getNum() + ")";
    }
    
    public RespQuestion() {
    }
    
    public RespQuestion(final int type, final int serial, final int num) {
        this.type = type;
        this.serial = serial;
        this.num = num;
    }
}
