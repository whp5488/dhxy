
package com.kitty.system.game.fightnew.messages.old;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 11731)
public class RespCombatCommandAccepted extends Message
{
    private int id;
    private int result;
    
    public RespCombatCommandAccepted() {
        this.result = 0;
    }
    
    public int getId() {
        return this.id;
    }
    
    public int getResult() {
        return this.result;
    }
    
    public void setId(final int id) {
        this.id = id;
    }
    
    public void setResult(final int result) {
        this.result = result;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespCombatCommandAccepted)) {
            return false;
        }
        final RespCombatCommandAccepted other = (RespCombatCommandAccepted)o;
        return other.canEqual(this) && this.getId() == other.getId() && this.getResult() == other.getResult();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespCombatCommandAccepted;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getId();
        result = result * 59 + this.getResult();
        return result;
    }
    
    @Override
    public String toString() {
        return "RespCombatCommandAccepted(id=" + this.getId() + ", result=" + this.getResult() + ")";
    }
}
