
package com.kitty.system.game.tianfu.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 4403)
public class ReqDayInCeUpgrade extends Message
{
    private Integer addLevel;
    
    public Integer getAddLevel() {
        return this.addLevel;
    }
    
    public void setAddLevel(final Integer addLevel) {
        this.addLevel = addLevel;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ReqDayInCeUpgrade)) {
            return false;
        }
        final ReqDayInCeUpgrade other = (ReqDayInCeUpgrade)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$addLevel = this.getAddLevel();
        final Object other$addLevel = other.getAddLevel();
        if (this$addLevel == null) {
            if (other$addLevel == null) {
                return true;
            }
        }
        else if (this$addLevel.equals(other$addLevel)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ReqDayInCeUpgrade;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $addLevel = this.getAddLevel();
        result = result * 59 + (($addLevel == null) ? 43 : $addLevel.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "ReqDayInCeUpgrade(addLevel=" + this.getAddLevel() + ")";
    }
}
