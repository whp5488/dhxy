package com.kitty.system.game.enter;

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
        RespTitleList other = (RespTitleList)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$list = this.getList();
        Object other$list = other.getList();
        return (this$list != null) ? this$list.equals(other$list) : (other$list == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespTitleList;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $list = this.getList();
        result = result * 59 + (($list == null) ? 43 : $list.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "RespTitleList(list=" + this.getList() + ")";
    }
    
    public RespTitleList() {
    }
    
    public RespTitleList(final List<Title> list) {
        this.list = list;
    }
}
