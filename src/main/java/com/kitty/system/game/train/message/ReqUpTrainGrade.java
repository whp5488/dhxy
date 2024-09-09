package com.kitty.system.game.train.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 20172)
public class ReqUpTrainGrade extends Message
{
    private short position;
    private int grade;
    
    public ReqUpTrainGrade() {
        this.position = 0;
    }
    
    public short getPosition() {
        return this.position;
    }
    
    public int getGrade() {
        return this.grade;
    }
    
    public void setPosition(final short position) {
        this.position = position;
    }
    
    public void setGrade(final int grade) {
        this.grade = grade;
    }
}
