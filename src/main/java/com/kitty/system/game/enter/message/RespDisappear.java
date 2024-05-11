
package com.kitty.system.game.enter.message;

import java.beans.ConstructorProperties;
import com.kitty.system.enums.war.wardef.LOGIC;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 12285)
public class RespDisappear extends Message
{
    private int id;
    private int type;
    
    public int getId() {
        return this.id;
    }
    
    public int getType() {
        return this.type;
    }
    
    public void setId(final int id) {
        this.id = id;
    }
    
    public void setType(final int type) {
        this.type = type;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespDisappear)) {
            return false;
        }
        final RespDisappear other = (RespDisappear)o;
        return other.canEqual(this) && this.getId() == other.getId() && this.getType() == other.getType();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespDisappear;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getId();
        result = result * 59 + this.getType();
        return result;
    }
    
    @Override
    public String toString() {
        return "RespDisappear(id=" + this.getId() + ", type=" + this.getType() + ")";
    }
    
    public RespDisappear() {
        this.type = LOGIC.LOGICTYPE_HERO.getValue();
    }
    
    @ConstructorProperties({ "id", "type" })
    public RespDisappear(final int id, final int type) {
        this.type = LOGIC.LOGICTYPE_HERO.getValue();
        this.id = id;
        this.type = type;
    }
}
