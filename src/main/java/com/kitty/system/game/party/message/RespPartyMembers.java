
package com.kitty.system.game.party.message;

import com.kitty.system.game.party.model.vo.PartyMemberObject;
import java.util.List;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 61603)
public class RespPartyMembers extends Message
{
    private byte page;
    private short tail;
    private List<PartyMemberObject> list;
    
    public RespPartyMembers() {
        this.page = 0;
        this.tail = 0;
    }
    
    public byte getPage() {
        return this.page;
    }
    
    public short getTail() {
        return this.tail;
    }
    
    public List<PartyMemberObject> getList() {
        return this.list;
    }
    
    public void setPage(final byte page) {
        this.page = page;
    }
    
    public void setTail(final short tail) {
        this.tail = tail;
    }
    
    public void setList(final List<PartyMemberObject> list) {
        this.list = list;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespPartyMembers)) {
            return false;
        }
        final RespPartyMembers other = (RespPartyMembers)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getPage() != other.getPage()) {
            return false;
        }
        if (this.getTail() != other.getTail()) {
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
        return other instanceof RespPartyMembers;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getPage();
        result = result * 59 + this.getTail();
        final Object $list = this.getList();
        result = result * 59 + (($list == null) ? 43 : $list.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "RespPartyMembers(page=" + this.getPage() + ", tail=" + this.getTail() + ", list=" + this.getList() + ")";
    }
}
