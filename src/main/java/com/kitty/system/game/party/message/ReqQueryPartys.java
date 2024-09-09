package com.kitty.system.game.party.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 32782)
public class ReqQueryPartys extends Message
{
    private String type;
    private String para;
    private byte page;
    private byte size;
    private byte sort;
    private byte way;
    
    public ReqQueryPartys() {
        this.page = 0;
        this.size = 8;
        this.sort = 4;
        this.way = 0;
    }
    
    public String getType() {
        return this.type;
    }
    
    public String getPara() {
        return this.para;
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
    
    public void setType(final String type) {
        this.type = type;
    }
    
    public void setPara(final String para) {
        this.para = para;
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
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ReqQueryPartys)) {
            return false;
        }
        ReqQueryPartys other = (ReqQueryPartys)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$type = this.getType();
        Object other$type = other.getType();
        if (!((this$type != null) ? this$type.equals(other$type) : (other$type == null))) {
            return false;
        }
        Object this$para = this.getPara();
        Object other$para = other.getPara();
        return ((this$para != null) ? this$para.equals(other$para) : (other$para == null)) && this.getPage() == other.getPage() && this.getSize() == other.getSize() && this.getSort() == other.getSort() && this.getWay() == other.getWay();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ReqQueryPartys;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $type = this.getType();
        result = result * 59 + (($type == null) ? 43 : $type.hashCode());
        Object $para = this.getPara();
        result = result * 59 + (($para == null) ? 43 : $para.hashCode());
        result = result * 59 + this.getPage();
        result = result * 59 + this.getSize();
        result = result * 59 + this.getSort();
        result = result * 59 + this.getWay();
        return result;
    }
    
    @Override
    public String toString() {
        return "ReqQueryPartys(type=" + this.getType() + ", para=" + this.getPara() + ", page=" + (int)this.getPage() + ", size=" + (int)this.getSize() + ", sort=" + (int)this.getSort() + ", way=" + (int)this.getWay() + ")";
    }
}
