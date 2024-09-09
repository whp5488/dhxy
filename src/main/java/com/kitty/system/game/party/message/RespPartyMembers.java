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
        RespPartyMembers other = (RespPartyMembers)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getPage() != other.getPage()) {
            return false;
        }
        if (this.getTail() != other.getTail()) {
            return false;
        }
        Object this$list = this.getList();
        Object other$list = other.getList();
        return (this$list != null) ? this$list.equals(other$list) : (other$list == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespPartyMembers;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getPage();
        result = result * 59 + this.getTail();
        Object $list = this.getList();
        result = result * 59 + (($list == null) ? 43 : $list.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "RespPartyMembers(page=" + (int)this.getPage() + ", tail=" + (int)this.getTail() + ", list=" + this.getList() + ")";
    }
}
