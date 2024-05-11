
package com.kitty.system.common.message;

import java.beans.ConstructorProperties;
import com.kitty.system.common.enter.FiedValue;
import java.util.List;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 45060)
public class RespStateRefresh extends Message
{
    private List<FiedValue> list;
    
    public List<FiedValue> getList() {
        return this.list;
    }
    
    public void setList(final List<FiedValue> list) {
        this.list = list;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespStateRefresh)) {
            return false;
        }
        final RespStateRefresh other = (RespStateRefresh)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$list = this.getList();
        final Object other$list = other.getList();
        if (this$list == null) {
            if (other$list == null) {
                return true;
            }
        }
        else if (this$list.equals(other$list)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespStateRefresh;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $list = this.getList();
        result = result * 59 + (($list == null) ? 43 : $list.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "RespStateRefresh(list=" + this.getList() + ")";
    }
    
    @ConstructorProperties({ "list" })
    public RespStateRefresh(final List<FiedValue> list) {
        this.list = list;
    }
    
    public RespStateRefresh() {
    }
}
