
package com.kitty.system.game.ranking.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 4226)
public class ReqQlkRanking extends Message
{
    private short qlkType;
    
    public short getQlkType() {
        return this.qlkType;
    }
    
    public void setQlkType(final short qlkType) {
        this.qlkType = qlkType;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ReqQlkRanking)) {
            return false;
        }
        final ReqQlkRanking other = (ReqQlkRanking)o;
        return other.canEqual(this) && this.getQlkType() == other.getQlkType();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ReqQlkRanking;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getQlkType();
        return result;
    }
    
    @Override
    public String toString() {
        return "ReqQlkRanking(qlkType=" + this.getQlkType() + ")";
    }
}
