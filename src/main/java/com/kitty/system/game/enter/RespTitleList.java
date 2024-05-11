
package com.kitty.system.game.enter;

import java.beans.ConstructorProperties;
import com.kitty.system.game.enter.vo.Title;
import java.util.List;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 62209)
public class RespTitleList extends Message
{
    private List<Title> list;
    
    public List<Title> getList() {
        return this.list;
    }
    
    public void setList(final List<Title> list) {
        this.list = list;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespTitleList)) {
            return false;
        }
        final RespTitleList other = (RespTitleList)o;
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
        return other instanceof RespTitleList;
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
        return "RespTitleList(list=" + this.getList() + ")";
    }
    
    public RespTitleList() {
    }
    
    @ConstructorProperties({ "list" })
    public RespTitleList(final List<Title> list) {
        this.list = list;
    }
}
