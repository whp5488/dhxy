
package com.kitty.system.game.ranking.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 4229)
public class ReqCaNomRanking extends Message
{
    private byte type;
    private byte classify;
    
    public ReqCaNomRanking() {
        this.type = 0;
        this.classify = 0;
    }
    
    public byte getType() {
        return this.type;
    }
    
    public byte getClassify() {
        return this.classify;
    }
    
    public void setType(final byte type) {
        this.type = type;
    }
    
    public void setClassify(final byte classify) {
        this.classify = classify;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ReqCaNomRanking)) {
            return false;
        }
        final ReqCaNomRanking other = (ReqCaNomRanking)o;
        return other.canEqual(this) && this.getType() == other.getType() && this.getClassify() == other.getClassify();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ReqCaNomRanking;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getType();
        result = result * 59 + this.getClassify();
        return result;
    }
    
    @Override
    public String toString() {
        return "ReqCaNomRanking(type=" + this.getType() + ", classify=" + this.getClassify() + ")";
    }
}
