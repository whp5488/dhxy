
package com.kitty.system.game.safe.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 32827)
public class RespSafeLockDlg extends Message
{
    private boolean isSet;
    private boolean unlock;
    private int reset;
    
    public RespSafeLockDlg() {
        this.reset = 0;
    }
    
    public boolean isSet() {
        return this.isSet;
    }
    
    public boolean isUnlock() {
        return this.unlock;
    }
    
    public int getReset() {
        return this.reset;
    }
    
    public void setSet(final boolean isSet) {
        this.isSet = isSet;
    }
    
    public void setUnlock(final boolean unlock) {
        this.unlock = unlock;
    }
    
    public void setReset(final int reset) {
        this.reset = reset;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespSafeLockDlg)) {
            return false;
        }
        final RespSafeLockDlg other = (RespSafeLockDlg)o;
        return other.canEqual(this) && this.isSet() == other.isSet() && this.isUnlock() == other.isUnlock() && this.getReset() == other.getReset();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespSafeLockDlg;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + (this.isSet() ? 79 : 97);
        result = result * 59 + (this.isUnlock() ? 79 : 97);
        result = result * 59 + this.getReset();
        return result;
    }
    
    @Override
    public String toString() {
        return "RespSafeLockDlg(isSet=" + this.isSet() + ", unlock=" + this.isUnlock() + ", reset=" + this.getReset() + ")";
    }
}
