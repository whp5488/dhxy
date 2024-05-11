
package com.kitty.system.game.general.message;

import java.util.ArrayList;
import com.kitty.system.game.general.message.vo.DomesticateInfo;
import java.util.List;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 41481)
public class RespDomesticateInfo extends Message
{
    private List<DomesticateInfo> list;
    
    public RespDomesticateInfo() {
        this.list = new ArrayList<DomesticateInfo>();
    }
    
    public List<DomesticateInfo> getList() {
        return this.list;
    }
    
    public void setList(final List<DomesticateInfo> list) {
        this.list = list;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespDomesticateInfo)) {
            return false;
        }
        final RespDomesticateInfo other = (RespDomesticateInfo)o;
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
        return other instanceof RespDomesticateInfo;
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
        return "RespDomesticateInfo(list=" + this.getList() + ")";
    }
}
