
package com.kitty.system.game.task.message;

import java.beans.ConstructorProperties;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 53723)
public class RespQuestionProvince extends Message
{
    private int time;
    private int serial;
    private int num;
    private int mistakes;
    
    public int getTime() {
        return this.time;
    }
    
    public int getSerial() {
        return this.serial;
    }
    
    public int getNum() {
        return this.num;
    }
    
    public int getMistakes() {
        return this.mistakes;
    }
    
    public void setTime(final int time) {
        this.time = time;
    }
    
    public void setSerial(final int serial) {
        this.serial = serial;
    }
    
    public void setNum(final int num) {
        this.num = num;
    }
    
    public void setMistakes(final int mistakes) {
        this.mistakes = mistakes;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespQuestionProvince)) {
            return false;
        }
        final RespQuestionProvince other = (RespQuestionProvince)o;
        return other.canEqual(this) && this.getTime() == other.getTime() && this.getSerial() == other.getSerial() && this.getNum() == other.getNum() && this.getMistakes() == other.getMistakes();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespQuestionProvince;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getTime();
        result = result * 59 + this.getSerial();
        result = result * 59 + this.getNum();
        result = result * 59 + this.getMistakes();
        return result;
    }
    
    @Override
    public String toString() {
        return "RespQuestionProvince(time=" + this.getTime() + ", serial=" + this.getSerial() + ", num=" + this.getNum() + ", mistakes=" + this.getMistakes() + ")";
    }
    
    public RespQuestionProvince() {
    }
    
    @ConstructorProperties({ "time", "serial", "num", "mistakes" })
    public RespQuestionProvince(final int time, final int serial, final int num, final int mistakes) {
        this.time = time;
        this.serial = serial;
        this.num = num;
        this.mistakes = mistakes;
    }
}
