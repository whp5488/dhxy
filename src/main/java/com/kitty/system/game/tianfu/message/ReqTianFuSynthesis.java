package com.kitty.system.game.tianfu.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 4401)
public class ReqTianFuSynthesis extends Message
{
    private short num;
    
    public ReqTianFuSynthesis() {
    }
    
    public short getNum() {
        return this.num;
    }
    
    public void setNum(final short num) {
        this.num = num;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ReqTianFuSynthesis)) {
            return false;
        }
        ReqTianFuSynthesis other = (ReqTianFuSynthesis)o;
        return other.canEqual(this) && this.getNum() == other.getNum();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ReqTianFuSynthesis;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getNum();
        return result;
    }
    
    @Override
    public String toString() {
        return "ReqTianFuSynthesis(num=" + (int)this.getNum() + ")";
    }
}
