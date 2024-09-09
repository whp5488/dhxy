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
        ReqPartyMembers other = (ReqPartyMembers)o;
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
        Object this$para = this.getPara();
        Object other$para = other.getPara();
        if (!((this$para != null) ? this$para.equals(other$para) : (other$para == null))) {
            return false;
        }
        Object this$list = this.getList();
        Object other$list = other.getList();
        return (this$list != null) ? this$list.equals(other$list) : (other$list == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ReqPartyMembers;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getPage();
        result = result * 59 + this.getSize();
        result = result * 59 + this.getSort();
        result = result * 59 + this.getWay();
        Object $para = this.getPara();
        result = result * 59 + (($para == null) ? 43 : $para.hashCode());
        Object $list = this.getList();
        result = result * 59 + (($list == null) ? 43 : $list.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "ReqPartyMembers(page=" + (int)this.getPage() + ", size=" + (int)this.getSize() + ", sort=" + (int)this.getSort() + ", way=" + (int)this.getWay() + ", para=" + this.getPara() + ", list=" + this.getList() + ")";
    }
}
