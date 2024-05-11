
package com.kitty.system.game.party.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 8376)
public class ReqPartyMembers extends Message
{
    private byte page;
    private byte size;
    private byte sort;
    private byte way;
    private String para;
    private String list;
    
    public ReqPartyMembers() {
        this.page = 1;
        this.size = 20;
        this.sort = 4;
        this.way = 0;
        this.para = "";
        this.list = "";
    }
    
    public byte getPage() {
        return this.page;
    }
    
    public byte getSize() {
        return this.size;
    }
    
    public byte getSort() {
        return this.sort;
    }
    
    public byte getWay() {
        return this.way;
    }
    
    public String getPara() {
        return this.para;
    }
    
    public String getList() {
        return this.list;
    }
    
    public void setPage(final byte page) {
        this.page = page;
    }
    
    public void setSize(final byte size) {
        this.size = size;
    }
    
    public void setSort(final byte sort) {
        this.sort = sort;
    }
    
    public void setWay(final byte way) {
        this.way = way;
    }
    
    public void setPara(final String para) {
        this.para = para;
    }
    
    public void setList(final String list) {
        this.list = list;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ReqPartyMembers)) {
            return false;
        }
        final ReqPartyMembers other = (ReqPartyMembers)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getPage() != other.getPage()) {
            return false;
        }
        if (this.getSize() != other.getSize()) {
            return false;
        }
        if (this.getSort() != other.getSort()) {
            return false;
        }
        if (this.getWay() != other.getWay()) {
            return false;
        }
        final Object this$para = this.getPara();
        final Object other$para = other.getPara();
        Label_0117: {
            if (this$para == null) {
                if (other$para == null) {
                    break Label_0117;
                }
            }
            else if (this$para.equals(other$para)) {
                break Label_0117;
            }
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
        return other instanceof ReqPartyMembers;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getPage();
        result = result * 59 + this.getSize();
        result = result * 59 + this.getSort();
        result = result * 59 + this.getWay();
        final Object $para = this.getPara();
        result = result * 59 + (($para == null) ? 43 : $para.hashCode());
        final Object $list = this.getList();
        result = result * 59 + (($list == null) ? 43 : $list.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "ReqPartyMembers(page=" + this.getPage() + ", size=" + this.getSize() + ", sort=" + this.getSort() + ", way=" + this.getWay() + ", para=" + this.getPara() + ", list=" + this.getList() + ")";
    }
}
