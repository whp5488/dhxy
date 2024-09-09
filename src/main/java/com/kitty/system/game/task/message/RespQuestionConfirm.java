package com.kitty.system.game.task.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 53720)
public class RespQuestionConfirm extends Message
{
    private byte type;
    
    public byte getType() {
        return this.type;
    }
    
    public void setType(final byte type) {
        this.type = type;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespQuestionConfirm)) {
            return false;
        }
        RespQuestionConfirm other = (RespQuestionConfirm)o;
        return other.canEqual(this) && this.getType() == other.getType();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespQuestionConfirm;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getType();
        return result;
    }
    
    @Override
    public String toString() {
        return "RespQuestionConfirm(type=" + (int)this.getType() + ")";
    }
    
    public RespQuestionConfirm() {
        this.type = 0;
    }
    
    public RespQuestionConfirm(final byte type) {
        this.type = 0;
        this.type = type;
    }
}
