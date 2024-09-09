package com.kitty.system.game.confirm.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 20736)
public class ReqConfirmResult extends Message
{
    private String select;
    
    public ReqConfirmResult() {
    }
    
    public String getSelect() {
        return this.select;
    }
    
    public void setSelect(final String select) {
        this.select = select;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ReqConfirmResult)) {
            return false;
        }
        ReqConfirmResult other = (ReqConfirmResult)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$select = this.getSelect();
        Object other$select = other.getSelect();
        return (this$select != null) ? this$select.equals(other$select) : (other$select == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ReqConfirmResult;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $select = this.getSelect();
        result = result * 59 + (($select == null) ? 43 : $select.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "ReqConfirmResult(select=" + this.getSelect() + ")";
    }
}
