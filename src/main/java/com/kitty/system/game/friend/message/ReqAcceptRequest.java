
package com.kitty.system.game.friend.message;

import java.util.List;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 36868)
public class ReqAcceptRequest extends Message
{
    private List<String> list;
    private byte accept;
    
    public List<String> getList() {
        return this.list;
    }
    
    public byte getAccept() {
        return this.accept;
    }
    
    public void setList(final List<String> list) {
        this.list = list;
    }
    
    public void setAccept(final byte accept) {
        this.accept = accept;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ReqAcceptRequest)) {
            return false;
        }
        final ReqAcceptRequest other = (ReqAcceptRequest)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$list = this.getList();
        final Object other$list = other.getList();
        if (this$list == null) {
            if (other$list == null) {
                return this.getAccept() == other.getAccept();
            }
        }
        else if (this$list.equals(other$list)) {
            return this.getAccept() == other.getAccept();
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ReqAcceptRequest;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $list = this.getList();
        result = result * 59 + (($list == null) ? 43 : $list.hashCode());
        result = result * 59 + this.getAccept();
        return result;
    }
    
    @Override
    public String toString() {
        return "ReqAcceptRequest(list=" + this.getList() + ", accept=" + this.getAccept() + ")";
    }
}
