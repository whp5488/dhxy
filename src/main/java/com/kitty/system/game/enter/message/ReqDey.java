package com.kitty.system.game.enter.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 64700)
public class ReqDey extends Message
{
    public int colorOne;
    public int colorTwo;
    
    public ReqDey() {
    }
    
    public int getColorOne() {
        return this.colorOne;
    }
    
    public int getColorTwo() {
        return this.colorTwo;
    }
    
    public void setColorOne(final int colorOne) {
        this.colorOne = colorOne;
    }
    
    public void setColorTwo(final int colorTwo) {
        this.colorTwo = colorTwo;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ReqDey)) {
            return false;
        }
        ReqDey other = (ReqDey)o;
        return other.canEqual(this) && this.getColorOne() == other.getColorOne() && this.getColorTwo() == other.getColorTwo();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ReqDey;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getColorOne();
        result = result * 59 + this.getColorTwo();
        return result;
    }
    
    @Override
    public String toString() {
        return "ReqDey(colorOne=" + this.getColorOne() + ", colorTwo=" + this.getColorTwo() + ")";
    }
}
